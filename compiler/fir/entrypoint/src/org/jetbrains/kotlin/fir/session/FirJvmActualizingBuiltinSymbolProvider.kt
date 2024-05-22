/*
 * Copyright 2010-2024 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.fir.session

import org.jetbrains.kotlin.config.AnalysisFlags
import org.jetbrains.kotlin.fir.analysis.checkers.hasAnnotationOrInsideAnnotatedClass
import org.jetbrains.kotlin.fir.languageVersionSettings
import org.jetbrains.kotlin.fir.resolve.providers.FirSymbolNamesProvider
import org.jetbrains.kotlin.fir.resolve.providers.FirSymbolProvider
import org.jetbrains.kotlin.fir.resolve.providers.FirSymbolProviderInternals
import org.jetbrains.kotlin.fir.resolve.providers.impl.FirBuiltinSymbolProvider
import org.jetbrains.kotlin.fir.symbols.impl.*
import org.jetbrains.kotlin.name.ClassId
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.name.Name
import org.jetbrains.kotlin.name.StandardClassIds

/**
 * It's initialized on the top of module-based providers for platform source sets.
 *
 * If compiler encounters an actual declaration with a corresponding expect one
 * marked with `ActualizeByJvmBuiltinProvider` annotation, then
 * `FirJvmActualizingBuiltinSymbolProvider` tries to return a member from `FirBuiltinSymbolProvider`
 * instead of falling through to an expect declaration in common source set.
 *
 * It's needed while initializing `Fir2IrLazyClass` to prevent resolving
 * to symbols from common source set
 *
 * Those actuals can be treated as declarations in a virtual file
 */
class FirJvmActualizingBuiltinSymbolProvider(
    private val refinedSourceSymbolProviders: List<FirSymbolProvider>,
    private val builtinSymbolProvider: FirBuiltinSymbolProvider,
) : FirSymbolProvider(builtinSymbolProvider.session) {
    init {
        require(session.languageVersionSettings.getFlag(AnalysisFlags.stdlibCompilation))
    }

    override fun getClassLikeSymbolByClassId(classId: ClassId): FirRegularClassSymbol? {
        for (symbolProvider in refinedSourceSymbolProviders) {
            val classSymbol = symbolProvider.getClassLikeSymbolByClassId(classId) ?: continue
            if (!classSymbol.hasAnnotationOrInsideAnnotatedClass(
                    StandardClassIds.Annotations.ActualizeByJvmBuiltinProvider,
                    symbolProvider.session
                )
            ) {
                continue
            }

            // If there are multiple declarations with the same name, they will be reported as redeclarations by a checker
            return builtinSymbolProvider.getClassLikeSymbolByClassId(classId)
        }

        return null
    }

    override val symbolNamesProvider: FirSymbolNamesProvider = builtinSymbolProvider.symbolNamesProvider

    @FirSymbolProviderInternals
    override fun getTopLevelCallableSymbolsTo(destination: MutableList<FirCallableSymbol<*>>, packageFqName: FqName, name: Name) {
    }

    @FirSymbolProviderInternals
    override fun getTopLevelFunctionSymbolsTo(destination: MutableList<FirNamedFunctionSymbol>, packageFqName: FqName, name: Name) {
    }

    @FirSymbolProviderInternals
    override fun getTopLevelPropertySymbolsTo(destination: MutableList<FirPropertySymbol>, packageFqName: FqName, name: Name) {
    }

    override fun getPackage(fqName: FqName): FqName? = builtinSymbolProvider.getPackage(fqName)
}