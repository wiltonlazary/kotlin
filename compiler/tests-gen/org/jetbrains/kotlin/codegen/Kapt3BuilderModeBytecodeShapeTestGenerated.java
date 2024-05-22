/*
 * Copyright 2010-2024 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.codegen;

import com.intellij.testFramework.TestDataPath;
import org.jetbrains.kotlin.test.JUnit3RunnerWithInners;
import org.jetbrains.kotlin.test.KotlinTestUtils;
import org.jetbrains.kotlin.test.util.KtTestUtil;
import org.jetbrains.kotlin.test.TargetBackend;
import org.jetbrains.kotlin.test.TestMetadata;
import org.junit.runner.RunWith;

import java.io.File;
import java.util.regex.Pattern;

/** This class is generated by {@link org.jetbrains.kotlin.test.generators.GenerateCompilerTestsKt}. DO NOT MODIFY MANUALLY */
@SuppressWarnings("all")
@TestMetadata("compiler/testData/codegen/kapt")
@TestDataPath("$PROJECT_ROOT")
@RunWith(JUnit3RunnerWithInners.class)
public class Kapt3BuilderModeBytecodeShapeTestGenerated extends AbstractKapt3BuilderModeBytecodeShapeTest {
  private void runTest(String testDataFilePath) {
    KotlinTestUtils.runTest(this::doTest, TargetBackend.JVM, testDataFilePath);
  }

  public void testAllFilesPresentInKapt() {
    KtTestUtil.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("compiler/testData/codegen/kapt"), Pattern.compile("^(.+)\\.kt$"), null, TargetBackend.JVM, true);
  }

  @TestMetadata("dataClass.kt")
  public void testDataClass() {
    runTest("compiler/testData/codegen/kapt/dataClass.kt");
  }

  @TestMetadata("errorTypes.kt")
  public void testErrorTypes() {
    runTest("compiler/testData/codegen/kapt/errorTypes.kt");
  }

  @TestMetadata("innerClasses.kt")
  public void testInnerClasses() {
    runTest("compiler/testData/codegen/kapt/innerClasses.kt");
  }

  @TestMetadata("interfaceImpls.kt")
  public void testInterfaceImpls() {
    runTest("compiler/testData/codegen/kapt/interfaceImpls.kt");
  }

  @TestMetadata("jvmOverloads.kt")
  public void testJvmOverloads() {
    runTest("compiler/testData/codegen/kapt/jvmOverloads.kt");
  }

  @TestMetadata("lambdas.kt")
  public void testLambdas() {
    runTest("compiler/testData/codegen/kapt/lambdas.kt");
  }

  @TestMetadata("simple.kt")
  public void testSimple() {
    runTest("compiler/testData/codegen/kapt/simple.kt");
  }
}
