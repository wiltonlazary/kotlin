/*
 * Copyright 2010-2024 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.analysis.low.level.api.fir.project.structure

import com.intellij.openapi.project.Project
import com.intellij.psi.util.CachedValue
import com.intellij.psi.util.CachedValueProvider
import com.intellij.psi.util.CachedValuesManager
import org.jetbrains.annotations.TestOnly
import org.jetbrains.kotlin.analysis.low.level.api.fir.LLFirInternals
import org.jetbrains.kotlin.analysis.low.level.api.fir.providers.LLFirBuiltinsAndCloneableSessionProvider
import org.jetbrains.kotlin.analysis.low.level.api.fir.sessions.LLFirBuiltinsAndCloneableSession
import org.jetbrains.kotlin.analysis.project.structure.KtBuiltinsModule
import org.jetbrains.kotlin.analyzer.common.CommonPlatformAnalyzerServices
import org.jetbrains.kotlin.config.LanguageVersionSettingsImpl
import org.jetbrains.kotlin.fir.BuiltinTypes
import org.jetbrains.kotlin.fir.PrivateSessionConstructor
import org.jetbrains.kotlin.fir.SessionConfiguration
import org.jetbrains.kotlin.fir.backend.jvm.FirJvmTypeMapper
import org.jetbrains.kotlin.fir.resolve.providers.FirProvider
import org.jetbrains.kotlin.fir.resolve.providers.FirSymbolProvider
import org.jetbrains.kotlin.fir.resolve.providers.impl.FirCloneableSymbolProvider
import org.jetbrains.kotlin.fir.resolve.providers.impl.FirExtensionSyntheticFunctionInterfaceProvider
import org.jetbrains.kotlin.fir.resolve.scopes.wrapScopeWithJvmMapped
import org.jetbrains.kotlin.fir.resolve.transformers.FirDummyCompilerLazyDeclarationResolver
import org.jetbrains.kotlin.fir.scopes.FirKotlinScopeProvider
import org.jetbrains.kotlin.fir.session.*
import org.jetbrains.kotlin.fir.symbols.FirLazyDeclarationResolver
import org.jetbrains.kotlin.platform.TargetPlatform
import org.jetbrains.kotlin.platform.has
import org.jetbrains.kotlin.platform.isCommon
import org.jetbrains.kotlin.platform.isJs
import org.jetbrains.kotlin.platform.jvm.JvmPlatform
import org.jetbrains.kotlin.platform.jvm.isJvm
import org.jetbrains.kotlin.resolve.jvm.modules.JavaModuleResolver
import org.jetbrains.kotlin.resolve.jvm.platform.JvmPlatformAnalyzerServices
import java.util.concurrent.ConcurrentHashMap

@OptIn(PrivateSessionConstructor::class, SessionConfiguration::class)
@LLFirInternals
class LLFirBuiltinsSessionFactory(private val project: Project) {
    private val builtInTypes = BuiltinTypes() // TODO should be platform-specific

    private val builtinsModules = ConcurrentHashMap<TargetPlatform, KtBuiltinsModule>()

    private val builtinsAndCloneableSessions = ConcurrentHashMap<TargetPlatform, CachedValue<LLFirBuiltinsAndCloneableSession>>()

    /**
     * Returns the [platform]'s [KtBuiltinsModule]. [getBuiltinsModule] should be used instead of [getBuiltinsSession] when a
     * [KtBuiltinsModule] is needed as a dependency for other [KtModule]s. This is because during project structure creation, we have to
     * avoid the creation of the builtins *session*, as not all services might have been registered at that point.
     */
    fun getBuiltinsModule(platform: TargetPlatform): KtBuiltinsModule =
        builtinsModules.getOrPut(platform) { KtBuiltinsModule(platform, platform.getAnalyzerServices(), project) }

    fun getBuiltinsSession(platform: TargetPlatform): LLFirBuiltinsAndCloneableSession =
        builtinsAndCloneableSessions.getOrPut(platform) {
            CachedValuesManager.getManager(project).createCachedValue {
                val session = createBuiltinsAndCloneableSession(platform)
                CachedValueProvider.Result(session, session.createValidityTracker())
            }
        }.value

    @TestOnly
    fun clearForTheNextTest() {
        builtinsModules.clear()
        builtinsAndCloneableSessions.clear()
    }

    private fun createBuiltinsAndCloneableSession(platform: TargetPlatform): LLFirBuiltinsAndCloneableSession {
        val builtinsModule = getBuiltinsModule(platform)

        val session = LLFirBuiltinsAndCloneableSession(builtinsModule, builtInTypes)
        val moduleData = LLFirModuleData(session)

        return session.apply {
            registerIdeComponents(project)
            register(FirLazyDeclarationResolver::class, FirDummyCompilerLazyDeclarationResolver)
            registerCommonComponents(LanguageVersionSettingsImpl.DEFAULT/*TODO*/)
            registerCommonComponentsAfterExtensionsAreConfigured()
            registerModuleData(moduleData)

            if (platform.isJvm()) {
                registerJavaComponents(JavaModuleResolver.getInstance(project))
            }

            val kotlinScopeProvider = when {
                platform.isJvm() -> FirKotlinScopeProvider(::wrapScopeWithJvmMapped)
                else -> FirKotlinScopeProvider()
            }
            register(FirKotlinScopeProvider::class, kotlinScopeProvider)

            val symbolProvider = createCompositeSymbolProvider(this) {
                addAll(
                    LLFirLibrarySymbolProviderFactory.getService(project)
                        .createBuiltinsSymbolProvider(session, moduleData, kotlinScopeProvider)
                )
                add(FirExtensionSyntheticFunctionInterfaceProvider(session, moduleData, kotlinScopeProvider))
                if (platform.has<JvmPlatform>()) {
                    add(FirCloneableSymbolProvider(session, moduleData, kotlinScopeProvider))
                }
            }

            register(FirSymbolProvider::class, symbolProvider)
            register(FirProvider::class, LLFirBuiltinsAndCloneableSessionProvider(symbolProvider))
            register(FirJvmTypeMapper::class, FirJvmTypeMapper(this))
        }
    }

    companion object {
        fun getInstance(project: Project): LLFirBuiltinsSessionFactory =
            project.getService(LLFirBuiltinsSessionFactory::class.java)
    }
}

private fun TargetPlatform.getAnalyzerServices() = when {
    isJvm() -> JvmPlatformAnalyzerServices
    isJs() -> JvmPlatformAnalyzerServices/*TODO*/
//    isNative() -> NativePlatformAnalyzerServices
    isCommon() -> CommonPlatformAnalyzerServices
    else -> JvmPlatformAnalyzerServices/*TODO*/
}
