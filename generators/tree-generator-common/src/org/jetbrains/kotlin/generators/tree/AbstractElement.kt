/*
 * Copyright 2010-2023 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.generators.tree

import org.jetbrains.kotlin.generators.tree.imports.ImportCollecting
import org.jetbrains.kotlin.generators.tree.imports.Importable

/**
 * Represents an abstract class/interface for a tree node in the tree generator.
 *
 * Examples: `IrElement`, `FirRegularClass`.
 *
 * Subclasses may contain additional properties/logic specific to a particular tree.
 */
abstract class AbstractElement<Element, Field, Implementation>(
    val name: String,
) : ElementOrRef<Element>, FieldContainer<Field>, ImplementationKindOwner, ImportCollecting
        where Element : AbstractElement<Element, Field, Implementation>,
              Field : AbstractField<Field>,
              Implementation : AbstractImplementation<Implementation, Element, *> {

    /**
     * The fully-qualified name of the property in the tree generator that is used to configure this element.
     */
    abstract val propertyName: String

    abstract val namePrefix: String

    var kDoc: String? = null

    val fields = mutableSetOf<Field>()

    val params = mutableListOf<TypeVariable>()

    private val elementParentsMutable = mutableListOf<ElementRef<Element>>()

    val elementParents: List<ElementRef<Element>>
        get() = elementParentsMutable

    fun addParent(parent: ElementRef<Element>) {
        elementParentsMutable.add(parent)
        parent.element.subElementsMutable.add(element)
    }

    fun replaceParent(oldParent: Element, newParent: ElementRef<Element>) {
        val parentIndex = elementParentsMutable.indexOfFirst { it.element == oldParent }
        require(parentIndex >= 0) {
            "$oldParent is not parent of $this"
        }
        elementParentsMutable[parentIndex] = newParent
        oldParent.subElementsMutable.remove(element)
        newParent.element.subElementsMutable.add(element)
    }

    val otherParents = mutableListOf<ClassRef<*>>()

    val parentRefs: List<ClassOrElementRef>
        get() = elementParents + otherParents

    val isRootElement: Boolean
        get() = elementParents.isEmpty()

    private val subElementsMutable: MutableSet<Element> = mutableSetOf()

    /**
     * A set of [Element]s which are direct subclasses of this element.
     */
    val subElements: Set<Element>
        get() = subElementsMutable

    var isSealed: Boolean = false

    /**
     * The value of this property will be used to name a `visit*` method for this element in visitor classes.
     *
     * In `visit*`, the `*` will be replaced with the value of this property.
     */
    var nameInVisitorMethod: String = name

    /**
     * The name of the method in visitors used to visit this element.
     */
    val visitFunctionName: String
        get() = "visit$nameInVisitorMethod"

    /**
     * The name of the parameter representing this element in the visitor method used to visit this element.
     */
    abstract val visitorParameterName: String

    /**
     * @see parentInVisitor
     */
    var customParentInVisitor: Element? = null

    /**
     * The default element to visit if the method for visiting this element is not overridden.
     */
    open val parentInVisitor: Element?
        get() = customParentInVisitor ?: elementParents.singleOrNull()?.element?.takeIf { !it.isRootElement }

    override val allParents: List<Element>
        get() = elementParents.map { it.element }

    override val typeName: String
        get() = namePrefix + name

    final override fun renderTo(appendable: Appendable, importCollector: ImportCollecting) {
        importCollector.addImport(this)
        appendable.append(typeName)
    }

    override val allFields: List<Field> by lazy {
        val result = LinkedHashSet<Field>()
        result.addAll(fields.toList().asReversed())
        result.forEach { overriddenFieldsHaveSameClass[it, it] = false }
        for (parentField in parentFields.asReversed()) {
            val overrides = !result.add(parentField)
            if (overrides) {
                val existingField = result.first { it == parentField }
                existingField.fromParent = true
                val haveSameClass = parentField.typeRef.copy(nullable = false) == existingField.typeRef.copy(nullable = false)
                if (!haveSameClass) {
                    existingField.overriddenTypes += parentField.typeRef
                }
                overriddenFieldsHaveSameClass[existingField, parentField] = haveSameClass
                existingField.updatePropertiesFromOverriddenField(parentField, haveSameClass)
            } else {
                overriddenFieldsHaveSameClass[parentField, parentField] = true
            }
        }
        result.toList().asReversed()
    }

    val overriddenFieldsHaveSameClass: MutableMap<Field, MutableMap<Field, Boolean>> = mutableMapOf()

    val parentFields: List<Field> by lazy {
        val result = LinkedHashMap<String, Field>()
        elementParents.forEach { parentRef ->
            val parent = parentRef.element
            val fields = parent.allFields.map { field ->
                field.replaceType(field.typeRef.substitute(parentRef.args) as TypeRefWithNullability)
                    .apply {
                        fromParent = true
                    }
            }
            fields.forEach {
                result.merge(it.name, it) { previousField, thisField ->
                    val resultField = previousField.copy()
                    if (thisField.isMutable) {
                        resultField.isMutable = true
                    }
                    resultField
                }
            }
        }
        result.values.toList()
    }

    /**
     * A custom return type of the corresponding transformer method for this element.
     */
    var transformerReturnType: Element? = null

    /**
     * @see org.jetbrains.kotlin.generators.tree.detectBaseTransformerTypes
     */
    internal var baseTransformerType: Element? = null

    /**
     * The return type of the corresponding transformer method for this element.
     *
     * By default, computed using [org.jetbrains.kotlin.generators.tree.detectBaseTransformerTypes], but can be customized via
     * [transformerReturnType]
     */
    val transformerClass: Element
        get() = transformerReturnType ?: baseTransformerType ?: element

    val implementations = mutableListOf<Implementation>()

    var doesNotNeedImplementation: Boolean = false

    /**
     * Types/functions that you want to additionally import in the file with the element class.
     *
     * This is useful if, for example, default values of fields reference classes or functions from other packages.
     *
     * Note that classes referenced in field types will be imported automatically.
     */
    val additionalImports = mutableListOf<Importable>()

    override fun addImport(importable: Importable) {
        additionalImports.add(importable)
    }

    override var kind: ImplementationKind? = null

    @Suppress("UNCHECKED_CAST")
    final override val element: Element
        get() = this as Element

    final override val args: Map<NamedTypeParameterRef, TypeRef>
        get() = emptyMap()

    final override val nullable: Boolean
        get() = false

    var doPrint = true

    final override fun copy(nullable: Boolean) = ElementRef(element, args, nullable)

    final override fun copy(args: Map<NamedTypeParameterRef, TypeRef>) = ElementRef(element, args, nullable)

    @Suppress("UNCHECKED_CAST")
    override fun substitute(map: TypeParameterSubstitutionMap): Element = this as Element

    fun withStarArgs(): ElementRef<Element> = copy(params.associateWith { TypeRef.Star })

    fun withSelfArgs(): ElementRef<Element> = copy(params.associateWith { it })

    operator fun TypeVariable.unaryPlus() = apply {
        params.add(this)
    }

    operator fun Field.unaryPlus() = apply {
        fields.add(this)
    }

    override fun toString(): String = buildString { renderTo(this, ImportCollecting.Empty) }
}
