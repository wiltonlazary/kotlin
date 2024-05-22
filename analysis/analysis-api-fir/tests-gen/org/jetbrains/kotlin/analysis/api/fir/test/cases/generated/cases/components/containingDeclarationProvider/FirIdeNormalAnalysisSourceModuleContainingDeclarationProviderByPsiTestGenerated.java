/*
 * Copyright 2010-2024 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.analysis.api.fir.test.cases.generated.cases.components.containingDeclarationProvider;

import com.intellij.testFramework.TestDataPath;
import org.jetbrains.kotlin.test.util.KtTestUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.kotlin.analysis.api.fir.test.configurators.AnalysisApiFirTestConfiguratorFactory;
import org.jetbrains.kotlin.analysis.test.framework.test.configurators.AnalysisApiTestConfiguratorFactoryData;
import org.jetbrains.kotlin.analysis.test.framework.test.configurators.AnalysisApiTestConfigurator;
import org.jetbrains.kotlin.analysis.test.framework.test.configurators.TestModuleKind;
import org.jetbrains.kotlin.analysis.test.framework.test.configurators.FrontendKind;
import org.jetbrains.kotlin.analysis.test.framework.test.configurators.AnalysisSessionMode;
import org.jetbrains.kotlin.analysis.test.framework.test.configurators.AnalysisApiMode;
import org.jetbrains.kotlin.analysis.api.impl.base.test.cases.components.containingDeclarationProvider.AbstractContainingDeclarationProviderByPsiTest;
import org.jetbrains.kotlin.test.TestMetadata;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.regex.Pattern;

/** This class is generated by {@link org.jetbrains.kotlin.generators.tests.analysis.api.GenerateAnalysisApiTestsKt}. DO NOT MODIFY MANUALLY */
@SuppressWarnings("all")
@TestMetadata("analysis/analysis-api/testData/components/containingDeclarationProvider/containingDeclarationByPsi")
@TestDataPath("$PROJECT_ROOT")
public class FirIdeNormalAnalysisSourceModuleContainingDeclarationProviderByPsiTestGenerated extends AbstractContainingDeclarationProviderByPsiTest {
  @NotNull
  @Override
  public AnalysisApiTestConfigurator getConfigurator() {
    return AnalysisApiFirTestConfiguratorFactory.INSTANCE.createConfigurator(
      new AnalysisApiTestConfiguratorFactoryData(
        FrontendKind.Fir,
        TestModuleKind.Source,
        AnalysisSessionMode.Normal,
        AnalysisApiMode.Ide
      )
    );
  }

  @Test
  public void testAllFilesPresentInContainingDeclarationByPsi() {
    KtTestUtil.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("analysis/analysis-api/testData/components/containingDeclarationProvider/containingDeclarationByPsi"), Pattern.compile("^(.+)\\.kt$"), null, true);
  }

  @Test
  @TestMetadata("classes.kt")
  public void testClasses() {
    runTest("analysis/analysis-api/testData/components/containingDeclarationProvider/containingDeclarationByPsi/classes.kt");
  }

  @Test
  @TestMetadata("codeFragments.kt")
  public void testCodeFragments() {
    runTest("analysis/analysis-api/testData/components/containingDeclarationProvider/containingDeclarationByPsi/codeFragments.kt");
  }

  @Test
  @TestMetadata("deeplyNestedCode.kt")
  public void testDeeplyNestedCode() {
    runTest("analysis/analysis-api/testData/components/containingDeclarationProvider/containingDeclarationByPsi/deeplyNestedCode.kt");
  }

  @Test
  @TestMetadata("enums.kt")
  public void testEnums() {
    runTest("analysis/analysis-api/testData/components/containingDeclarationProvider/containingDeclarationByPsi/enums.kt");
  }

  @Test
  @TestMetadata("functions.kt")
  public void testFunctions() {
    runTest("analysis/analysis-api/testData/components/containingDeclarationProvider/containingDeclarationByPsi/functions.kt");
  }

  @Test
  @TestMetadata("localDeclarations.kt")
  public void testLocalDeclarations() {
    runTest("analysis/analysis-api/testData/components/containingDeclarationProvider/containingDeclarationByPsi/localDeclarations.kt");
  }

  @Test
  @TestMetadata("typeAliases.kt")
  public void testTypeAliases() {
    runTest("analysis/analysis-api/testData/components/containingDeclarationProvider/containingDeclarationByPsi/typeAliases.kt");
  }
}
