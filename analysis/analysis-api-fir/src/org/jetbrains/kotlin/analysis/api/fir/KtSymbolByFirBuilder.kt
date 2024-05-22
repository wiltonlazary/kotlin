/*
 * Copyright 2010-2023 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.analysis.api.fir

import com.intellij.openapi.project.Project
import com.intellij.util.containers.ContainerUtil
import org.jetbrains.kotlin.analysis.api.KaStarTypeProjection
import org.jetbrains.kotlin.analysis.api.KaTypeArgumentWithVariance
import org.jetbrains.kotlin.analysis.api.KaTypeProjection
import org.jetbrains.kotlin.analysis.api.fir.signatures.KaFirFunctionLikeSubstitutorBasedSignature
import org.jetbrains.kotlin.analysis.api.fir.signatures.KaFirVariableLikeSubstitutorBasedSignature
import org.jetbrains.kotlin.analysis.api.fir.symbols.*
import org.jetbrains.kotlin.analysis.api.fir.types.*
import org.jetbrains.kotlin.analysis.api.lifetime.KaLifetimeToken
import org.jetbrains.kotlin.analysis.api.signatures.KaCallableSignature
import org.jetbrains.kotlin.analysis.api.signatures.KaFunctionLikeSignature
import org.jetbrains.kotlin.analysis.api.signatures.KaVariableLikeSignature
import org.jetbrains.kotlin.analysis.api.symbols.*
import org.jetbrains.kotlin.analysis.api.types.KaSubstitutor
import org.jetbrains.kotlin.analysis.api.types.KaType
import org.jetbrains.kotlin.analysis.low.level.api.fir.api.LLFirResolveSession
import org.jetbrains.kotlin.analysis.low.level.api.fir.util.errorWithFirSpecificEntries
import org.jetbrains.kotlin.analysis.providers.KotlinPackageProvider
import org.jetbrains.kotlin.descriptors.ClassKind
import org.jetbrains.kotlin.fir.FirElement
import org.jetbrains.kotlin.fir.FirSession
import org.jetbrains.kotlin.fir.declarations.*
import org.jetbrains.kotlin.fir.declarations.FirOuterClassTypeParameterRef
import org.jetbrains.kotlin.fir.declarations.impl.FirFieldImpl
import org.jetbrains.kotlin.fir.diagnostics.ConeCannotInferTypeParameterType
import org.jetbrains.kotlin.fir.diagnostics.ConeSimpleDiagnostic
import org.jetbrains.kotlin.fir.java.declarations.FirJavaField
import org.jetbrains.kotlin.fir.originalForSubstitutionOverride
import org.jetbrains.kotlin.fir.originalIfFakeOverride
import org.jetbrains.kotlin.fir.renderer.FirRenderer
import org.jetbrains.kotlin.fir.resolve.diagnostics.ConeUnmatchedTypeArgumentsError
import org.jetbrains.kotlin.fir.resolve.diagnostics.ConeUnresolvedError
import org.jetbrains.kotlin.fir.resolve.diagnostics.ConeUnresolvedSymbolError
import org.jetbrains.kotlin.fir.resolve.getContainingClass
import org.jetbrains.kotlin.fir.resolve.getSymbolByLookupTag
import org.jetbrains.kotlin.fir.resolve.providers.FirSymbolProvider
import org.jetbrains.kotlin.fir.resolve.providers.symbolProvider
import org.jetbrains.kotlin.fir.resolve.substitution.ChainedSubstitutor
import org.jetbrains.kotlin.fir.resolve.substitution.ConeSubstitutor
import org.jetbrains.kotlin.fir.resolve.substitution.ConeSubstitutorByMap
import org.jetbrains.kotlin.fir.resolve.toSymbol
import org.jetbrains.kotlin.fir.scopes.impl.importedFromObjectOrStaticData
import org.jetbrains.kotlin.fir.scopes.impl.originalConstructorIfTypeAlias
import org.jetbrains.kotlin.fir.symbols.ConeClassLikeLookupTag
import org.jetbrains.kotlin.fir.symbols.ConeTypeParameterLookupTag
import org.jetbrains.kotlin.fir.symbols.FirBasedSymbol
import org.jetbrains.kotlin.fir.symbols.impl.*
import org.jetbrains.kotlin.fir.symbols.lazyResolveToPhase
import org.jetbrains.kotlin.fir.types.*
import org.jetbrains.kotlin.fir.types.impl.ConeClassLikeTypeImpl
import org.jetbrains.kotlin.fir.utils.exceptions.withConeTypeEntry
import org.jetbrains.kotlin.fir.utils.exceptions.withFirEntry
import org.jetbrains.kotlin.fir.utils.exceptions.withFirSymbolEntry
import org.jetbrains.kotlin.fir.visitors.FirVisitorVoid
import org.jetbrains.kotlin.name.ClassId
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.types.Variance
import org.jetbrains.kotlin.utils.exceptions.errorWithAttachment
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

/**
 * Maps FirElement to KaSymbol & ConeType to KaType, thread safe
 */
internal class KaSymbolByFirBuilder(
    private val project: Project,
    val analysisSession: KaFirSession,
    val token: KaLifetimeToken,
) {
    private val firResolveSession: LLFirResolveSession get() = analysisSession.firResolveSession
    private val firProvider: FirSymbolProvider get() = rootSession.symbolProvider
    val rootSession: FirSession = firResolveSession.useSiteFirSession

    private val symbolsCache = BuilderCache<FirBasedSymbol<*>, KaSymbol>()

    val classifierBuilder = ClassifierSymbolBuilder()
    val functionLikeBuilder = FunctionLikeSymbolBuilder()
    val variableLikeBuilder = VariableLikeSymbolBuilder()
    val callableBuilder = CallableSymbolBuilder()
    val anonymousInitializerBuilder = AnonymousInitializerBuilder()
    val typeBuilder = TypeBuilder()

    fun buildSymbol(fir: FirDeclaration): KaSymbol =
        buildSymbol(fir.symbol)

    fun buildSymbol(firSymbol: FirBasedSymbol<*>): KaSymbol {
        return when (firSymbol) {
            is FirClassLikeSymbol<*> -> classifierBuilder.buildClassLikeSymbol(firSymbol)
            is FirTypeParameterSymbol -> classifierBuilder.buildTypeParameterSymbol(firSymbol)
            is FirCallableSymbol<*> -> callableBuilder.buildCallableSymbol(firSymbol)
            is FirFileSymbol -> buildFileSymbol(firSymbol)
            is FirScriptSymbol -> buildScriptSymbol(firSymbol)
            else -> throwUnexpectedElementError(firSymbol)
        }
    }


    fun buildDestructuringDeclarationSymbol(firSymbol: FirVariableSymbol<*>): KaDestructuringDeclarationSymbol {
        return symbolsCache.cache(firSymbol) {
            KaFirDestructuringDeclarationSymbol(firSymbol, analysisSession)
        }
    }

    fun buildEnumEntrySymbol(firSymbol: FirEnumEntrySymbol) =
        symbolsCache.cache(firSymbol) { KaFirEnumEntrySymbol(firSymbol, analysisSession) }

    fun buildFileSymbol(firSymbol: FirFileSymbol) = KaFirFileSymbol(firSymbol, analysisSession)

    fun buildScriptSymbol(firSymbol: FirScriptSymbol) = KaFirScriptSymbol(firSymbol, analysisSession)

    private val packageProvider: KotlinPackageProvider get() = analysisSession.useSitePackageProvider

    fun createPackageSymbolIfOneExists(packageFqName: FqName): KaFirPackageSymbol? {
        val exists = packageProvider.doesPackageExist(packageFqName, analysisSession.targetPlatform)
        if (!exists) {
            return null
        }
        return createPackageSymbol(packageFqName)
    }

    fun createPackageSymbol(packageFqName: FqName): KaFirPackageSymbol {
        return KaFirPackageSymbol(packageFqName, project, token)
    }

    inner class ClassifierSymbolBuilder {
        fun buildClassifierSymbol(firSymbol: FirClassifierSymbol<*>): KaClassifierSymbol {
            return when (firSymbol) {
                is FirClassLikeSymbol<*> -> classifierBuilder.buildClassLikeSymbol(firSymbol)
                is FirTypeParameterSymbol -> buildTypeParameterSymbol(firSymbol)
                else -> throwUnexpectedElementError(firSymbol)
            }
        }


        fun buildClassLikeSymbol(firSymbol: FirClassLikeSymbol<*>): KaClassLikeSymbol {
            return when (firSymbol) {
                is FirClassSymbol<*> -> buildClassOrObjectSymbol(firSymbol)
                is FirTypeAliasSymbol -> buildTypeAliasSymbol(firSymbol)
                else -> throwUnexpectedElementError(firSymbol)
            }
        }

        fun buildClassOrObjectSymbol(firSymbol: FirClassSymbol<*>): KaClassOrObjectSymbol {
            return when (firSymbol) {
                is FirAnonymousObjectSymbol -> buildAnonymousObjectSymbol(firSymbol)
                is FirRegularClassSymbol -> buildNamedClassOrObjectSymbol(firSymbol)
                else -> throwUnexpectedElementError(firSymbol)
            }
        }

        fun buildNamedClassOrObjectSymbol(symbol: FirRegularClassSymbol): KaFirNamedClassOrObjectSymbol {
            return symbolsCache.cache(symbol) { KaFirNamedClassOrObjectSymbol(symbol, analysisSession) }
        }

        fun buildAnonymousObjectSymbol(symbol: FirAnonymousObjectSymbol): KaAnonymousObjectSymbol {
            return symbolsCache.cache(symbol) {
                when (symbol.classKind) {
                    ClassKind.ENUM_ENTRY -> KaFirEnumEntryInitializerSymbol(symbol, analysisSession)
                    else -> KaFirAnonymousObjectSymbol(symbol, analysisSession)
                }
            }
        }

        fun buildTypeAliasSymbol(symbol: FirTypeAliasSymbol): KaFirTypeAliasSymbol {
            return symbolsCache.cache(symbol) { KaFirTypeAliasSymbol(symbol, analysisSession) }
        }

        fun buildTypeParameterSymbol(firSymbol: FirTypeParameterSymbol): KaFirTypeParameterSymbol {
            return symbolsCache.cache(firSymbol) {
                KaFirTypeParameterSymbol(firSymbol, analysisSession)
            }
        }

        fun buildTypeParameterSymbolByLookupTag(lookupTag: ConeTypeParameterLookupTag): KaTypeParameterSymbol? {
            val firTypeParameterSymbol = firProvider.getSymbolByLookupTag(lookupTag) as? FirTypeParameterSymbol ?: return null
            return buildTypeParameterSymbol(firTypeParameterSymbol)
        }

        fun buildClassLikeSymbolByClassId(classId: ClassId): KaClassLikeSymbol? {
            val firClassLikeSymbol = firProvider.getClassLikeSymbolByClassId(classId) ?: return null
            return buildClassLikeSymbol(firClassLikeSymbol)
        }

        fun buildClassLikeSymbolByLookupTag(lookupTag: ConeClassLikeLookupTag): KaClassLikeSymbol? {
            val firClassLikeSymbol = firProvider.getSymbolByLookupTag(lookupTag) ?: return null
            return buildClassLikeSymbol(firClassLikeSymbol)
        }
    }

    inner class FunctionLikeSymbolBuilder {
        fun buildFunctionLikeSymbol(firSymbol: FirFunctionSymbol<*>): KaFunctionLikeSymbol {
            return when (firSymbol) {
                is FirNamedFunctionSymbol -> {
                    if (firSymbol.origin == FirDeclarationOrigin.SamConstructor) {
                        buildSamConstructorSymbol(firSymbol)
                    } else {
                        buildFunctionSymbol(firSymbol)
                    }
                }
                is FirConstructorSymbol -> buildConstructorSymbol(firSymbol)
                is FirAnonymousFunctionSymbol -> buildAnonymousFunctionSymbol(firSymbol)
                is FirPropertyAccessorSymbol -> buildPropertyAccessorSymbol(firSymbol)
                else -> throwUnexpectedElementError(firSymbol)
            }
        }

        fun buildFunctionLikeSignature(fir: FirFunctionSymbol<*>): KaFunctionLikeSignature<KaFunctionLikeSymbol> {
            if (fir is FirNamedFunctionSymbol && fir.origin != FirDeclarationOrigin.SamConstructor)
                return buildFunctionSignature(fir)
            return with(analysisSession) { buildFunctionLikeSymbol(fir).asSignature() }
        }

        fun buildFunctionSymbol(firSymbol: FirNamedFunctionSymbol): KaFirFunctionSymbol {
            firSymbol.fir.unwrapSubstitutionOverrideIfNeeded()?.let {
                return buildFunctionSymbol(it.symbol)
            }

            if (firSymbol.dispatchReceiverType?.contains { it is ConeStubType } == true) {
                return buildFunctionSymbol(
                    firSymbol.originalIfFakeOverride()
                        ?: errorWithFirSpecificEntries("Stub type in real declaration", fir = firSymbol.fir)
                )
            }

            firSymbol.unwrapImportedFromObjectOrStatic(::buildFunctionSymbol)?.let { return it }

            check(firSymbol.origin != FirDeclarationOrigin.SamConstructor)
            return symbolsCache.cache(firSymbol) { KaFirFunctionSymbol(firSymbol, analysisSession) }
        }

        fun buildFunctionSignature(firSymbol: FirNamedFunctionSymbol): KaFunctionLikeSignature<KaFirFunctionSymbol> {
            firSymbol.lazyResolveToPhase(FirResolvePhase.IMPLICIT_TYPES_BODY_RESOLVE)
            return KaFirFunctionLikeSubstitutorBasedSignature(analysisSession.token, firSymbol, analysisSession.firSymbolBuilder)
        }

        fun buildAnonymousFunctionSymbol(firSymbol: FirAnonymousFunctionSymbol): KaFirAnonymousFunctionSymbol {
            return symbolsCache.cache(firSymbol) {
                KaFirAnonymousFunctionSymbol(firSymbol, analysisSession)
            }
        }

        fun buildConstructorSymbol(firSymbol: FirConstructorSymbol): KaFirConstructorSymbol {
            val originalFirSymbol = firSymbol.fir.originalConstructorIfTypeAlias?.symbol ?: firSymbol
            val unwrapped = originalFirSymbol.originalIfFakeOverride() ?: originalFirSymbol
            return symbolsCache.cache(unwrapped) {
                KaFirConstructorSymbol(unwrapped, analysisSession)
            }
        }

        fun buildSamConstructorSymbol(firSymbol: FirNamedFunctionSymbol): KaFirSamConstructorSymbol {
            check(firSymbol.origin == FirDeclarationOrigin.SamConstructor)
            return symbolsCache.cache(firSymbol) {
                KaFirSamConstructorSymbol(firSymbol, analysisSession)
            }
        }

        fun buildPropertyAccessorSymbol(firSymbol: FirPropertyAccessorSymbol): KaFunctionLikeSymbol {
            return symbolsCache.cache(firSymbol) {
                if (firSymbol.isGetter) {
                    KaFirPropertyGetterSymbol(firSymbol, analysisSession)
                } else {
                    KaFirPropertySetterSymbol(firSymbol, analysisSession)
                }
            }
        }
    }

    inner class VariableLikeSymbolBuilder {
        fun buildVariableLikeSymbol(firSymbol: FirVariableSymbol<*>): KaVariableLikeSymbol {
            return when (firSymbol) {
                is FirPropertySymbol -> buildVariableSymbol(firSymbol)
                is FirValueParameterSymbol -> buildValueParameterSymbol(firSymbol)
                is FirFieldSymbol -> buildFieldSymbol(firSymbol)
                is FirEnumEntrySymbol -> buildEnumEntrySymbol(firSymbol) // TODO enum entry should not be callable
                is FirBackingFieldSymbol -> buildBackingFieldSymbol(firSymbol)
                is FirErrorPropertySymbol -> buildErrorVariableSymbol(firSymbol)

                is FirDelegateFieldSymbol -> throwUnexpectedElementError(firSymbol)
            }
        }

        fun buildVariableLikeSignature(firSymbol: FirVariableSymbol<*>): KaVariableLikeSignature<KaVariableLikeSymbol> {
            if (firSymbol is FirPropertySymbol && !firSymbol.isLocal && firSymbol !is FirSyntheticPropertySymbol) {
                return buildPropertySignature(firSymbol)
            }
            return with(analysisSession) { buildVariableLikeSymbol(firSymbol).asSignature() }
        }

        fun buildVariableSymbol(firSymbol: FirPropertySymbol): KaVariableSymbol {
            return when {
                firSymbol.isLocal -> buildLocalVariableSymbol(firSymbol)
                firSymbol is FirSyntheticPropertySymbol -> buildSyntheticJavaPropertySymbol(firSymbol)
                else -> buildPropertySymbol(firSymbol)
            }
        }

        fun buildPropertySymbol(firSymbol: FirPropertySymbol): KaVariableSymbol {
            checkRequirementForBuildingSymbol<KaKotlinPropertySymbol>(firSymbol, !firSymbol.isLocal)
            checkRequirementForBuildingSymbol<KaKotlinPropertySymbol>(firSymbol, firSymbol !is FirSyntheticPropertySymbol)

            firSymbol.fir.unwrapSubstitutionOverrideIfNeeded()?.let {
                return buildVariableSymbol(it.symbol)
            }

            firSymbol.unwrapImportedFromObjectOrStatic(::buildPropertySymbol)?.let { return it }

            return symbolsCache.cache(firSymbol) {
                KaFirKotlinPropertySymbol(firSymbol, analysisSession)
            }
        }

        fun buildPropertySignature(firSymbol: FirPropertySymbol): KaVariableLikeSignature<KaVariableSymbol> {
            firSymbol.lazyResolveToPhase(FirResolvePhase.IMPLICIT_TYPES_BODY_RESOLVE)
            return KaFirVariableLikeSubstitutorBasedSignature(analysisSession.token, firSymbol, analysisSession.firSymbolBuilder)
        }

        fun buildLocalVariableSymbol(firSymbol: FirPropertySymbol): KaFirLocalVariableSymbol {
            checkRequirementForBuildingSymbol<KaFirLocalVariableSymbol>(firSymbol, firSymbol.isLocal)
            return symbolsCache.cache(firSymbol) {
                KaFirLocalVariableSymbol(firSymbol, analysisSession)
            }
        }

        fun buildErrorVariableSymbol(firSymbol: FirErrorPropertySymbol): KaFirErrorVariableSymbol {
            return symbolsCache.cache(firSymbol) {
                KaFirErrorVariableSymbol(firSymbol, analysisSession)
            }
        }

        fun buildSyntheticJavaPropertySymbol(firSymbol: FirSyntheticPropertySymbol): KaFirSyntheticJavaPropertySymbol {
            return symbolsCache.cache(firSymbol) {
                KaFirSyntheticJavaPropertySymbol(firSymbol, analysisSession)
            }
        }

        fun buildValueParameterSymbol(firSymbol: FirValueParameterSymbol): KaValueParameterSymbol {
            val functionSymbol = firSymbol.containingFunctionSymbol
            functionSymbol.fir.unwrapSubstitutionOverrideIfNeeded()?.let { unwrappedFunction ->
                val originalIndex = functionSymbol.valueParameterSymbols.indexOf(firSymbol)
                if (originalIndex == -1) {
                    errorWithAttachment("Containing function doesn't have the corresponding parameter") {
                        withFirSymbolEntry("valueParameter", firSymbol)
                        withFirSymbolEntry("function", functionSymbol)
                    }
                }

                val unwrappedParameter = unwrappedFunction.symbol.valueParameterSymbols[originalIndex]
                return buildValueParameterSymbol(unwrappedParameter)
            }

            return symbolsCache.cache(firSymbol) {
                KaFirValueParameterSymbol(firSymbol, analysisSession)
            }
        }


        fun buildFieldSymbol(firSymbol: FirFieldSymbol): KaFirJavaFieldSymbol {
            firSymbol.unwrapImportedFromObjectOrStatic(::buildFieldSymbol)?.let { return it }

            checkRequirementForBuildingSymbol<KaFirJavaFieldSymbol>(firSymbol, firSymbol.fir.isJavaFieldOrSubstitutionOverrideOfJavaField())
            return symbolsCache.cache(firSymbol) { KaFirJavaFieldSymbol(firSymbol, analysisSession) }
        }

        fun buildBackingFieldSymbol(firSymbol: FirBackingFieldSymbol): KaFirBackingFieldSymbol {
            return KaFirBackingFieldSymbol(firSymbol, analysisSession)
        }

        fun buildBackingFieldSymbolByProperty(firSymbol: FirPropertySymbol): KaFirBackingFieldSymbol {
            val backingFieldSymbol = firSymbol.backingFieldSymbol
                ?: error("FirProperty backingField is null")
            return buildBackingFieldSymbol(backingFieldSymbol)
        }

        private fun FirField.isJavaFieldOrSubstitutionOverrideOfJavaField(): Boolean = when (this) {
            is FirJavaField -> true
            is FirFieldImpl -> (this as FirField).originalForSubstitutionOverride?.isJavaFieldOrSubstitutionOverrideOfJavaField() == true
            else -> throwUnexpectedElementError(this)
        }
    }

    inner class CallableSymbolBuilder {
        fun buildCallableSymbol(firSymbol: FirCallableSymbol<*>): KaCallableSymbol {
            return when (firSymbol) {
                is FirPropertyAccessorSymbol -> buildPropertyAccessorSymbol(firSymbol)
                is FirFunctionSymbol<*> -> functionLikeBuilder.buildFunctionLikeSymbol(firSymbol)
                is FirVariableSymbol<*> -> variableLikeBuilder.buildVariableLikeSymbol(firSymbol)
                else -> throwUnexpectedElementError(firSymbol)
            }
        }

        fun buildCallableSignature(firSymbol: FirCallableSymbol<*>): KaCallableSignature<KaCallableSymbol> {
            return when (firSymbol) {
                is FirPropertyAccessorSymbol ->  with(analysisSession) { buildPropertyAccessorSymbol(firSymbol).asSignature() }
                is FirFunctionSymbol<*> -> functionLikeBuilder.buildFunctionLikeSignature(firSymbol)
                is FirVariableSymbol<*> -> variableLikeBuilder.buildVariableLikeSignature(firSymbol)
                else -> throwUnexpectedElementError(firSymbol)
            }
        }


        fun buildPropertyAccessorSymbol(firSymbol: FirPropertyAccessorSymbol): KaPropertyAccessorSymbol {
            return when {
                firSymbol.isGetter -> buildGetterSymbol(firSymbol)
                else -> buildSetterSymbol(firSymbol)
            }
        }

        fun buildGetterSymbol(firSymbol: FirPropertyAccessorSymbol): KaFirPropertyGetterSymbol {
            checkRequirementForBuildingSymbol<KaFirPropertyGetterSymbol>(firSymbol, firSymbol.isGetter)
            return symbolsCache.cache(firSymbol) {
                KaFirPropertyGetterSymbol(firSymbol, analysisSession)
            }
        }

        fun buildSetterSymbol(firSymbol: FirPropertyAccessorSymbol): KaFirPropertySetterSymbol {
            checkRequirementForBuildingSymbol<KaFirPropertySetterSymbol>(firSymbol, firSymbol.isSetter)
            return symbolsCache.cache(firSymbol) {
                KaFirPropertySetterSymbol(firSymbol, analysisSession)
            }
        }

        fun buildBackingFieldSymbol(firSymbol: FirBackingFieldSymbol): KaFirBackingFieldSymbol {
            return symbolsCache.cache(firSymbol) {
                KaFirBackingFieldSymbol(firSymbol, analysisSession)
            }
        }

        fun buildExtensionReceiverSymbol(firCallableSymbol: FirCallableSymbol<*>): KaReceiverParameterSymbol? {
            if (firCallableSymbol.fir.receiverParameter == null) return null
            return KaFirReceiverParameterSymbol(firCallableSymbol, analysisSession)
        }
    }

    inner class AnonymousInitializerBuilder {
        fun buildClassInitializer(firSymbol: FirAnonymousInitializerSymbol): KaClassInitializerSymbol {
            return symbolsCache.cache(firSymbol) { KaFirClassInitializerSymbol(firSymbol, analysisSession) }
        }
    }

    inner class TypeBuilder {
        fun buildKtType(coneType: ConeKotlinType): KaType {
            return when (coneType) {
                is ConeClassLikeTypeImpl -> {
                    when {
                        coneType.lookupTag.toSymbol(rootSession) == null -> {
                            KaFirClassErrorType(coneType, ConeUnresolvedSymbolError(coneType.lookupTag.classId), this@KaSymbolByFirBuilder)
                        }
                        hasFunctionalClassId(coneType) -> KaFirFunctionalType(coneType, this@KaSymbolByFirBuilder)
                        else -> KaFirUsualClassType(coneType, this@KaSymbolByFirBuilder)
                    }
                }
                is ConeTypeParameterType -> KaFirTypeParameterType(coneType, this@KaSymbolByFirBuilder)
                is ConeErrorType -> when (val diagnostic = coneType.diagnostic) {
                    is ConeUnresolvedError, is ConeUnmatchedTypeArgumentsError ->
                        KaFirClassErrorType(coneType, diagnostic, this@KaSymbolByFirBuilder)
                    else -> KaFirTypeErrorType(coneType, this@KaSymbolByFirBuilder)
                }
                is ConeDynamicType -> KaFirDynamicType(coneType, this@KaSymbolByFirBuilder)
                is ConeFlexibleType -> KaFirFlexibleType(coneType, this@KaSymbolByFirBuilder)
                is ConeIntersectionType -> KaFirIntersectionType(coneType, this@KaSymbolByFirBuilder)
                is ConeDefinitelyNotNullType -> KaFirDefinitelyNotNullType(coneType, this@KaSymbolByFirBuilder)
                is ConeCapturedType -> KaFirCapturedType(coneType, this@KaSymbolByFirBuilder)
                is ConeIntegerLiteralConstantType -> KaFirIntegerLiteralType(coneType, this@KaSymbolByFirBuilder)
                is ConeIntegerConstantOperatorType -> buildKtType(coneType.getApproximatedType())

                is ConeTypeVariableType -> {
                    val diagnostic = when ( val typeParameter = coneType.typeConstructor.originalTypeParameter) {
                        null -> ConeSimpleDiagnostic("Cannot infer parameter type for ${coneType.typeConstructor.debugName}")
                        else -> ConeCannotInferTypeParameterType((typeParameter as ConeTypeParameterLookupTag).typeParameterSymbol)
                    }
                    buildKtType(ConeErrorType(diagnostic, isUninferredParameter = true, attributes = coneType.attributes))
                }
                else -> throwUnexpectedElementError(coneType)
            }
        }

        private fun hasFunctionalClassId(coneType: ConeClassLikeTypeImpl): Boolean {
            return coneType.isSomeFunctionType(analysisSession.firResolveSession.useSiteFirSession)
        }

        fun buildKtType(coneType: FirTypeRef): KaType {
            return buildKtType(coneType.coneType)
        }

        fun buildTypeProjection(coneType: ConeTypeProjection): KaTypeProjection = when (coneType) {
            is ConeStarProjection -> KaStarTypeProjection(token)
            is ConeKotlinTypeProjection -> KaTypeArgumentWithVariance(
                buildKtType(coneType.type),
                coneType.kind.toVariance(),
                token,
            )
        }

        private fun ProjectionKind.toVariance(): Variance = when (this) {
            ProjectionKind.OUT -> Variance.OUT_VARIANCE
            ProjectionKind.IN -> Variance.IN_VARIANCE
            ProjectionKind.INVARIANT -> Variance.INVARIANT
            ProjectionKind.STAR -> error("KtStarProjectionTypeArgument should not be directly created")
        }

        fun buildSubstitutor(substitutor: ConeSubstitutor): KaSubstitutor {
            if (substitutor == ConeSubstitutor.Empty) return KaSubstitutor.Empty(token)
            return when (substitutor) {
                is ConeSubstitutorByMap -> KaFirMapBackedSubstitutor(substitutor, this@KaSymbolByFirBuilder)
                is ChainedSubstitutor -> KaFirChainedSubstitutor(substitutor, this@KaSymbolByFirBuilder)
                else -> KaFirGenericSubstitutor(substitutor, this@KaSymbolByFirBuilder)
            }
        }
    }

    /**
     * We shouldn't expose imported callables as they may have different [org.jetbrains.kotlin.name.CallableId]s
     * than the original callables.
     * Resolved FIR has explicitly declared original objects receivers instead of such synthetic callables.
     */
    private inline fun <reified T : FirCallableSymbol<*>, R> T.unwrapImportedFromObjectOrStatic(builder: (T) -> R): R? {
        return if (origin == FirDeclarationOrigin.ImportedFromObjectOrStatic) {
            val originalSymbol = fir.importedFromObjectOrStaticData!!.original.symbol
            // The symbol has to be the same type as it is just a copy with possibly different `CallableId`
            builder(originalSymbol as T)
        } else {
            null
        }
    }

    /**
     * N.B. This functions lifts only a single layer of SUBSTITUTION_OVERRIDE at a time.
     */
    private inline fun <reified T : FirCallableDeclaration> T.unwrapSubstitutionOverrideIfNeeded(): T? {
        unwrapUseSiteSubstitutionOverride()?.let { return it }

        unwrapInheritanceSubstitutionOverrideIfNeeded()?.let { return it }

        return null
    }

    /**
     * Use-site substitution override happens in situations like this:
     *
     * ```
     * interface List<A> { fun get(i: Int): A }
     *
     * fun take(list: List<String>) {
     *   list.get(10) // this call
     * }
     * ```
     *
     * In FIR, `List::get` symbol in the example will be a substitution override with a `String` instead of `A`.
     * We want to lift such substitution overrides.
     *
     * @receiver A declaration that needs to be unwrapped.
     * @return An unsubstituted declaration ([originalForSubstitutionOverride]]) if [this] is a use-site substitution override.
     */
    private inline fun <reified T : FirCallableDeclaration> T.unwrapUseSiteSubstitutionOverride(): T? {
        val originalDeclaration = originalForSubstitutionOverride ?: return null
        return originalDeclaration.takeIf { this.origin is FirDeclarationOrigin.SubstitutionOverride.CallSite }
    }

    /**
     * We want to unwrap a SUBSTITUTION_OVERRIDE wrapper if it doesn't affect the declaration's signature in any way. If the signature
     * is somehow changed, then we want to keep the wrapper.
     *
     * Such substitute overrides happen because of inheritance.
     *
     * If the declaration references only its own type parameters, or parameters from the outer declarations, then
     * we consider that it's signature will not be changed by the SUBSTITUTION_OVERRIDE, so the wrapper can be unwrapped.
     *
     * This have a few caveats when it comes to the inner classes. TODO Provide a reference to some more in-detail description of that.
     *
     * @receiver A declaration that needs to be unwrapped.
     * @return An unsubstituted declaration ([originalForSubstitutionOverride]]) if it exists and if it does not have any change
     * in signature; `null` otherwise.
     */
    private inline fun <reified T : FirCallableDeclaration> T.unwrapInheritanceSubstitutionOverrideIfNeeded(): T? {
        val containingClass = getContainingClass(rootSession) ?: return null
        val originalDeclaration = originalForSubstitutionOverride ?: return null

        val allowedTypeParameters = buildSet {
            // declaration's own parameters
            originalDeclaration.typeParameters.mapTo(this) { it.symbol.toLookupTag() }

            // captured outer parameters
            containingClass.typeParameters.mapNotNullTo(this) {
                (it as? FirOuterClassTypeParameterRef)?.symbol?.toLookupTag()
            }
        }

        val usedTypeParameters = collectReferencedTypeParameters(originalDeclaration)

        return if (allowedTypeParameters.containsAll(usedTypeParameters)) {
            originalDeclaration
        } else {
            null
        }
    }

    companion object {
        private fun throwUnexpectedElementError(element: FirBasedSymbol<*>): Nothing {
            errorWithAttachment("Unexpected ${element::class.simpleName}") {
                withFirSymbolEntry("firSymbol", element)
            }
        }

        private fun throwUnexpectedElementError(element: FirElement): Nothing {
            errorWithAttachment("Unexpected ${element::class.simpleName}") {
                withFirEntry("firElement", element)
            }
        }

        private fun throwUnexpectedElementError(element: ConeKotlinType): Nothing {
            errorWithAttachment("Unexpected ${element::class.simpleName}") {
                withConeTypeEntry("coneType", element)
            }
        }

        @OptIn(ExperimentalContracts::class)
        private inline fun <reified S : KaSymbol> checkRequirementForBuildingSymbol(
            firSymbol: FirBasedSymbol<*>,
            requirement: Boolean,
        ) {
            contract {
                returns() implies requirement
            }
            require(requirement) {
                val renderedSymbol = FirRenderer.withResolvePhase().renderElementWithTypeAsString(firSymbol.fir)
                "Cannot build ${S::class.simpleName} for $renderedSymbol}"
            }
        }
    }
}

private class BuilderCache<From, To : KaSymbol> {
    private val cache = ContainerUtil.createConcurrentWeakKeySoftValueMap<From, To>()

    inline fun <reified S : To> cache(key: From, calculation: () -> S): S {
        val value = cache.getOrPut(key, calculation)
        return value as? S
            ?: errorWithAttachment("Cannot cast ${value::class} to ${S::class}") {
                withEntry("value", value.toString())
            }
    }
}

internal fun FirElement.buildSymbol(builder: KaSymbolByFirBuilder) =
    (this as? FirDeclaration)?.symbol?.let(builder::buildSymbol)

internal fun FirDeclaration.buildSymbol(builder: KaSymbolByFirBuilder): KaSymbol =
    builder.buildSymbol(symbol)

internal fun FirBasedSymbol<*>.buildSymbol(builder: KaSymbolByFirBuilder): KaSymbol =
    builder.buildSymbol(this)

private fun collectReferencedTypeParameters(declaration: FirCallableDeclaration): Set<ConeTypeParameterLookupTag> {
    val allUsedTypeParameters = mutableSetOf<ConeTypeParameterLookupTag>()

    declaration.accept(object : FirVisitorVoid() {
        override fun visitElement(element: FirElement) {
            element.acceptChildren(this)
        }

        override fun visitSimpleFunction(simpleFunction: FirSimpleFunction) {
            simpleFunction.typeParameters.forEach { it.accept(this) }

            simpleFunction.receiverParameter?.accept(this)
            simpleFunction.valueParameters.forEach { it.returnTypeRef.accept(this) }
            simpleFunction.returnTypeRef.accept(this)
        }

        override fun visitProperty(property: FirProperty) {
            property.typeParameters.forEach { it.accept(this) }

            property.receiverParameter?.accept(this)
            property.returnTypeRef.accept(this)
        }

        override fun visitReceiverParameter(receiverParameter: FirReceiverParameter) {
            receiverParameter.typeRef.accept(this)
        }

        override fun visitResolvedTypeRef(resolvedTypeRef: FirResolvedTypeRef) {
            super.visitResolvedTypeRef(resolvedTypeRef)

            handleTypeRef(resolvedTypeRef)
        }

        private fun handleTypeRef(resolvedTypeRef: FirResolvedTypeRef) {
            val resolvedType = resolvedTypeRef.type

            resolvedType.forEachType {
                if (it is ConeTypeParameterType) {
                    allUsedTypeParameters.add(it.lookupTag)
                }
            }
        }
    })

    return allUsedTypeParameters
}
