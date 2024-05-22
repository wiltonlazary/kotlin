/*
 * Copyright 2010-2023 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.light.classes.symbol

import com.intellij.openapi.project.Project
import com.intellij.openapi.util.ModificationTracker
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiElement
import com.intellij.psi.search.GlobalSearchScope
import org.jetbrains.kotlin.analysis.decompiled.light.classes.DecompiledLightClassesFactory
import org.jetbrains.kotlin.analysis.decompiled.light.classes.KtLightClassForDecompiledDeclaration
import org.jetbrains.kotlin.analysis.decompiler.psi.file.KtClsFile
import org.jetbrains.kotlin.analysis.project.structure.*
import org.jetbrains.kotlin.analysis.providers.createAllLibrariesModificationTracker
import org.jetbrains.kotlin.analysis.providers.createDeclarationProvider
import org.jetbrains.kotlin.analysis.providers.createPackageProvider
import org.jetbrains.kotlin.analysis.providers.createProjectWideOutOfBlockModificationTracker
import org.jetbrains.kotlin.asJava.KotlinAsJavaSupportBase
import org.jetbrains.kotlin.asJava.classes.KtFakeLightClass
import org.jetbrains.kotlin.asJava.classes.KtLightClass
import org.jetbrains.kotlin.asJava.classes.KtLightClassForFacade
import org.jetbrains.kotlin.light.classes.symbol.classes.SymbolBasedFakeLightClass
import org.jetbrains.kotlin.light.classes.symbol.classes.SymbolLightClassForFacade
import org.jetbrains.kotlin.light.classes.symbol.classes.SymbolLightClassForScript
import org.jetbrains.kotlin.light.classes.symbol.classes.createSymbolLightClassNoCache
import org.jetbrains.kotlin.name.ClassId
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.name.parentOrNull
import org.jetbrains.kotlin.platform.has
import org.jetbrains.kotlin.platform.jvm.JvmPlatform
import org.jetbrains.kotlin.psi.KtClassOrObject
import org.jetbrains.kotlin.psi.KtElement
import org.jetbrains.kotlin.psi.KtFile
import org.jetbrains.kotlin.psi.KtScript

class SymbolKotlinAsJavaSupport(project: Project) : KotlinAsJavaSupportBase<KtModule>(project) {
    private val projectStructureProvider by lazy { ProjectStructureProvider.getInstance(project) }

    private fun PsiElement.getModuleIfSupportEnabled(): KtModule? = projectStructureProvider.getModule(
        element = this,
        contextualModule = null,
    ).takeIf(KtModule::isLightClassesEnabled)

    override fun findClassOrObjectDeclarationsInPackage(
        packageFqName: FqName,
        searchScope: GlobalSearchScope
    ): Collection<KtClassOrObject> = project.createDeclarationProvider(searchScope, contextualModule = null).run {
        getTopLevelKotlinClassLikeDeclarationNamesInPackage(packageFqName).flatMap {
            getAllClassesByClassId(ClassId.topLevel(packageFqName.child(it)))
        }
    }

    override fun findFilesForPackage(packageFqName: FqName, searchScope: GlobalSearchScope): Collection<KtFile> = buildSet {
        addAll(project.createDeclarationProvider(searchScope, contextualModule = null).findFilesForFacadeByPackage(packageFqName))
        findClassOrObjectDeclarationsInPackage(packageFqName, searchScope).mapTo(this) {
            it.containingKtFile
        }
    }

    override fun findFilesForFacadeByPackage(packageFqName: FqName, searchScope: GlobalSearchScope): Collection<KtFile> {
        return project.createDeclarationProvider(searchScope, contextualModule = null).findFilesForFacadeByPackage(packageFqName)
    }

    override fun findFilesForScript(scriptFqName: FqName, searchScope: GlobalSearchScope): Collection<KtScript> {
        return project.createDeclarationProvider(searchScope, contextualModule = null).findFilesForScript(scriptFqName)
    }

    private fun FqName.toClassIdSequence(): Sequence<ClassId> {
        var currentName = shortNameOrSpecial()
        if (currentName.isSpecial) return emptySequence()
        var currentParent = parentOrNull() ?: return emptySequence()
        var currentRelativeName = currentName.asString()

        return sequence {
            while (true) {
                yield(ClassId(currentParent, FqName(currentRelativeName), isLocal = false))
                currentName = currentParent.shortNameOrSpecial()
                if (currentName.isSpecial) break
                currentParent = currentParent.parentOrNull() ?: break
                currentRelativeName = "${currentName.asString()}.$currentRelativeName"
            }
        }
    }

    override fun findClassOrObjectDeclarations(fqName: FqName, searchScope: GlobalSearchScope): Collection<KtClassOrObject> =
        fqName.toClassIdSequence().flatMap {
            project.createDeclarationProvider(searchScope, contextualModule = null).getAllClassesByClassId(it)
        }
            .filter { it.isFromSourceOrLibraryBinary() }
            .toSet()

    override fun packageExists(fqName: FqName, scope: GlobalSearchScope): Boolean =
        project.createPackageProvider(scope).doesKotlinOnlyPackageExist(fqName)

    override fun getSubPackages(fqn: FqName, scope: GlobalSearchScope): Collection<FqName> =
        project.createPackageProvider(scope)
            .getKotlinOnlySubPackagesFqNames(fqn, nameFilter = { true })
            .map { fqn.child(it) }

    override fun createInstanceOfLightScript(script: KtScript): KtLightClass? {
        val module = script.getModuleIfSupportEnabled() ?: return null
        return SymbolLightClassForScript(script, module)
    }

    override fun KtFile.findModule(): KtModule? = getModuleIfSupportEnabled()

    override fun declarationLocation(file: KtFile): DeclarationLocation? = when (file.getModuleIfSupportEnabled()) {
        is KtSourceModule -> DeclarationLocation.ProjectSources
        is KtLibraryModule -> DeclarationLocation.LibraryClasses
        is KtLibrarySourceModule -> DeclarationLocation.LibrarySources
        else -> null
    }

    override fun createInstanceOfDecompiledLightClass(classOrObject: KtClassOrObject): KtLightClass? {
        return DecompiledLightClassesFactory.getLightClassForDecompiledClassOrObject(classOrObject, project)
    }

    override fun createInstanceOfLightClass(classOrObject: KtClassOrObject): KtLightClass? {
        val module = classOrObject.getModuleIfSupportEnabled() ?: return null
        return createSymbolLightClassNoCache(classOrObject, module)
    }

    override fun createInstanceOfDecompiledLightFacade(facadeFqName: FqName, files: List<KtFile>): KtLightClassForFacade? {
        return DecompiledLightClassesFactory.createLightFacadeForDecompiledKotlinFile(project, facadeFqName, files)
    }

    override fun projectWideOutOfBlockModificationTracker(): ModificationTracker {
        return project.createProjectWideOutOfBlockModificationTracker()
    }

    override fun outOfBlockModificationTracker(element: PsiElement): ModificationTracker {
        return project.createProjectWideOutOfBlockModificationTracker()
    }

    override fun librariesTracker(element: PsiElement): ModificationTracker {
        return project.createAllLibrariesModificationTracker()
    }

    override fun createInstanceOfLightFacade(facadeFqName: FqName, files: List<KtFile>): KtLightClassForFacade? {
        val module = files.first().getModuleIfSupportEnabled() ?: return null
        return createInstanceOfLightFacade(facadeFqName, module, files)
    }

    override fun createInstanceOfLightFacade(facadeFqName: FqName, module: KtModule, files: List<KtFile>): KtLightClassForFacade? {
        return SymbolLightClassForFacade(facadeFqName, files, module)
    }

    override val KtModule.contentSearchScope: GlobalSearchScope
        get() = GlobalSearchScope.union(
            buildList {
                add(contentScope)
                for (dependency in transitiveDependsOnDependencies) {
                    add(dependency.contentScope)
                }
            }
        )

    override fun facadeIsApplicable(module: KtModule, file: KtFile): Boolean =
        module.isFromSourceOrLibraryBinary() && module.isLightClassesEnabled()

    override fun getKotlinInternalClasses(fqName: FqName, scope: GlobalSearchScope): Collection<PsiClass> {
        val facadeKtFiles = project.createDeclarationProvider(scope, null).findInternalFilesForFacade(fqName)
        if (facadeKtFiles.isEmpty()) return emptyList()

        val partShortName = fqName.shortName().asString()
        val partClassFileShortName = "$partShortName.class"

        return facadeKtFiles.mapNotNull { facadeKtFile ->
            if (facadeKtFile is KtClsFile) {
                val partClassFile = facadeKtFile.virtualFile.parent.findChild(partClassFileShortName) ?: return@mapNotNull null
                val psiFile = facadeKtFile.manager.findFile(partClassFile) as? KtClsFile ?: facadeKtFile
                val javaClsClass = DecompiledLightClassesFactory.createClsJavaClassFromVirtualFile(
                    mirrorFile = psiFile,
                    classFile = partClassFile,
                    correspondingClassOrObject = null,
                    project = project,
                ) ?: return@mapNotNull null

                KtLightClassForDecompiledDeclaration(javaClsClass, javaClsClass.parent, psiFile, null)
            } else {
                null
            }
        }
    }

    override fun findFilesForFacade(facadeFqName: FqName, searchScope: GlobalSearchScope): Collection<KtFile> {
        return project.createDeclarationProvider(searchScope, contextualModule = null).findFilesForFacade(facadeFqName)
    }

    override fun getFakeLightClass(classOrObject: KtClassOrObject): KtFakeLightClass = SymbolBasedFakeLightClass(classOrObject)

    private fun KtElement.isFromSourceOrLibraryBinary(): Boolean = getModuleIfSupportEnabled()?.isFromSourceOrLibraryBinary() == true

    private fun KtModule.isFromSourceOrLibraryBinary(): Boolean {
        return when (this) {
            is KtSourceModule -> true
            is KtLibraryModule -> true
            is KtDanglingFileModule -> contextModule.isFromSourceOrLibraryBinary()
            else -> false
        }
    }
}

private fun KtModule.isLightClassesEnabled(): Boolean = platform.has<JvmPlatform>()
