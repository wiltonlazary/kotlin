/*
 * Copyright 2010-2023 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.analysis.api.fir.evaluate

import org.jetbrains.kotlin.analysis.api.KaSession
import org.jetbrains.kotlin.analysis.api.annotations.*
import org.jetbrains.kotlin.analysis.api.base.KaConstantValue
import org.jetbrains.kotlin.analysis.api.base.KaConstantValueFactory
import org.jetbrains.kotlin.analysis.api.components.KaConstantEvaluationMode
import org.jetbrains.kotlin.analysis.api.fir.KaSymbolByFirBuilder
import org.jetbrains.kotlin.descriptors.ClassKind
import org.jetbrains.kotlin.fir.analysis.checkers.getContainingClassSymbol
import org.jetbrains.kotlin.fir.declarations.FirClass
import org.jetbrains.kotlin.fir.declarations.getTargetType
import org.jetbrains.kotlin.fir.diagnostics.ConeSimpleDiagnostic
import org.jetbrains.kotlin.fir.expressions.*
import org.jetbrains.kotlin.fir.psi
import org.jetbrains.kotlin.fir.references.FirResolvedNamedReference
import org.jetbrains.kotlin.fir.resolve.diagnostics.ConeUnresolvedNameError
import org.jetbrains.kotlin.fir.resolve.diagnostics.ConeUnresolvedSymbolError
import org.jetbrains.kotlin.fir.resolve.diagnostics.ConeUnresolvedTypeQualifierError
import org.jetbrains.kotlin.fir.resolve.fullyExpandedType
import org.jetbrains.kotlin.fir.symbols.impl.FirConstructorSymbol
import org.jetbrains.kotlin.fir.symbols.impl.FirEnumEntrySymbol
import org.jetbrains.kotlin.fir.symbols.impl.FirNamedFunctionSymbol
import org.jetbrains.kotlin.fir.types.*
import org.jetbrains.kotlin.name.CallableId
import org.jetbrains.kotlin.name.ClassId
import org.jetbrains.kotlin.name.FqNameUnsafe
import org.jetbrains.kotlin.name.Name
import org.jetbrains.kotlin.psi.KtCallElement
import org.jetbrains.kotlin.psi.KtElement
import org.jetbrains.kotlin.resolve.ArrayFqNames

internal object FirAnnotationValueConverter {
    fun toNamedConstantValue(
        analysisSession: KaSession,
        argumentMapping: Map<Name, FirExpression>,
        builder: KaSymbolByFirBuilder,
    ): List<KaNamedAnnotationValue> = argumentMapping.map { (name, expression) ->
        KaNamedAnnotationValue(
            name,
            expression.convertConstantExpression(builder) ?: KaUnsupportedAnnotationValue(analysisSession.token),
            analysisSession.token
        )
    }

    private fun FirLiteralExpression.convertConstantExpression(
        analysisSession: KaSession
    ): KaConstantAnnotationValue? {
        val expression = psi as? KtElement

        @OptIn(UnresolvedExpressionTypeAccess::class)
        val type = coneTypeOrNull
        val constantValue = when {
            value == null -> KaConstantValue.KaNullConstantValue(expression)
            type == null -> KaConstantValueFactory.createConstantValue(value, psi as? KtElement)
            type.isBoolean -> KaConstantValue.KaBooleanConstantValue(value as Boolean, expression)
            type.isChar -> KaConstantValue.KaCharConstantValue((value as? Char) ?: (value as Number).toInt().toChar(), expression)
            type.isByte -> KaConstantValue.KaByteConstantValue((value as Number).toByte(), expression)
            type.isUByte -> KaConstantValue.KaUnsignedByteConstantValue((value as Number).toByte().toUByte(), expression)
            type.isShort -> KaConstantValue.KaShortConstantValue((value as Number).toShort(), expression)
            type.isUShort -> KaConstantValue.KaUnsignedShortConstantValue((value as Number).toShort().toUShort(), expression)
            type.isInt -> KaConstantValue.KaIntConstantValue((value as Number).toInt(), expression)
            type.isUInt -> KaConstantValue.KaUnsignedIntConstantValue((value as Number).toInt().toUInt(), expression)
            type.isLong -> KaConstantValue.KaLongConstantValue((value as Number).toLong(), expression)
            type.isULong -> KaConstantValue.KaUnsignedLongConstantValue((value as Number).toLong().toULong(), expression)
            type.isString -> KaConstantValue.KaStringConstantValue(value.toString(), expression)
            type.isFloat -> KaConstantValue.KaFloatConstantValue((value as Number).toFloat(), expression)
            type.isDouble -> KaConstantValue.KaDoubleConstantValue((value as Number).toDouble(), expression)
            else -> null
        }

        return constantValue?.let { KaConstantAnnotationValue(it, analysisSession.token) }
    }

    private fun Collection<FirExpression>.convertVarargsExpression(
        builder: KaSymbolByFirBuilder,
    ): Pair<Collection<KaAnnotationValue>, KtElement?> {
        var representativePsi: KtElement? = null
        val flattenedVarargs = buildList {
            for (expr in this@convertVarargsExpression) {
                val converted = expr.convertConstantExpression(builder) ?: continue

                if ((expr is FirSpreadArgumentExpression || expr is FirNamedArgumentExpression) && converted is KaArrayAnnotationValue) {
                    addAll(converted.values)
                } else {
                    add(converted)
                }
                representativePsi = representativePsi ?: converted.sourcePsi
            }
        }

        return flattenedVarargs to representativePsi
    }


    fun toConstantValue(
        firExpression: FirExpression,
        builder: KaSymbolByFirBuilder,
    ): KaAnnotationValue? = firExpression.convertConstantExpression(builder)

    private fun FirExpression.convertConstantExpression(builder: KaSymbolByFirBuilder): KaAnnotationValue? {
        val token = builder.analysisSession.token
        val sourcePsi = psi as? KtElement

        return when (this) {
            is FirLiteralExpression -> convertConstantExpression(builder.analysisSession)
            is FirNamedArgumentExpression -> {
                expression.convertConstantExpression(builder)
            }

            is FirSpreadArgumentExpression -> {
                expression.convertConstantExpression(builder)
            }

            is FirVarargArgumentsExpression -> {
                // Vararg arguments may have multiple independent expressions associated.
                // Choose one to be the representative PSI value for the entire assembled argument.
                val (annotationValues, representativePsi) = arguments.convertVarargsExpression(builder)
                KaArrayAnnotationValue(annotationValues, representativePsi ?: sourcePsi, token)
            }

            is FirArrayLiteral -> {
                // Desugared collection literals.
                KaArrayAnnotationValue(argumentList.arguments.convertVarargsExpression(builder).first, sourcePsi, token)
            }

            is FirFunctionCall -> {
                val reference = calleeReference as? FirResolvedNamedReference ?: return null
                when (val resolvedSymbol = reference.resolvedSymbol) {
                    is FirConstructorSymbol -> {
                        val classSymbol = resolvedSymbol.getContainingClassSymbol(builder.rootSession) ?: return null
                        if ((classSymbol.fir as? FirClass)?.classKind == ClassKind.ANNOTATION_CLASS) {
                            val resultMap = mutableMapOf<Name, FirExpression>()
                            resolvedArgumentMapping?.entries?.forEach { (arg, param) ->
                                resultMap[param.name] = arg
                            }

                            KaAnnotationApplicationValue(
                                KaAnnotationApplicationWithArgumentsInfo(
                                    resolvedSymbol.callableId.classId,
                                    psi as? KtCallElement,
                                    useSiteTarget = null,
                                    toNamedConstantValue(builder.analysisSession, resultMap, builder),
                                    index = null,
                                    constructorSymbolPointer = with(builder.analysisSession) {
                                        builder.functionLikeBuilder.buildConstructorSymbol(resolvedSymbol).createPointer()
                                    },
                                    token = token
                                ),
                                token
                            )
                        } else null
                    }

                    is FirNamedFunctionSymbol -> {
                        // arrayOf call with a single vararg argument.
                        if (resolvedSymbol.callableId.asSingleFqName() in ArrayFqNames.ARRAY_CALL_FQ_NAMES)
                            argumentList.arguments.singleOrNull()?.convertConstantExpression(builder)
                                ?: KaArrayAnnotationValue(emptyList(), sourcePsi, token)
                        else null
                    }

                    is FirEnumEntrySymbol -> {
                        KaEnumEntryAnnotationValue(resolvedSymbol.callableId, sourcePsi, token)
                    }

                    else -> null
                }
            }

            is FirPropertyAccessExpression -> {
                val reference = calleeReference as? FirResolvedNamedReference ?: return null
                when (val resolvedSymbol = reference.resolvedSymbol) {
                    is FirEnumEntrySymbol -> {
                        KaEnumEntryAnnotationValue(resolvedSymbol.callableId, sourcePsi, token)
                    }

                    else -> null
                }
            }

            is FirEnumEntryDeserializedAccessExpression -> {
                KaEnumEntryAnnotationValue(CallableId(enumClassId, enumEntryName), sourcePsi, token)
            }

            is FirGetClassCall -> {
                val coneType = getTargetType()?.fullyExpandedType(builder.rootSession)

                if (coneType is ConeClassLikeType && coneType !is ConeErrorType) {
                    val classId = coneType.lookupTag.classId
                    val type = builder.typeBuilder.buildKtType(coneType)
                    KaKClassAnnotationValue(type, classId, sourcePsi, token)
                } else {
                    val classId = computeErrorCallClassId(this)
                    val diagnostic = classId?.let(::ConeUnresolvedSymbolError) ?: ConeSimpleDiagnostic("Unresolved class reference")
                    val errorType = builder.typeBuilder.buildKtType(ConeErrorType(diagnostic))
                    KaKClassAnnotationValue(errorType, classId, sourcePsi, token)
                }
            }

            else -> null
        } ?: FirCompileTimeConstantEvaluator.evaluate(this, KaConstantEvaluationMode.CONSTANT_EXPRESSION_EVALUATION)
            ?.convertConstantExpression(builder.analysisSession)
    }

    private fun computeErrorCallClassId(call: FirGetClassCall): ClassId? {
        val qualifierParts = mutableListOf<String?>()

        fun process(expression: FirExpression) {
            val errorType = expression.resolvedType as? ConeErrorType
            val unresolvedName = when (val diagnostic = errorType?.diagnostic) {
                is ConeUnresolvedTypeQualifierError -> diagnostic.qualifier
                is ConeUnresolvedNameError -> diagnostic.qualifier
                else -> null
            }
            qualifierParts += unresolvedName
            if (errorType != null && expression is FirPropertyAccessExpression) {
                expression.explicitReceiver?.let { process(it) }
            }
        }

        process(call.argument)

        val fqNameString = qualifierParts.asReversed().filterNotNull().takeIf { it.isNotEmpty() }?.joinToString(".")
        if (fqNameString != null) {
            val fqNameUnsafe = FqNameUnsafe(fqNameString)
            if (fqNameUnsafe.isSafe) {
                return ClassId.topLevel(fqNameUnsafe.toSafe())
            }
        }

        return null
    }
}
