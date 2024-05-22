/*
 * Copyright 2010-2022 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.analysis.api.impl.base.test.cases.components

import com.intellij.psi.PsiElement
import org.jetbrains.kotlin.analysis.api.KaAnalysisApiInternals
import org.jetbrains.kotlin.analysis.api.KaSession
import org.jetbrains.kotlin.analysis.api.calls.KaApplicableCallCandidateInfo
import org.jetbrains.kotlin.analysis.api.calls.KaCall
import org.jetbrains.kotlin.analysis.api.calls.KaCallCandidateInfo
import org.jetbrains.kotlin.analysis.api.calls.KaCallInfo
import org.jetbrains.kotlin.analysis.api.calls.KaCallableMemberCall
import org.jetbrains.kotlin.analysis.api.calls.KaCompoundArrayAccessCall
import org.jetbrains.kotlin.analysis.api.calls.KaCompoundVariableAccessCall
import org.jetbrains.kotlin.analysis.api.calls.KaErrorCallInfo
import org.jetbrains.kotlin.analysis.api.calls.KaInapplicableCallCandidateInfo
import org.jetbrains.kotlin.analysis.api.calls.calls
import org.jetbrains.kotlin.analysis.api.calls.symbol
import org.jetbrains.kotlin.analysis.api.diagnostics.KaDiagnostic
import org.jetbrains.kotlin.analysis.api.impl.base.KaChainedSubstitutor
import org.jetbrains.kotlin.analysis.api.impl.base.KaMapBackedSubstitutor
import org.jetbrains.kotlin.analysis.api.renderer.declarations.impl.KaDeclarationRendererForSource
import org.jetbrains.kotlin.analysis.api.renderer.declarations.modifiers.renderers.KaRendererKeywordFilter
import org.jetbrains.kotlin.analysis.api.scopes.KaScope
import org.jetbrains.kotlin.analysis.api.signatures.KaCallableSignature
import org.jetbrains.kotlin.analysis.api.signatures.KaFunctionLikeSignature
import org.jetbrains.kotlin.analysis.api.signatures.KaVariableLikeSignature
import org.jetbrains.kotlin.analysis.api.symbols.*
import org.jetbrains.kotlin.analysis.api.symbols.markers.KaNamedSymbol
import org.jetbrains.kotlin.analysis.api.types.KaSubstitutor
import org.jetbrains.kotlin.analysis.api.types.KaType
import org.jetbrains.kotlin.analysis.utils.printer.prettyPrint
import org.jetbrains.kotlin.name.CallableId
import org.jetbrains.kotlin.name.Name
import org.jetbrains.kotlin.test.services.TestServices
import org.jetbrains.kotlin.test.services.assertions
import org.jetbrains.kotlin.types.Variance
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.KVisibility
import kotlin.reflect.full.memberProperties

@OptIn(KaAnalysisApiInternals::class)
internal fun KaSession.stringRepresentation(any: Any?): String = with(any) {
    fun KaType.render() = asStringForDebugging().replace('/', '.')
    return when (this) {
        null -> "null"
        is KaFunctionLikeSymbol -> buildString {
            append(
                when (this@with) {
                    is KaFunctionSymbol -> callableIdIfNonLocal ?: name
                    is KaSamConstructorSymbol -> callableIdIfNonLocal ?: name
                    is KaConstructorSymbol -> "<constructor>"
                    is KaPropertyGetterSymbol -> callableIdIfNonLocal ?: "<getter>"
                    is KaPropertySetterSymbol -> callableIdIfNonLocal ?: "<setter>"
                    else -> error("unexpected symbol kind in KaCall: ${this@with::class}")
                }
            )
            append("(")
            (this@with as? KaFunctionSymbol)?.receiverParameter?.let { receiver ->
                append("<extension receiver>: ${receiver.type.render()}")
                if (valueParameters.isNotEmpty()) append(", ")
            }

            @Suppress("DEPRECATION")
            (this@with as? KaCallableSymbol)?.getDispatchReceiverType()?.let { dispatchReceiverType ->
                append("<dispatch receiver>: ${dispatchReceiverType.render()}")
                if (valueParameters.isNotEmpty()) append(", ")
            }
            valueParameters.joinTo(this) { stringRepresentation(it) }
            append(")")
            append(": ${returnType.render()}")
        }
        is KaValueParameterSymbol -> "${if (isVararg) "vararg " else ""}$name: ${returnType.render()}"
        is KaTypeParameterSymbol -> this.nameOrAnonymous.asString()
        is KaVariableSymbol -> "${if (isVal) "val" else "var"} $name: ${returnType.render()}"
        is KaSymbol -> DebugSymbolRenderer().render(analysisSession, this)
        is Boolean -> toString()
        is Map<*, *> -> if (isEmpty()) "{}" else entries.joinToString(
            separator = ",\n  ",
            prefix = "{\n  ",
            postfix = "\n}"
        ) { (k, v) -> "${k?.let { stringRepresentation(it).indented() }} -> (${v?.let { stringRepresentation(it).indented() }})" }
        is Collection<*> -> if (isEmpty()) "[]" else joinToString(
            separator = ",\n  ",
            prefix = "[\n  ",
            postfix = "\n]"
        ) {
            it?.let { stringRepresentation(it).indented() } ?: "null"
        }
        is PsiElement -> this.text
        is KaSubstitutor.Empty -> "<empty substitutor>"
        is KaMapBackedSubstitutor -> {
            val mappingText = getAsMap().entries
                .joinToString(prefix = "{", postfix = "}") { (k, v) -> stringRepresentation(k) + " = " + v.asStringForDebugging() }
            "<map substitutor: $mappingText>"
        }
        is KaChainedSubstitutor -> "${stringRepresentation(first)} then ${stringRepresentation(second)}"
        is KaSubstitutor -> "<complex substitutor>"
        is KaDiagnostic -> "$severity<$factoryName: $defaultMessage>"
        is KaType -> render()
        is Enum<*> -> name
        is Name -> asString()
        is CallableId -> toString()
        is KaCallableSignature<*> -> stringRepresentation(this)
        else -> buildString {
            val clazz = this@with::class
            val className = DebugSymbolRenderer.computeApiEntityName(clazz)
            append(className)
            appendLine(":")
            clazz.memberProperties
                .filter { it.name != "token" && it.visibility == KVisibility.PUBLIC }
                .joinTo(this, separator = "\n  ", prefix = "  ") { property ->
                    val name = property.name

                    @Suppress("UNCHECKED_CAST")
                    val value = (property as KProperty1<Any, *>).get(this@with)?.let {
                        if (className == "KtErrorCallInfo" && name == "candidateCalls") {
                            sortedCalls(it as Collection<KaCall>)
                        } else it
                    }
                    val valueAsString = value?.let { stringRepresentation(it).indented() }
                    "$name = $valueAsString"
                }
        }
    }
}

private fun KaSession.stringRepresentation(signature: KaCallableSignature<*>): String = buildString {
    when (signature) {
        is KaFunctionLikeSignature<*> -> append(DebugSymbolRenderer.computeApiEntityName(KaFunctionLikeSignature::class))
        is KaVariableLikeSignature<*> -> append(DebugSymbolRenderer.computeApiEntityName(KaVariableLikeSignature::class))
    }
    appendLine(":")
    val memberProperties = listOfNotNull(
        KaVariableLikeSignature<*>::name.takeIf { signature is KaVariableLikeSignature<*> },
        KaCallableSignature<*>::receiverType,
        KaCallableSignature<*>::returnType,
        KaCallableSignature<*>::symbol,
        KaFunctionLikeSignature<*>::valueParameters.takeIf { signature is KaFunctionLikeSignature<*> },
        KaCallableSignature<*>::callableIdIfNonLocal
    )
    memberProperties.joinTo(this, separator = "\n  ", prefix = "  ") { property ->
        @Suppress("UNCHECKED_CAST")
        val value = (property as KProperty1<Any, *>).get(signature)
        val valueAsString = value?.let { stringRepresentation(it).indented() }
        "${property.name} = $valueAsString"
    }
}

private fun String.indented() = replace("\n", "\n  ")

internal fun KaSession.prettyPrintSignature(signature: KaCallableSignature<*>): String = prettyPrint {
    when (signature) {
        is KaFunctionLikeSignature -> {
            append("fun ")
            signature.receiverType?.let { append('.'); append(it.render(position = Variance.INVARIANT)) }
            append((signature.symbol as KaNamedSymbol).name.asString())
            printCollection(signature.valueParameters, prefix = "(", postfix = ")") { parameter ->
                append(parameter.name.asString())
                append(": ")
                append(parameter.returnType.render(position = Variance.INVARIANT))
            }
            append(": ")
            append(signature.returnType.render(position = Variance.INVARIANT))
        }
        is KaVariableLikeSignature -> {
            val symbol = signature.symbol
            if (symbol is KaVariableSymbol) {
                append(if (symbol.isVal) "val" else "var")
                append(" ")
            }
            signature.receiverType?.let { append('.'); append(it.render(position = Variance.INVARIANT)) }
            append((symbol as KaNamedSymbol).name.asString())
            append(": ")
            append(signature.returnType.render(position = Variance.INVARIANT))
        }
    }
}

internal fun KaSession.sortedCalls(collection: Collection<KaCall>): Collection<KaCall> = collection.sortedWith { call1, call2 ->
    compareCalls(call1, call2)
}

internal fun KaCall.symbols(): List<KaSymbol> = when (this) {
    is KaCompoundVariableAccessCall -> listOfNotNull(symbol, compoundAccess.operationPartiallyAppliedSymbol.symbol)
    is KaCompoundArrayAccessCall -> listOfNotNull(
        getPartiallyAppliedSymbol.symbol,
        setPartiallyAppliedSymbol.symbol,
        compoundAccess.operationPartiallyAppliedSymbol.symbol,
    )

    is KaCallableMemberCall<*, *> -> listOf(symbol)
}

internal fun KaSession.compareCalls(call1: KaCall, call2: KaCall): Int {
    // The order of candidate calls is non-deterministic. Sort by symbol string value.
    if (call1 !is KaCallableMemberCall<*, *> || call2 !is KaCallableMemberCall<*, *>) return 0
    return stringRepresentation(call1.partiallyAppliedSymbol).compareTo(stringRepresentation(call2.partiallyAppliedSymbol))
}

internal fun KaSession.assertStableSymbolResult(
    testServices: TestServices,
    firstCandidates: List<KaCallCandidateInfo>,
    secondCandidates: List<KaCallCandidateInfo>,
) {
    val assertions = testServices.assertions
    assertions.assertEquals(firstCandidates.size, secondCandidates.size)

    for ((firstCandidate, secondCandidate) in firstCandidates.zip(secondCandidates)) {
        assertions.assertEquals(firstCandidate::class, secondCandidate::class)
        assertStableResult(testServices, firstCandidate.candidate, secondCandidate.candidate)
        assertions.assertEquals(firstCandidate.isInBestCandidates, secondCandidate.isInBestCandidates)

        when (firstCandidate) {
            is KaApplicableCallCandidateInfo -> {}
            is KaInapplicableCallCandidateInfo -> {
                assertStableResult(
                    testServices = testServices,
                    firstDiagnostic = firstCandidate.diagnostic,
                    secondDiagnostic = (secondCandidate as KaInapplicableCallCandidateInfo).diagnostic,
                )
            }
        }
    }
}

internal fun KaSession.assertStableResult(testServices: TestServices, firstInfo: KaCallInfo?, secondInfo: KaCallInfo?) {
    val assertions = testServices.assertions
    if (firstInfo == null || secondInfo == null) {
        assertions.assertEquals(firstInfo, secondInfo)
        return
    }

    assertions.assertEquals(firstInfo::class, secondInfo::class)
    if (firstInfo is KaErrorCallInfo) {
        assertStableResult(
            testServices = testServices,
            firstDiagnostic = firstInfo.diagnostic,
            secondDiagnostic = (secondInfo as KaErrorCallInfo).diagnostic,
        )
    }

    val firstCalls = sortedCalls(firstInfo.calls)
    val secondCalls = sortedCalls(secondInfo.calls)
    assertions.assertEquals(firstCalls.size, secondCalls.size)

    for ((firstCall, secondCall) in firstCalls.zip(secondCalls)) {
        assertStableResult(testServices, firstCall, secondCall)
    }
}

internal fun KaSession.assertStableResult(testServices: TestServices, firstCall: KaCall, secondCall: KaCall) {
    val assertions = testServices.assertions
    assertions.assertEquals(firstCall::class, secondCall::class)

    val symbolsFromFirstCall = firstCall.symbols()
    val symbolsFromSecondCall = secondCall.symbols()
    assertions.assertEquals(symbolsFromFirstCall, symbolsFromSecondCall)
}

internal fun KaSession.assertStableResult(
    testServices: TestServices,
    firstDiagnostic: KaDiagnostic,
    secondDiagnostic: KaDiagnostic,
) {
    val assertions = testServices.assertions
    assertions.assertEquals(firstDiagnostic.defaultMessage, secondDiagnostic.defaultMessage)
    assertions.assertEquals(firstDiagnostic.factoryName, secondDiagnostic.factoryName)
    assertions.assertEquals(firstDiagnostic.severity, secondDiagnostic.severity)
}

internal fun KaSession.renderScopeWithParentDeclarations(scope: KaScope): String = prettyPrint {
    fun KaSymbol.qualifiedNameString() = when (this) {
        is KaConstructorSymbol -> "<constructor> ${containingClassIdIfNonLocal?.asString()}"
        is KaClassLikeSymbol -> classIdIfNonLocal!!.asString()
        is KaCallableSymbol -> callableIdIfNonLocal!!.toString()
        else -> error("unknown symbol $this")
    }

    val renderingOptions = KaDeclarationRendererForSource.WITH_SHORT_NAMES.with {
        modifiersRenderer = modifiersRenderer.with {
            keywordsRenderer = keywordsRenderer.with { keywordFilter = KaRendererKeywordFilter.NONE }
        }
    }

    printCollection(scope.getAllSymbols().toList(), separator = "\n\n") { symbol ->
        val containingDeclaration = symbol.getContainingSymbol() as KaClassLikeSymbol
        append(symbol.render(renderingOptions))
        append(" fromClass ")
        append(containingDeclaration.classIdIfNonLocal?.asString())
        if (symbol.typeParameters.isNotEmpty()) {
            appendLine()
            withIndent {
                printCollection(symbol.typeParameters, separator = "\n") { typeParameter ->
                    val containingDeclarationForTypeParameter = typeParameter.getContainingSymbol()
                    append(typeParameter.render(renderingOptions))
                    append(" from ")
                    append(containingDeclarationForTypeParameter?.qualifiedNameString())
                }
            }
        }

        if (symbol is KaFunctionLikeSymbol && symbol.valueParameters.isNotEmpty()) {
            appendLine()
            withIndent {
                printCollection(symbol.valueParameters, separator = "\n") { typeParameter ->
                    val containingDeclarationForValueParameter = typeParameter.getContainingSymbol()
                    append(typeParameter.render(renderingOptions))
                    append(" from ")
                    append(containingDeclarationForValueParameter?.qualifiedNameString())
                }
            }
        }
    }
}