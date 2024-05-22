/*
 * Copyright 2010-2024 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.analysis.api.components

import org.jetbrains.kotlin.analysis.api.lifetime.KaLifetimeOwner
import org.jetbrains.kotlin.analysis.api.lifetime.KaLifetimeToken
import org.jetbrains.kotlin.analysis.api.lifetime.validityAsserted
import org.jetbrains.kotlin.analysis.api.lifetime.withValidityAssertion
import org.jetbrains.kotlin.analysis.api.scopes.KaScope
import org.jetbrains.kotlin.analysis.api.scopes.KaTypeScope
import org.jetbrains.kotlin.analysis.api.symbols.KaFileSymbol
import org.jetbrains.kotlin.analysis.api.symbols.KaPackageSymbol
import org.jetbrains.kotlin.analysis.api.symbols.KaSymbol
import org.jetbrains.kotlin.analysis.api.symbols.markers.KaSymbolWithMembers
import org.jetbrains.kotlin.analysis.api.types.KaType
import org.jetbrains.kotlin.psi.KtElement
import org.jetbrains.kotlin.psi.KtFile
import java.util.Objects

public abstract class KaScopeProvider : KaSessionComponent() {
    public abstract fun getMemberScope(classSymbol: KaSymbolWithMembers): KaScope

    public abstract fun getStaticMemberScope(symbol: KaSymbolWithMembers): KaScope

    public open fun getCombinedMemberScope(symbol: KaSymbolWithMembers): KaScope = getCompositeScope(
        listOf(
            getMemberScope(symbol),
            getStaticMemberScope(symbol),
        )
    )

    public abstract fun getDeclaredMemberScope(classSymbol: KaSymbolWithMembers): KaScope

    public abstract fun getStaticDeclaredMemberScope(classSymbol: KaSymbolWithMembers): KaScope

    public abstract fun getCombinedDeclaredMemberScope(classSymbol: KaSymbolWithMembers): KaScope

    public abstract fun getDelegatedMemberScope(classSymbol: KaSymbolWithMembers): KaScope

    public abstract fun getEmptyScope(): KaScope

    public abstract fun getFileScope(fileSymbol: KaFileSymbol): KaScope

    public abstract fun getPackageScope(packageSymbol: KaPackageSymbol): KaScope

    public abstract fun getCompositeScope(subScopes: List<KaScope>): KaScope

    public abstract fun getTypeScope(type: KaType): KaTypeScope?

    public abstract fun getSyntheticJavaPropertiesScope(type: KaType): KaTypeScope?

    public abstract fun getImportingScopeContext(file: KtFile): KaScopeContext

    public abstract fun getScopeContextForPosition(
        originalFile: KtFile,
        positionInFakeFile: KtElement
    ): KaScopeContext
}

public typealias KtScopeProvider = KaScopeProvider

public interface KaScopeProviderMixIn : KaSessionMixIn {
    /**
     * Returns a [KaScope] containing *non-static* callable members (functions, properties, and constructors) and all classifier members
     * (classes and objects) of the given [KaSymbolWithMembers]. The scope includes members inherited from the symbol's supertypes, in
     * addition to members which are declared explicitly inside the symbol's body.
     *
     * The member scope doesn't include synthetic Java properties. To get such properties, use [getSyntheticJavaPropertiesScope].
     *
     * @see getStaticMemberScope
     */
    public fun KaSymbolWithMembers.getMemberScope(): KaScope =
        withValidityAssertion { analysisSession.scopeProvider.getMemberScope(this) }

    /**
     * Returns a [KaScope] containing the *static* members of the given [KaSymbolWithMembers].
     *
     * The behavior of the scope differs based on whether the given [KaSymbolWithMembers] is a Kotlin or Java class:
     *
     * - **Kotlin class:** The scope contains static callables (functions and properties) and classifiers (classes and objects) declared
     *   directly in the [KaSymbolWithMembers]. Hence, the static member scope for Kotlin classes is equivalent to [getDeclaredMemberScope].
     * - **Java class:** The scope contains static callables (functions and properties) declared in the [KaSymbolWithMembers] or any of its
     *   superclasses (excluding static callables from super-interfaces), and classes declared directly in the [KaSymbolWithMembers]. This
     *   follows Kotlin's rules about static inheritance in Java classes, where static callables are propagated from superclasses, but
     *   nested classes are not.
     *
     * #### Kotlin Example
     *
     * ```kotlin
     * abstract class A {
     *     class C1
     *     inner class D1
     *     object O1
     *
     *     // There is no way to declare a static callable in an abstract class, as only enum classes define additional static callables.
     * }
     *
     * class B : A() {
     *     class C2
     *     inner class D2
     *     object O2
     *     companion object {
     *         val baz: String = ""
     *     }
     * }
     * ```
     *
     * The static member scope of `B` contains the following symbols:
     *
     * ```
     * class C2
     * inner class D2
     * object O2
     * companion object
     * ```
     *
     * #### Java Example
     *
     * ```java
     * // SuperInterface.java
     * public interface SuperInterface {
     *     public static void fromSuperInterface() { }
     * }
     *
     * // SuperClass.java
     * public abstract class SuperClass implements SuperInterface {
     *     static class NestedSuperClass { }
     *     class InnerSuperClass { }
     *     public static void fromSuperClass() { }
     * }
     *
     * // FILE: JavaClass.java
     * public class JavaClass extends SuperClass {
     *     static class NestedClass { }
     *     class InnerClass { }
     *     public static void fromJavaClass() { }
     * }
     * ```
     *
     * The static member scope of `JavaClass` contains the following symbols:
     *
     * ```
     * public static void fromSuperClass()
     * public static void fromJavaClass()
     * static class NestedClass
     * class InnerClass
     * ```
     *
     * @see getMemberScope
     */
    public fun KaSymbolWithMembers.getStaticMemberScope(): KaScope =
        withValidityAssertion { analysisSession.scopeProvider.getStaticMemberScope(this) }

    /**
     * Returns a [KaScope] containing all members from [getMemberScope] and [getStaticMemberScope].
     */
    public fun KaSymbolWithMembers.getCombinedMemberScope(): KaScope =
        withValidityAssertion { analysisSession.scopeProvider.getCombinedMemberScope(this) }

    /**
     * Returns a [KaScope] containing the *non-static* callables (functions, properties, and constructors) and inner classes explicitly
     * declared in the given [KaSymbolWithMembers].
     *
     * The declared member scope does not contain classifiers (including the companion object) except for inner classes. To retrieve the
     * classifiers declared in this [KaSymbolWithMembers], please use the *static* declared member scope provided by
     * [getStaticDeclaredMemberScope].
     *
     * @see getStaticDeclaredMemberScope
     */
    public fun KaSymbolWithMembers.getDeclaredMemberScope(): KaScope =
        withValidityAssertion { analysisSession.scopeProvider.getDeclaredMemberScope(this) }

    /**
     * Returns a [KaScope] containing the *static* callables (functions and properties) and all classifiers (classes and objects) explicitly
     * declared in the given [KaSymbolWithMembers].
     *
     * It is worth noting that, while Java classes may contain declarations of static callables freely, in Kotlin only enum classes define
     * static callables. Hence, for non-enum Kotlin classes, it is not expected that the static declared member scope will contain any
     * callables.
     *
     * @see getDeclaredMemberScope
     */
    public fun KaSymbolWithMembers.getStaticDeclaredMemberScope(): KaScope =
        withValidityAssertion { analysisSession.scopeProvider.getStaticDeclaredMemberScope(this) }

    /**
     * Returns a [KaScope] containing *all* members explicitly declared in the given [KaSymbolWithMembers].
     *
     * In contrast to [getDeclaredMemberScope] and [getStaticDeclaredMemberScope], this scope contains both static and non-static members.
     */
    public fun KaSymbolWithMembers.getCombinedDeclaredMemberScope(): KaScope =
        withValidityAssertion { analysisSession.scopeProvider.getCombinedDeclaredMemberScope(this) }

    public fun KaSymbolWithMembers.getDelegatedMemberScope(): KaScope =
        withValidityAssertion { analysisSession.scopeProvider.getDelegatedMemberScope(this) }

    public fun KaFileSymbol.getFileScope(): KaScope =
        withValidityAssertion { analysisSession.scopeProvider.getFileScope(this) }

    public fun KaPackageSymbol.getPackageScope(): KaScope =
        withValidityAssertion { analysisSession.scopeProvider.getPackageScope(this) }

    public fun List<KaScope>.asCompositeScope(): KaScope =
        withValidityAssertion { analysisSession.scopeProvider.getCompositeScope(this) }

    /**
     * Return a [KaTypeScope] for a given [KaType].
     * The type scope will include all members which are declared and callable on a given type.
     *
     * Comparing to the [KaScope], in the [KaTypeScope] all use-site type parameters are substituted.
     *
     * Consider the following code
     * ```
     * fun foo(list: List<String>) {
     *      list // get KtTypeScope for it
     * }
     *```
     *
     * Inside the `LIST_KT_ELEMENT.getKaType().getTypeScope()` would contain the `get(i: Int): String` method with substituted type `T = String`
     *
     * @return type scope for the given type if given `KaType` is not error type, `null` otherwise.
     * Returned [KaTypeScope] includes synthetic Java properties.
     *
     * @see KaTypeScope
     * @see KaTypeProviderMixIn.getKaType
     */
    public fun KaType.getTypeScope(): KaTypeScope? =
        withValidityAssertion { analysisSession.scopeProvider.getTypeScope(this) }

    /**
     * Returns a [KaTypeScope] with synthetic Java properties created for a given [KaType].
     */
    public fun KaType.getSyntheticJavaPropertiesScope(): KaTypeScope? =
        withValidityAssertion { analysisSession.scopeProvider.getSyntheticJavaPropertiesScope(this) }

    /**
     * For each scope in [KaScopeContext] an index is calculated. The indexes are relative to position, and they are only known for
     * scopes obtained with [getScopeContextForPosition].
     *
     * Scopes with [KaScopeKind.TypeScope] include synthetic Java properties.
     */
    public fun KtFile.getScopeContextForPosition(positionInFakeFile: KtElement): KaScopeContext =
        withValidityAssertion { analysisSession.scopeProvider.getScopeContextForPosition(this, positionInFakeFile) }

    /**
     * Returns a [KaScopeContext] formed by all imports in the [KtFile].
     *
     * By default, this will also include default importing scopes, which can be filtered by [KaScopeKind]
     */
    public fun KtFile.getImportingScopeContext(): KaScopeContext =
        withValidityAssertion { analysisSession.scopeProvider.getImportingScopeContext(this) }

    /**
     * Returns single scope, containing declarations from all scopes that satisfy [filter]. The order of declarations corresponds to the
     * order of their containing scopes, which are sorted according to their indexes in scope tower.
     */
    public fun KaScopeContext.getCompositeScope(filter: (KaScopeKind) -> Boolean = { true }): KaScope = withValidityAssertion {
        val subScopes = scopes.filter { filter(it.kind) }.map { it.scope }
        subScopes.asCompositeScope()
    }
}

public typealias KtScopeProviderMixIn = KaScopeProviderMixIn

public class KaScopeContext(
    scopes: List<KaScopeWithKind>,
    implicitReceivers: List<KaImplicitReceiver>,
    override val token: KaLifetimeToken
) : KaLifetimeOwner {
    public val implicitReceivers: List<KaImplicitReceiver> by validityAsserted(implicitReceivers)

    /**
     * Scopes for position, sorted according to their indexes in scope tower, i.e. the first scope is the closest one to position.
     */
    public val scopes: List<KaScopeWithKind> by validityAsserted(scopes)
}

public typealias KtScopeContext = KaScopeContext

public class KaImplicitReceiver(
    override val token: KaLifetimeToken,
    type: KaType,
    ownerSymbol: KaSymbol,
    scopeIndexInTower: Int
) : KaLifetimeOwner {
    public val ownerSymbol: KaSymbol by validityAsserted(ownerSymbol)
    public val type: KaType by validityAsserted(type)
    public val scopeIndexInTower: Int by validityAsserted(scopeIndexInTower)
}

public typealias KtImplicitReceiver = KaImplicitReceiver

public sealed class KaScopeKind {
    /**
     * Index in scope tower. For example:
     * ```
     * fun f(a: A, b: B) {      // local scope:       indexInTower = 2
     *     with(a) {            // type scope for A:  indexInTower = 1
     *         with(b) {        // type scope for B:  indexInTower = 0
     *             <caret>
     *         }
     *     }
     * }
     * ```
     */
    public abstract val indexInTower: Int

    public class LocalScope(override val indexInTower: Int) : KaScopeKind()

    /**
     * Represents [KaScope] for type, which include synthetic Java properties of corresponding type.
     */
    public class TypeScope(override val indexInTower: Int) : KaScopeKind()

    public sealed class NonLocalScope : KaScopeKind()

    /**
     * Represents [KaScope] containing type parameters.
     */
    public class TypeParameterScope(override val indexInTower: Int) : NonLocalScope()

    /**
     * Represents [KaScope] containing declarations from package.
     */
    public class PackageMemberScope(override val indexInTower: Int) : NonLocalScope()

    /**
     * Represents [KaScope] containing declarations from imports.
     */
    public sealed class ImportingScope : NonLocalScope()

    /**
     * Represents [KaScope] containing declarations from explicit non-star imports.
     */
    public class ExplicitSimpleImportingScope(override val indexInTower: Int) : ImportingScope()

    /**
     * Represents [KaScope] containing declarations from explicit star imports.
     */
    public class ExplicitStarImportingScope(override val indexInTower: Int) : ImportingScope()

    /**
     * Represents [KaScope] containing declarations from non-star imports which are not declared explicitly and are added by default.
     */
    public class DefaultSimpleImportingScope(override val indexInTower: Int) : ImportingScope()

    /**
     * Represents [KaScope] containing declarations from star imports which are not declared explicitly and are added by default.
     */
    public class DefaultStarImportingScope(override val indexInTower: Int) : ImportingScope()

    /**
     * Represents [KaScope] containing static members of a classifier.
     */
    public class StaticMemberScope(override val indexInTower: Int) : NonLocalScope()

    /**
     * Represents [KaScope] containing members of a script.
     */
    public class ScriptMemberScope(override val indexInTower: Int) : NonLocalScope()
}

public typealias KtScopeKind = KaScopeKind

public class KaScopeWithKind(
    private val backingScope: KaScope,
    private val backingKind: KaScopeKind,
    override val token: KaLifetimeToken,
) : KaLifetimeOwner {
    public val scope: KaScope get() = withValidityAssertion { backingScope }
    public val kind: KaScopeKind get() = withValidityAssertion { backingKind }

    override fun equals(other: Any?): Boolean {
        return this === other ||
                other is KaScopeWithKind &&
                other.backingScope == backingScope &&
                other.backingKind == backingKind
    }

    override fun hashCode(): Int = Objects.hash(backingScope, backingKind)
}

public typealias KtScopeWithKind = KaScopeWithKind