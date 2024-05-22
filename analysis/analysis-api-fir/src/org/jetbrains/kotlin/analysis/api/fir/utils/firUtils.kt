/*
 * Copyright 2010-2023 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */
package org.jetbrains.kotlin.analysis.api.fir.utils

import com.intellij.psi.PsiElement
import org.jetbrains.kotlin.analysis.api.KaConstantInitializerValue
import org.jetbrains.kotlin.analysis.api.KaConstantValueForAnnotation
import org.jetbrains.kotlin.analysis.api.KaInitializerValue
import org.jetbrains.kotlin.analysis.api.KaNonConstantInitializerValue
import org.jetbrains.kotlin.analysis.api.components.KaConstantEvaluationMode
import org.jetbrains.kotlin.analysis.api.fir.KaSymbolByFirBuilder
import org.jetbrains.kotlin.analysis.api.fir.evaluate.FirAnnotationValueConverter
import org.jetbrains.kotlin.analysis.api.fir.evaluate.FirCompileTimeConstantEvaluator
import org.jetbrains.kotlin.analysis.api.types.KaTypeNullability
import org.jetbrains.kotlin.descriptors.ClassKind
import org.jetbrains.kotlin.fir.FirSession
import org.jetbrains.kotlin.fir.analysis.checkers.classKind
import org.jetbrains.kotlin.fir.analysis.checkers.getContainingClassSymbol
import org.jetbrains.kotlin.fir.declarations.FirDeclarationOrigin
import org.jetbrains.kotlin.fir.declarations.utils.isInner
import org.jetbrains.kotlin.fir.declarations.utils.isStatic
import org.jetbrains.kotlin.fir.expressions.FirExpression
import org.jetbrains.kotlin.fir.psi
import org.jetbrains.kotlin.fir.symbols.impl.FirCallableSymbol
import org.jetbrains.kotlin.fir.symbols.impl.FirConstructorSymbol
import org.jetbrains.kotlin.fir.types.ConeNullability
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.psi.*

internal fun PsiElement.unwrap(): PsiElement {
    return when (this) {
        is KtExpression -> this.unwrap()
        else -> this
    }
}

internal fun KtExpression.unwrap(): KtExpression {
    return when (this) {
        is KtLabeledExpression -> baseExpression?.unwrap()
        is KtAnnotatedExpression -> baseExpression?.unwrap()
        is KtFunctionLiteral -> (parent as? KtLambdaExpression)?.unwrap()
        else -> this
    } ?: this
}

internal fun KaTypeNullability.toConeNullability() = when (this) {
    KaTypeNullability.NULLABLE -> ConeNullability.NULLABLE
    KaTypeNullability.NON_NULLABLE -> ConeNullability.NOT_NULL
    KaTypeNullability.UNKNOWN -> ConeNullability.UNKNOWN
}

/**
 * @receiver A symbol that needs to be imported
 * @param useSiteSession A use-site fir session.
 * @return An [FqName] by which this symbol can be imported (if it is possible)
 */
internal fun FirCallableSymbol<*>.computeImportableName(useSiteSession: FirSession): FqName? {
    if (callableId.isLocal) return null

    // SAM constructors are synthetic, but can be imported
    if (origin is FirDeclarationOrigin.SamConstructor) return callableId.asSingleFqName()

    // if classId == null, callable is topLevel
    val containingClassId = callableId.classId
        ?: return callableId.asSingleFqName()

    val containingClass = getContainingClassSymbol(useSiteSession) ?: return null

    if (this is FirConstructorSymbol) return if (!containingClass.isInner) containingClassId.asSingleFqName() else null

    // Java static members, enums, and object members can be imported
    val canBeImported = containingClass.origin is FirDeclarationOrigin.Java && isStatic ||
            containingClass.classKind == ClassKind.ENUM_CLASS && isStatic ||
            containingClass.classKind == ClassKind.OBJECT

    return if (canBeImported) callableId.asSingleFqName() else null
}

internal fun FirExpression.asKaInitializerValue(builder: KaSymbolByFirBuilder, forAnnotationDefaultValue: Boolean): KaInitializerValue {
    val ktExpression = psi as? KtExpression
    val evaluated = FirCompileTimeConstantEvaluator.evaluateAsKtConstantValue(this, KaConstantEvaluationMode.CONSTANT_EXPRESSION_EVALUATION)

    return when (evaluated) {
        null -> if (forAnnotationDefaultValue) {
            val annotationConstantValue = FirAnnotationValueConverter.toConstantValue(this, builder)
            if (annotationConstantValue != null) {
                KaConstantValueForAnnotation(annotationConstantValue, ktExpression)
            } else {
                KaNonConstantInitializerValue(ktExpression)
            }
        } else {
            KaNonConstantInitializerValue(ktExpression)
        }
        else -> KaConstantInitializerValue(evaluated, ktExpression)
    }
}
