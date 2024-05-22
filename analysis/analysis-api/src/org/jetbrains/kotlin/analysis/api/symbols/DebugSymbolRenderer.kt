/*
 * Copyright 2010-2023 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.analysis.api.symbols

import com.intellij.psi.PsiElement
import org.jetbrains.kotlin.analysis.api.*
import org.jetbrains.kotlin.analysis.api.annotations.*
import org.jetbrains.kotlin.analysis.api.base.KaContextReceiver
import org.jetbrains.kotlin.analysis.api.components.KaSymbolContainingDeclarationProviderMixIn
import org.jetbrains.kotlin.analysis.api.components.KaSymbolInfoProviderMixIn
import org.jetbrains.kotlin.analysis.api.contracts.description.Context
import org.jetbrains.kotlin.analysis.api.contracts.description.KaContractEffectDeclaration
import org.jetbrains.kotlin.analysis.api.contracts.description.renderKaContractEffectDeclaration
import org.jetbrains.kotlin.analysis.api.symbols.markers.KaNamedSymbol
import org.jetbrains.kotlin.analysis.api.symbols.markers.KaPossiblyNamedSymbol
import org.jetbrains.kotlin.analysis.api.types.KaClassTypeQualifier
import org.jetbrains.kotlin.analysis.api.types.KaErrorType
import org.jetbrains.kotlin.analysis.api.types.KaNonErrorClassType
import org.jetbrains.kotlin.analysis.api.types.KaType
import org.jetbrains.kotlin.analysis.project.structure.KtModule
import org.jetbrains.kotlin.analysis.utils.printer.PrettyPrinter
import org.jetbrains.kotlin.analysis.utils.printer.prettyPrint
import org.jetbrains.kotlin.descriptors.Visibility
import org.jetbrains.kotlin.name.ClassId
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.name.Name
import org.jetbrains.kotlin.name.SpecialNames
import org.jetbrains.kotlin.psi.KtFile
import org.jetbrains.kotlin.renderer.render
import org.jetbrains.kotlin.resolve.deprecation.DeprecationInfo
import org.jetbrains.kotlin.types.Variance
import java.lang.reflect.InvocationTargetException
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KProperty
import kotlin.reflect.KVisibility
import kotlin.reflect.full.*
import kotlin.reflect.jvm.isAccessible

public class DebugSymbolRenderer(
    public val renderExtra: Boolean = false,
    public val renderTypeByProperties: Boolean = false,
    public val renderExpandedTypes: Boolean = false,
) {

    public fun render(analysisSession: KaSession, symbol: KaSymbol): String {
        return prettyPrint { analysisSession.renderSymbol(symbol, this@prettyPrint) }
    }

    public fun renderAnnotationApplication(analysisSession: KaSession, application: KaAnnotationApplication): String {
        return prettyPrint { analysisSession.renderAnnotationApplication(application, this@prettyPrint) }
    }

    public fun renderType(analysisSession: KaSession, type: KaType): String {
        return prettyPrint { analysisSession.renderType(type, this@prettyPrint) }
    }

    private fun KaSession.renderSymbol(symbol: KaSymbol, printer: PrettyPrinter) {
        renderSymbolInternals(symbol, printer)

        if (!renderExtra) return
        printer.withIndent {
            @Suppress("DEPRECATION")
            (symbol as? KaCallableSymbol)?.getDispatchReceiverType()?.let { dispatchType ->
                appendLine().append("getDispatchReceiver()").append(": ")
                renderType(dispatchType, printer)
            }

            KaSymbolContainingDeclarationProviderMixIn::class
                .declaredMemberExtensionFunctions
                .filterNot {
                    // Rendering a containing symbol is prone to stack overflow.
                    // * function symbol will render its value parameter symbol(s)
                    //   whose containing symbol is that function symbol.
                    // * property symbol contains accessor symbol(s) and/or backing field symbol
                    //   whose containing symbol is that property symbol.
                    it.name == "getContainingSymbol"
                }
                .forEach {
                    if (it.name == "getContainingJvmClassName") {
                        if (symbol is KaCallableSymbol) {
                            appendLine()
                            renderFunction(it, printer, renderSymbolsFully = false, analysisSession, symbol)
                        }
                    } else {
                        appendLine()
                        renderFunction(it, printer, renderSymbolsFully = false, analysisSession, symbol)
                    }
                }

            KaSymbolInfoProviderMixIn::class.declaredMemberExtensionProperties
                .asSequence()
                .filter { (it.extensionReceiverParameter?.type?.classifier as? KClass<*>)?.isInstance(symbol) == true }
                .forEach {
                    appendLine()
                    renderProperty(it, printer, renderSymbolsFully = false, analysisSession, symbol)
                }
        }
    }

    public fun KaSession.renderForSubstitutionOverrideUnwrappingTest(symbol: KaSymbol): String = prettyPrint {
        if (symbol !is KaCallableSymbol) return@prettyPrint

        renderFrontendIndependentKClassNameOf(symbol, printer)

        withIndent {
            appendLine()
            renderProperty(KaCallableSymbol::callableIdIfNonLocal, printer, renderSymbolsFully = false, symbol)
            if (symbol is KaNamedSymbol) {
                appendLine()
                renderProperty(KaNamedSymbol::name, printer, renderSymbolsFully = false, symbol)
            }
            appendLine()
            renderProperty(KaCallableSymbol::origin, printer, renderSymbolsFully = false, symbol)
        }
    }

    private fun KaSession.renderFunction(
        function: KFunction<*>,
        printer: PrettyPrinter,
        renderSymbolsFully: Boolean,
        vararg args: Any,
    ) {
        printer.append(function.name).append(": ")
        renderFunctionCall(function, printer, renderSymbolsFully, args)
    }

    private fun KaSession.renderProperty(
        property: KProperty<*>,
        printer: PrettyPrinter,
        renderSymbolsFully: Boolean,
        vararg args: Any,
    ) {
        printer.append(property.name).append(": ")
        renderFunctionCall(property.getter, printer, renderSymbolsFully, args)
    }

    private fun KaSession.renderFunctionCall(
        function: KFunction<*>,
        printer: PrettyPrinter,
        renderSymbolsFully: Boolean,
        args: Array<out Any>,
    ) {
        try {
            function.isAccessible = true
            renderValue(function.call(*args), printer, renderSymbolsFully)
        } catch (e: InvocationTargetException) {
            printer.append("Could not render due to ").appendLine(e.cause.toString())
        }
    }

    private fun KaSession.renderSymbolInternals(symbol: KaSymbol, printer: PrettyPrinter) {
        renderFrontendIndependentKClassNameOf(symbol, printer)
        val apiClass = getFrontendIndependentKClassOf(symbol)
        printer.withIndent {
            val members = apiClass.members
                .filterIsInstance<KProperty<*>>()
                .filter { it.name !in ignoredPropertyNames }
                .sortedBy { it.name }
            appendLine()
            printCollectionIfNotEmpty(members, separator = "\n") { member ->
                val renderSymbolsFully = member.name == KaValueParameterSymbol::generatedPrimaryConstructorProperty.name
                renderProperty(member, printer, renderSymbolsFully, symbol)
            }
        }
    }

    private fun renderFrontendIndependentKClassNameOf(instanceOfClassToRender: Any, printer: PrettyPrinter) {
        val apiClass = getFrontendIndependentKClassOf(instanceOfClassToRender)
        printer.append(computeApiEntityName(apiClass)).append(':')
    }

    private fun KaSession.renderList(values: List<*>, printer: PrettyPrinter, renderSymbolsFully: Boolean) {
        if (values.isEmpty()) {
            printer.append("[]")
            return
        }

        printer.withIndentInSquareBrackets {
            printCollection(values, separator = "\n") { renderValue(it, printer, renderSymbolsFully) }
        }
    }

    private fun KaSession.renderSymbolTag(symbol: KaSymbol, printer: PrettyPrinter, renderSymbolsFully: Boolean) {
        fun renderId(id: Any?, symbol: KaSymbol) {
            if (id != null) {
                renderValue(id, printer, renderSymbolsFully)
            } else {
                val outerName = (symbol as? KaPossiblyNamedSymbol)?.name ?: SpecialNames.NO_NAME_PROVIDED
                printer.append("<local>/" + outerName.asString())
            }
        }

        if (renderSymbolsFully || symbol is KaBackingFieldSymbol ||
            symbol is KaPropertyGetterSymbol || symbol is KaPropertySetterSymbol ||
            symbol is KaValueParameterSymbol || symbol is KaReceiverParameterSymbol
        ) {
            renderSymbol(symbol, printer)
            return
        }

        with(printer) {
            append(computeApiEntityName(getFrontendIndependentKClassOf(symbol)))
            append("(")
            when (symbol) {
                is KaClassLikeSymbol -> renderId(symbol.classIdIfNonLocal, symbol)
                is KaCallableSymbol -> renderId(symbol.callableIdIfNonLocal, symbol)
                is KaNamedSymbol -> renderValue(symbol.name, printer, renderSymbolsFully = false)
                is KaFileSymbol -> renderValue((symbol.psi as KtFile).name, printer, renderSymbolsFully = false)
                else -> error("Unsupported symbol ${symbol::class}")
            }
            append(")")
        }
    }

    private fun renderAnnotationValue(value: KaAnnotationValue, printer: PrettyPrinter) {
        printer.append(KaAnnotationValueRenderer.render(value))
    }

    private fun KaSession.renderNamedConstantValue(value: KaNamedAnnotationValue, printer: PrettyPrinter) {
        printer.append(value.name.render()).append(" = ")
        renderValue(value.expression, printer, renderSymbolsFully = false)
    }

    private fun KaSession.renderType(type: KaType, printer: PrettyPrinter) {
        val typeToRender = if (renderExpandedTypes) type.fullyExpandedType else type

        renderFrontendIndependentKClassNameOf(typeToRender, printer)
        printer.withIndent {
            appendLine()
            if (renderTypeByProperties) {
                renderByPropertyNames(typeToRender, printer)
            } else {
                append("annotationsList: ")
                renderAnnotationsList(typeToRender.annotationsList, printer)

                if (typeToRender is KaNonErrorClassType) {
                    appendLine()
                    append("ownTypeArguments: ")
                    renderList(typeToRender.ownTypeArguments, printer, renderSymbolsFully = false)
                }

                appendLine()
                append("type: ")
                when (typeToRender) {
                    is KaErrorType -> append("ERROR_TYPE")
                    else -> append(typeToRender.asStringForDebugging())
                }
            }
        }
    }

    private fun KaSession.renderByPropertyNames(value: Any, printer: PrettyPrinter) {
        val members = value::class.members
            .filter { it.name !in ignoredPropertyNames }
            .filter { it.visibility != KVisibility.PRIVATE && it.visibility != KVisibility.INTERNAL }
            .sortedBy { it.name }
            .filterIsInstance<KProperty<*>>()

        printer.printCollectionIfNotEmpty(members, separator = "\n") { member ->
            renderProperty(member, printer, renderSymbolsFully = false, value)
        }
    }

    private fun KaSession.renderAnnotationApplication(call: KaAnnotationApplication, printer: PrettyPrinter) {
        with(printer) {
            renderValue(call.classId, printer, renderSymbolsFully = false)
            append('(')
            if (call is KaAnnotationApplicationWithArgumentsInfo) {
                call.arguments.sortedBy { it.name }.forEachIndexed { index, value ->
                    if (index > 0) {
                        append(", ")
                    }
                    renderValue(value, printer, renderSymbolsFully = false)
                }
            } else {
                append("isCallWithArguments=${call.isCallWithArguments}")
            }
            append(')')

            withIndent {
                appendLine().append("psi: ")
                val psi =
                    if (call.psi?.containingKtFile?.isCompiled == true) {
                        null
                    } else call.psi
                renderValue(psi?.javaClass?.simpleName, printer, renderSymbolsFully = false)
            }
        }
    }

    private fun renderDeprecationInfo(info: DeprecationInfo, printer: PrettyPrinter) {
        with(printer) {
            append("DeprecationInfo(")
            append("deprecationLevel=${info.deprecationLevel}, ")
            append("propagatesToOverrides=${info.propagatesToOverrides}, ")
            append("message=${info.message}")
            append(")")
        }
    }

    private fun KaSession.renderValue(value: Any?, printer: PrettyPrinter, renderSymbolsFully: Boolean) {
        when (value) {
            // Symbol-related values
            is KaSymbol -> renderSymbolTag(value, printer, renderSymbolsFully)
            is KaType -> renderType(value, printer)
            is KaTypeProjection -> renderTypeProjection(value, printer)
            is KaClassTypeQualifier -> renderTypeQualifier(value, printer)
            is KaAnnotationValue -> renderAnnotationValue(value, printer)
            is KaContractEffectDeclaration -> Context(this@KaSession, printer, this@DebugSymbolRenderer)
                .renderKaContractEffectDeclaration(value, endWithNewLine = false)
            is KaNamedAnnotationValue -> renderNamedConstantValue(value, printer)
            is KaInitializerValue -> renderKtInitializerValue(value, printer)
            is KaContextReceiver -> renderContextReceiver(value, printer)
            is KaAnnotationApplication -> renderAnnotationApplication(value, printer)
            is KaAnnotationsList -> renderAnnotationsList(value, printer)
            is KtModule -> renderKtModule(value, printer)
            // Other custom values
            is Name -> printer.append(value.asString())
            is FqName -> printer.append(value.asString())
            is ClassId -> printer.append(value.asString())
            is DeprecationInfo -> renderDeprecationInfo(value, printer)
            is Visibility -> printer.append(value::class.java.simpleName)
            // Unsigned integers
            is UByte -> printer.append(value.toString())
            is UShort -> printer.append(value.toString())
            is UInt -> printer.append(value.toString())
            is ULong -> printer.append(value.toString())
            // Java values
            is Enum<*> -> printer.append(value.name)
            is List<*> -> renderList(value, printer, renderSymbolsFully = false)
            else -> printer.append(value.toString())
        }
    }

    private fun KaSession.renderTypeProjection(value: KaTypeProjection, printer: PrettyPrinter) {
        when (value) {
            is KaStarTypeProjection -> printer.append("*")
            is KaTypeArgumentWithVariance -> {
                if (value.variance != Variance.INVARIANT) {
                    printer.append("${value.variance.label} ")
                }
                renderType(value.type, printer)
            }
        }
    }

    private fun KaSession.renderTypeQualifier(value: KaClassTypeQualifier, printer: PrettyPrinter) {
        with(printer) {
            appendLine("qualifier:")
            withIndent {
                renderByPropertyNames(value, printer)
            }
        }
    }

    private fun KaSession.renderContextReceiver(receiver: KaContextReceiver, printer: PrettyPrinter) {
        with(printer) {
            append("KtContextReceiver:")
            withIndent {
                appendLine()
                append("label: ")
                renderValue(receiver.label, printer, renderSymbolsFully = false)
                appendLine()
                append("type: ")
                renderType(receiver.type, printer)
            }
        }
    }

    private fun renderKtModule(ktModule: KtModule, printer: PrettyPrinter) {
        val ktModuleClass = ktModule::class.allSuperclasses.first { it in ktModuleSubclasses }
        printer.append(computeApiEntityName(ktModuleClass) + " \"" + ktModule.moduleDescription + "\"")
    }

    private fun KClass<*>.allSealedSubClasses(): List<KClass<*>> = buildList {
        add(this@allSealedSubClasses)
        sealedSubclasses.flatMapTo(this) { it.allSealedSubClasses() }
    }

    private val ktModuleSubclasses = KtModule::class.allSealedSubClasses().distinct().sortedWith { a, b ->
        when {
            a == b -> 0
            a.isSubclassOf(b) -> -1
            b.isSubclassOf(a) -> 1
            else -> 0
        }
    }

    private fun renderKtInitializerValue(value: KaInitializerValue, printer: PrettyPrinter) {
        with(printer) {
            when (value) {
                is KaConstantInitializerValue -> {
                    append("KtConstantInitializerValue(")
                    append(value.constant.renderAsKotlinConstant())
                    append(")")
                }

                is KaNonConstantInitializerValue -> {
                    append("KtNonConstantInitializerValue(")
                    append(value.initializerPsi?.firstLineOfPsi() ?: "NO_PSI")
                    append(")")
                }

                is KaConstantValueForAnnotation -> {
                    append("KtConstantValueForAnnotation(")
                    append(value.annotationValue.renderAsSourceCode())
                    append(")")
                }
            }
        }
    }

    private fun KaSession.renderAnnotationsList(value: KaAnnotationsList, printer: PrettyPrinter) {
        renderList(value.annotations, printer, renderSymbolsFully = false)
    }

    private fun getFrontendIndependentKClassOf(instanceOfClass: Any): KClass<*> {
        var current: Class<*> = instanceOfClass.javaClass

        while (true) {
            val className = current.name
            if (symbolImplementationPackageNames.none { className.startsWith("$it.") }) {
                return current.kotlin
            }
            current = current.superclass
        }
    }

    private fun PsiElement.firstLineOfPsi(): String {
        val text = text
        val lines = text.lines()
        return if (lines.count() <= 1) text
        else lines.first() + " ..."
    }

    public companion object {
        public fun computeApiEntityName(klass: KClass<*>): String {
            val simpleName = klass.simpleName ?: return "null"

            if (simpleName.startsWith("Ka")) {
                // A temporary replacement to ease the 'Kt' -> 'Ka' prefix migration
                return "Kt" + simpleName.drop(2)
            }

            return simpleName
        }

        private val ignoredPropertyNames = setOf(
            "psi",
            "token",
            "builder",
            "coneType",
            "analysisContext",
            "fe10Type"
        )

        private val symbolImplementationPackageNames = listOf(
            "org.jetbrains.kotlin.analysis.api.fir",
            "org.jetbrains.kotlin.analysis.api.descriptors",
        )
    }
}

private val PrettyPrinter.printer: PrettyPrinter
    get() = this