/*
 * Copyright 2010-2024 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.analysis.api.fir.components

import org.jetbrains.kotlin.analysis.api.components.*
import org.jetbrains.kotlin.analysis.api.fir.KaFirSession
import org.jetbrains.kotlin.analysis.api.fir.KaSymbolByFirBuilder
import org.jetbrains.kotlin.analysis.api.fir.scopes.*
import org.jetbrains.kotlin.analysis.api.fir.symbols.*
import org.jetbrains.kotlin.analysis.api.fir.types.KaFirType
import org.jetbrains.kotlin.analysis.api.fir.utils.firSymbol
import org.jetbrains.kotlin.analysis.api.impl.base.scopes.KaCompositeScope
import org.jetbrains.kotlin.analysis.api.impl.base.scopes.KaEmptyScope
import org.jetbrains.kotlin.analysis.api.scopes.KaScope
import org.jetbrains.kotlin.analysis.api.scopes.KaTypeScope
import org.jetbrains.kotlin.analysis.api.symbols.KaFileSymbol
import org.jetbrains.kotlin.analysis.api.symbols.KaPackageSymbol
import org.jetbrains.kotlin.analysis.api.symbols.markers.KaSymbolWithDeclarations
import org.jetbrains.kotlin.analysis.api.symbols.markers.KaSymbolWithMembers
import org.jetbrains.kotlin.analysis.api.types.KaType
import org.jetbrains.kotlin.analysis.low.level.api.fir.api.LLFirResolveSession
import org.jetbrains.kotlin.analysis.low.level.api.fir.api.getOrBuildFirFile
import org.jetbrains.kotlin.analysis.low.level.api.fir.util.ContextCollector
import org.jetbrains.kotlin.analysis.utils.errors.unexpectedElementError
import org.jetbrains.kotlin.analysis.utils.printer.parentOfType
import org.jetbrains.kotlin.fir.declarations.FirClass
import org.jetbrains.kotlin.fir.declarations.FirResolvePhase
import org.jetbrains.kotlin.fir.declarations.utils.delegateFields
import org.jetbrains.kotlin.fir.java.JavaScopeProvider
import org.jetbrains.kotlin.fir.java.declarations.FirJavaClass
import org.jetbrains.kotlin.fir.resolve.ScopeSession
import org.jetbrains.kotlin.fir.resolve.SessionHolderImpl
import org.jetbrains.kotlin.fir.resolve.calls.FirSyntheticPropertiesScope
import org.jetbrains.kotlin.fir.resolve.scope
import org.jetbrains.kotlin.fir.resolve.scopeSessionKey
import org.jetbrains.kotlin.fir.scopes.*
import org.jetbrains.kotlin.fir.scopes.impl.*
import org.jetbrains.kotlin.fir.symbols.impl.*
import org.jetbrains.kotlin.fir.symbols.lazyResolveToPhaseWithCallableMembers
import org.jetbrains.kotlin.fir.types.ConeKotlinType
import org.jetbrains.kotlin.kdoc.psi.api.KDoc
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.name.Name
import org.jetbrains.kotlin.psi.KtElement
import org.jetbrains.kotlin.psi.KtFile
import org.jetbrains.kotlin.utils.exceptions.errorWithAttachment
import org.jetbrains.kotlin.utils.exceptions.withPsiEntry

internal class KaFirScopeProvider(
    override val analysisSession: KaFirSession,
    private val builder: KaSymbolByFirBuilder,
    private val firResolveSession: LLFirResolveSession,
) : KaScopeProvider() {

    private fun getScopeSession(): ScopeSession {
        return analysisSession.getScopeSessionFor(analysisSession.useSiteSession)
    }

    private fun KaSymbolWithMembers.getFirForScope(): FirClass = when (this) {
        is KaFirNamedClassOrObjectSymbol -> firSymbol.fir
        is KaFirPsiJavaClassSymbol -> firSymbol.fir
        is KaFirAnonymousObjectSymbol -> firSymbol.fir
        else -> error(
            "`${this::class.qualifiedName}` needs to be specially handled by the scope provider or is an unknown" +
                    " ${KaSymbolWithDeclarations::class.simpleName} implementation."
        )
    }

    override fun getMemberScope(classSymbol: KaSymbolWithMembers): KaScope {
        val firScope = classSymbol.getFirForScope().unsubstitutedScope(
            analysisSession.useSiteSession,
            getScopeSession(),
            withForcedTypeCalculator = false,
            memberRequiredPhase = FirResolvePhase.STATUS,
        )
        return KaFirDelegatingNamesAwareScope(firScope, builder)
    }

    override fun getStaticMemberScope(symbol: KaSymbolWithMembers): KaScope {
        val fir = symbol.getFirForScope()
        val firScope = fir.scopeProvider.getStaticScope(fir, analysisSession.useSiteSession, getScopeSession()) ?: return getEmptyScope()
        return KaFirDelegatingNamesAwareScope(firScope, builder)
    }

    override fun getDeclaredMemberScope(classSymbol: KaSymbolWithMembers): KaScope =
        getDeclaredMemberScope(classSymbol, DeclaredMemberScopeKind.NON_STATIC)

    override fun getStaticDeclaredMemberScope(classSymbol: KaSymbolWithMembers): KaScope =
        getDeclaredMemberScope(classSymbol, DeclaredMemberScopeKind.STATIC)

    override fun getCombinedDeclaredMemberScope(classSymbol: KaSymbolWithMembers): KaScope =
        getDeclaredMemberScope(classSymbol, DeclaredMemberScopeKind.COMBINED)

    private enum class DeclaredMemberScopeKind {
        NON_STATIC,

        STATIC,

        /**
         * A scope containing both non-static and static members. A smart combined scope (as opposed to a naive combination of [KaScope]s
         * with [getCompositeScope]) avoids duplicate inner classes, as they are contained in non-static and static scopes.
         *
         * A proper combined declared member scope kind also makes it easier to cache combined scopes directly (if needed).
         */
        COMBINED,
    }

    private fun getDeclaredMemberScope(classSymbol: KaSymbolWithMembers, kind: DeclaredMemberScopeKind): KaScope {
        val firDeclaration = classSymbol.firSymbol.fir
        val firScope = when (firDeclaration) {
            is FirJavaClass -> getFirJavaDeclaredMemberScope(firDeclaration, kind) ?: return getEmptyScope()
            else -> getFirKotlinDeclaredMemberScope(classSymbol, kind)
        }

        return KaFirDelegatingNamesAwareScope(firScope, builder)
    }

    private fun getFirKotlinDeclaredMemberScope(
        classSymbol: KaSymbolWithMembers,
        kind: DeclaredMemberScopeKind,
    ): FirContainingNamesAwareScope {
        val combinedScope = getCombinedFirKotlinDeclaredMemberScope(classSymbol)
        return when (kind) {
            DeclaredMemberScopeKind.NON_STATIC -> FirNonStaticMembersScope(combinedScope)
            DeclaredMemberScopeKind.STATIC -> FirStaticScope(combinedScope)
            DeclaredMemberScopeKind.COMBINED -> combinedScope
        }
    }

    /**
     * Returns a declared member scope which contains both static and non-static callables, as well as all classifiers. Java classes need to
     * be handled specially, because [declaredMemberScope] doesn't handle Java enhancement properly.
     */
    private fun getCombinedFirKotlinDeclaredMemberScope(symbolWithMembers: KaSymbolWithMembers): FirContainingNamesAwareScope {
        val useSiteSession = analysisSession.useSiteSession
        return when (symbolWithMembers) {
            is KaFirScriptSymbol -> FirScriptDeclarationsScope(useSiteSession, symbolWithMembers.firSymbol.fir)
            else -> useSiteSession.declaredMemberScope(symbolWithMembers.getFirForScope(), memberRequiredPhase = null)
        }
    }

    private fun getFirJavaDeclaredMemberScope(
        firJavaClass: FirJavaClass,
        kind: DeclaredMemberScopeKind,
    ): FirContainingNamesAwareScope? {
        val useSiteSession = analysisSession.useSiteSession
        val scopeSession = getScopeSession()

        fun getBaseUseSiteScope() = JavaScopeProvider.getUseSiteMemberScope(
            firJavaClass,
            useSiteSession,
            scopeSession,
            memberRequiredPhase = FirResolvePhase.TYPES,
        )

        fun getStaticScope() = JavaScopeProvider.getStaticScope(firJavaClass, useSiteSession, scopeSession)

        val firScope = when (kind) {
            // `FirExcludingNonInnerClassesScope` is a workaround for non-static member scopes containing static classes (see KT-61900).
            DeclaredMemberScopeKind.NON_STATIC -> FirExcludingNonInnerClassesScope(getBaseUseSiteScope())

            DeclaredMemberScopeKind.STATIC -> getStaticScope() ?: return null

            // Java enhancement scopes as provided by `JavaScopeProvider` are either use-site or static scopes, so we need to compose them
            // to get the combined scope. A base declared member scope with Java enhancement doesn't exist, unfortunately.
            DeclaredMemberScopeKind.COMBINED -> {
                // The static scope contains inner classes, so we need to exclude them from the non-static scope to avoid duplicates.
                val nonStaticScope = FirNoClassifiersScope(getBaseUseSiteScope())
                getStaticScope()
                    ?.let { staticScope -> FirNameAwareCompositeScope(listOf(nonStaticScope, staticScope)) }
                    ?: nonStaticScope
            }
        }

        val cacheKey = when (kind) {
            DeclaredMemberScopeKind.NON_STATIC -> JAVA_ENHANCEMENT_FOR_DECLARED_MEMBERS
            DeclaredMemberScopeKind.STATIC -> JAVA_ENHANCEMENT_FOR_STATIC_DECLARED_MEMBERS
            DeclaredMemberScopeKind.COMBINED -> JAVA_ENHANCEMENT_FOR_ALL_DECLARED_MEMBERS
        }

        return scopeSession.getOrBuild(firJavaClass.symbol, cacheKey) {
            FirJavaDeclaredMembersOnlyScope(firScope, firJavaClass)
        }
    }

    override fun getDelegatedMemberScope(classSymbol: KaSymbolWithMembers): KaScope {
        val declaredScope = (getDeclaredMemberScope(classSymbol) as? KaFirDelegatingNamesAwareScope)?.firScope ?: return getEmptyScope()

        val fir = classSymbol.getFirForScope()
        val delegateFields = fir.delegateFields

        if (delegateFields.isEmpty()) {
            return getEmptyScope()
        }

        fir.lazyResolveToPhaseWithCallableMembers(FirResolvePhase.STATUS)

        val firScope = FirDelegatedMemberScope(
            analysisSession.useSiteSession,
            getScopeSession(),
            fir,
            declaredScope,
            delegateFields
        )
        return KaFirDelegatedMemberScope(firScope, builder)
    }

    override fun getFileScope(fileSymbol: KaFileSymbol): KaScope {
        check(fileSymbol is KaFirFileSymbol) { "KtFirScopeProvider can only work with KtFirFileSymbol, but ${fileSymbol::class} was provided" }
        return KaFirFileScope(fileSymbol, builder)
    }

    override fun getEmptyScope(): KaScope {
        return KaEmptyScope(token)
    }

    override fun getPackageScope(packageSymbol: KaPackageSymbol): KaScope {
        return createPackageScope(packageSymbol.fqName)
    }

    override fun getCompositeScope(subScopes: List<KaScope>): KaScope {
        return KaCompositeScope.create(subScopes, token)
    }

    override fun getTypeScope(type: KaType): KaTypeScope? {
        check(type is KaFirType) { "KtFirScopeProvider can only work with KtFirType, but ${type::class} was provided" }
        return getFirTypeScope(type)
            ?.withSyntheticPropertiesScopeOrSelf(type.coneType)
            ?.let { convertToKtTypeScope(it) }
    }

    override fun getSyntheticJavaPropertiesScope(type: KaType): KaTypeScope? {
        check(type is KaFirType) { "KtFirScopeProvider can only work with KtFirType, but ${type::class} was provided" }
        val typeScope = getFirTypeScope(type) ?: return null
        return getFirSyntheticPropertiesScope(type.coneType, typeScope)?.let { convertToKtTypeScope(it) }
    }

    override fun getImportingScopeContext(file: KtFile): KaScopeContext {
        val firFile = file.getOrBuildFirFile(firResolveSession)
        val firFileSession = firFile.moduleData.session
        val firImportingScopes = createImportingScopes(
            firFile,
            firFileSession,
            analysisSession.getScopeSessionFor(firFileSession),
            useCaching = true,
        )

        val ktScopesWithKinds = createScopesWithKind(firImportingScopes.withIndex())
        return KaScopeContext(ktScopesWithKinds, implicitReceivers = emptyList(), token)
    }

    override fun getScopeContextForPosition(
        originalFile: KtFile,
        positionInFakeFile: KtElement
    ): KaScopeContext {
        val fakeFile = positionInFakeFile.containingKtFile

        // If the position is in KDoc, we want to pass the owning declaration to the ContextCollector.
        // That way, the resulting scope will contain all the nested declarations which can be references by KDoc.
        val parentKDoc = positionInFakeFile.parentOfType<KDoc>()
        val correctedPosition = parentKDoc?.owner ?: positionInFakeFile

        val context = ContextCollector.process(
            fakeFile.getOrBuildFirFile(firResolveSession),
            SessionHolderImpl(analysisSession.useSiteSession, getScopeSession()),
            correctedPosition,
        )

        val towerDataContext =
            context?.towerDataContext
                ?: errorWithAttachment("Cannot find context for ${positionInFakeFile::class}") {
                    withPsiEntry("positionInFakeFile", positionInFakeFile)
                }
        val towerDataElementsIndexed = towerDataContext.towerDataElements.asReversed().withIndex()

        val implicitReceivers = towerDataElementsIndexed.flatMap { (index, towerDataElement) ->
            val receivers = listOfNotNull(towerDataElement.implicitReceiver) + towerDataElement.contextReceiverGroup.orEmpty()

            receivers.map { receiver ->
                KaImplicitReceiver(
                    token,
                    builder.typeBuilder.buildKtType(receiver.type),
                    builder.buildSymbol(receiver.boundSymbol.fir),
                    index
                )
            }
        }

        val firScopes = towerDataElementsIndexed.flatMap { (index, towerDataElement) ->
            val availableScopes = towerDataElement
                .getAvailableScopes { coneType -> withSyntheticPropertiesScopeOrSelf(coneType) }
                .flatMap { flattenFirScope(it) }
            availableScopes.map { IndexedValue(index, it) }
        }
        val ktScopesWithKinds = createScopesWithKind(firScopes)

        return KaScopeContext(ktScopesWithKinds, implicitReceivers, token)
    }

    private fun createScopesWithKind(firScopes: Iterable<IndexedValue<FirScope>>): List<KaScopeWithKind> {
        return firScopes.map { (index, firScope) ->
            KaScopeWithKind(convertToKtScope(firScope), getScopeKind(firScope, index), token)
        }
    }

    private fun flattenFirScope(firScope: FirScope): List<FirScope> = when (firScope) {
        is FirCompositeScope -> firScope.scopes.flatMap { flattenFirScope(it) }
        is FirNameAwareCompositeScope -> firScope.scopes.flatMap { flattenFirScope(it) }
        else -> listOf(firScope)
    }

    private fun convertToKtScope(firScope: FirScope): KaScope {
        return when (firScope) {
            is FirAbstractSimpleImportingScope -> KaFirNonStarImportingScope(firScope, builder)
            is FirAbstractStarImportingScope -> KaFirStarImportingScope(firScope, analysisSession)
            is FirDefaultStarImportingScope -> KaFirDefaultStarImportingScope(firScope, analysisSession)
            is FirPackageMemberScope -> createPackageScope(firScope.fqName)
            is FirContainingNamesAwareScope -> KaFirDelegatingNamesAwareScope(firScope, builder)
            else -> TODO(firScope::class.toString())
        }
    }

    private fun getScopeKind(firScope: FirScope, indexInTower: Int): KaScopeKind = when (firScope) {
        is FirNameAwareOnlyCallablesScope -> getScopeKind(firScope.delegate, indexInTower)

        is FirLocalScope -> KaScopeKind.LocalScope(indexInTower)
        is FirTypeScope -> KaScopeKind.TypeScope(indexInTower)
        is FirTypeParameterScope -> KaScopeKind.TypeParameterScope(indexInTower)
        is FirPackageMemberScope -> KaScopeKind.PackageMemberScope(indexInTower)

        is FirNestedClassifierScope -> KaScopeKind.StaticMemberScope(indexInTower)
        is FirNestedClassifierScopeWithSubstitution -> KaScopeKind.StaticMemberScope(indexInTower)
        is FirLazyNestedClassifierScope -> KaScopeKind.StaticMemberScope(indexInTower)
        is FirStaticScope -> KaScopeKind.StaticMemberScope(indexInTower)

        is FirExplicitSimpleImportingScope -> KaScopeKind.ExplicitSimpleImportingScope(indexInTower)
        is FirExplicitStarImportingScope -> KaScopeKind.ExplicitStarImportingScope(indexInTower)
        is FirDefaultSimpleImportingScope -> KaScopeKind.DefaultSimpleImportingScope(indexInTower)
        is FirDefaultStarImportingScope -> KaScopeKind.DefaultStarImportingScope(indexInTower)

        is FirScriptDeclarationsScope -> KaScopeKind.ScriptMemberScope(indexInTower)

        else -> unexpectedElementError("scope", firScope)
    }

    private fun createPackageScope(fqName: FqName): KaFirPackageScope {
        return KaFirPackageScope(fqName, analysisSession)
    }

    private fun convertToKtTypeScope(firScope: FirScope): KaTypeScope {
        return when (firScope) {
            is FirContainingNamesAwareScope -> KaFirDelegatingTypeScope(firScope, builder)
            else -> TODO(firScope::class.toString())
        }
    }

    private fun getFirTypeScope(type: KaFirType): FirTypeScope? = type.coneType.scope(
        firResolveSession.useSiteFirSession,
        getScopeSession(),
        CallableCopyTypeCalculator.Forced,
        requiredMembersPhase = FirResolvePhase.STATUS,
    )

    private fun getFirSyntheticPropertiesScope(coneType: ConeKotlinType, typeScope: FirTypeScope): FirSyntheticPropertiesScope? =
        FirSyntheticPropertiesScope.createIfSyntheticNamesProviderIsDefined(
            firResolveSession.useSiteFirSession,
            coneType,
            typeScope
        )

    private fun FirTypeScope.withSyntheticPropertiesScopeOrSelf(coneType: ConeKotlinType): FirTypeScope {
        val syntheticPropertiesScope = getFirSyntheticPropertiesScope(coneType, this) ?: return this
        return FirTypeScopeWithSyntheticProperties(typeScope = this, syntheticPropertiesScope)
    }
}

private class FirTypeScopeWithSyntheticProperties(
    val typeScope: FirTypeScope,
    val syntheticPropertiesScope: FirSyntheticPropertiesScope,
) : FirDelegatingTypeScope(typeScope) {
    override fun getCallableNames(): Set<Name> = typeScope.getCallableNames() + syntheticPropertiesScope.getCallableNames()
    override fun mayContainName(name: Name): Boolean = typeScope.mayContainName(name) || syntheticPropertiesScope.mayContainName(name)

    override fun processPropertiesByName(name: Name, processor: (FirVariableSymbol<*>) -> Unit) {
        typeScope.processPropertiesByName(name, processor)
        syntheticPropertiesScope.processPropertiesByName(name, processor)
    }
}

private val JAVA_ENHANCEMENT_FOR_DECLARED_MEMBERS = scopeSessionKey<FirRegularClassSymbol, FirContainingNamesAwareScope>()

private val JAVA_ENHANCEMENT_FOR_STATIC_DECLARED_MEMBERS = scopeSessionKey<FirRegularClassSymbol, FirContainingNamesAwareScope>()

private val JAVA_ENHANCEMENT_FOR_ALL_DECLARED_MEMBERS = scopeSessionKey<FirRegularClassSymbol, FirContainingNamesAwareScope>()
