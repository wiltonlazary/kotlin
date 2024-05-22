/*
 * Copyright 2010-2024 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.analysis.low.level.api.fir;

import com.intellij.testFramework.TestDataPath;
import org.jetbrains.kotlin.test.util.KtTestUtil;
import org.jetbrains.kotlin.test.TestMetadata;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.regex.Pattern;

/** This class is generated by {@link org.jetbrains.kotlin.generators.tests.analysis.api.GenerateAnalysisApiTestsKt}. DO NOT MODIFY MANUALLY */
@SuppressWarnings("all")
@TestMetadata("analysis/low-level-api-fir/testData/partialRawBuilder")
@TestDataPath("$PROJECT_ROOT")
public class OutOfContentRootPartialRawFirBuilderTestCaseGenerated extends AbstractOutOfContentRootPartialRawFirBuilderTestCase {
  @Test
  public void testAllFilesPresentInPartialRawBuilder() {
    KtTestUtil.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("analysis/low-level-api-fir/testData/partialRawBuilder"), Pattern.compile("^(.+)\\.kt$"), null, true);
  }

  @Test
  @TestMetadata("localFunction.kt")
  public void testLocalFunction() {
    runTest("analysis/low-level-api-fir/testData/partialRawBuilder/localFunction.kt");
  }

  @Test
  @TestMetadata("memberFunction.kt")
  public void testMemberFunction() {
    runTest("analysis/low-level-api-fir/testData/partialRawBuilder/memberFunction.kt");
  }

  @Test
  @TestMetadata("memberProperty.kt")
  public void testMemberProperty() {
    runTest("analysis/low-level-api-fir/testData/partialRawBuilder/memberProperty.kt");
  }

  @Test
  @TestMetadata("paramemtersCatching.kt")
  public void testParamemtersCatching() {
    runTest("analysis/low-level-api-fir/testData/partialRawBuilder/paramemtersCatching.kt");
  }

  @Test
  @TestMetadata("simpleFunction.kt")
  public void testSimpleFunction() {
    runTest("analysis/low-level-api-fir/testData/partialRawBuilder/simpleFunction.kt");
  }

  @Test
  @TestMetadata("simpleVal.kt")
  public void testSimpleVal() {
    runTest("analysis/low-level-api-fir/testData/partialRawBuilder/simpleVal.kt");
  }

  @Test
  @TestMetadata("simpleVar.kt")
  public void testSimpleVar() {
    runTest("analysis/low-level-api-fir/testData/partialRawBuilder/simpleVar.kt");
  }
}
