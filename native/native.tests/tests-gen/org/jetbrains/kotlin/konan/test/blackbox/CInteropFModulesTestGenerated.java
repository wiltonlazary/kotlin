/*
 * Copyright 2010-2024 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.konan.test.blackbox;

import com.intellij.testFramework.TestDataPath;
import org.jetbrains.kotlin.test.util.KtTestUtil;
import org.jetbrains.kotlin.test.TestMetadata;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.regex.Pattern;

/** This class is generated by {@link org.jetbrains.kotlin.generators.tests.GenerateNativeTestsKt}. DO NOT MODIFY MANUALLY */
@SuppressWarnings("all")
public class CInteropFModulesTestGenerated extends AbstractNativeCInteropFModulesTest {
  @Nested
  @TestMetadata("native/native.tests/testData/CInterop/simple/simpleDefs")
  @TestDataPath("$PROJECT_ROOT")
  public class SimpleDefs {
    @Test
    public void testAllFilesPresentInSimpleDefs() {
      KtTestUtil.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("native/native.tests/testData/CInterop/simple/simpleDefs"), Pattern.compile("^([^_](.+))$"), null, false);
    }

    @Test
    @TestMetadata("filterA")
    public void testFilterA() {
      runTest("native/native.tests/testData/CInterop/simple/simpleDefs/filterA/");
    }

    @Test
    @TestMetadata("filterAB")
    public void testFilterAB() {
      runTest("native/native.tests/testData/CInterop/simple/simpleDefs/filterAB/");
    }

    @Test
    @TestMetadata("filterABC")
    public void testFilterABC() {
      runTest("native/native.tests/testData/CInterop/simple/simpleDefs/filterABC/");
    }

    @Test
    @TestMetadata("filterAC")
    public void testFilterAC() {
      runTest("native/native.tests/testData/CInterop/simple/simpleDefs/filterAC/");
    }

    @Test
    @TestMetadata("filterB")
    public void testFilterB() {
      runTest("native/native.tests/testData/CInterop/simple/simpleDefs/filterB/");
    }

    @Test
    @TestMetadata("filterBC")
    public void testFilterBC() {
      runTest("native/native.tests/testData/CInterop/simple/simpleDefs/filterBC/");
    }

    @Test
    @TestMetadata("filterC")
    public void testFilterC() {
      runTest("native/native.tests/testData/CInterop/simple/simpleDefs/filterC/");
    }

    @Test
    @TestMetadata("full")
    public void testFull() {
      runTest("native/native.tests/testData/CInterop/simple/simpleDefs/full/");
    }

    @Test
    @TestMetadata("modulesA")
    public void testModulesA() {
      runTest("native/native.tests/testData/CInterop/simple/simpleDefs/modulesA/");
    }

    @Test
    @TestMetadata("modulesAB")
    public void testModulesAB() {
      runTest("native/native.tests/testData/CInterop/simple/simpleDefs/modulesAB/");
    }

    @Test
    @TestMetadata("modulesB")
    public void testModulesB() {
      runTest("native/native.tests/testData/CInterop/simple/simpleDefs/modulesB/");
    }
  }

  @Nested
  @TestMetadata("native/native.tests/testData/CInterop/framework/frameworkDefs")
  @TestDataPath("$PROJECT_ROOT")
  public class FrameworkDefs {
    @Test
    public void testAllFilesPresentInFrameworkDefs() {
      KtTestUtil.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("native/native.tests/testData/CInterop/framework/frameworkDefs"), Pattern.compile("^([^_](.+))$"), null, false);
    }

    @Test
    @TestMetadata("childImportFModules")
    public void testChildImportFModules() {
      runTest("native/native.tests/testData/CInterop/framework/frameworkDefs/childImportFModules/");
    }

    @Test
    @TestMetadata("excludePod1")
    public void testExcludePod1() {
      runTest("native/native.tests/testData/CInterop/framework/frameworkDefs/excludePod1/");
    }

    @Test
    @TestMetadata("excludePod1Umbrella")
    public void testExcludePod1Umbrella() {
      runTest("native/native.tests/testData/CInterop/framework/frameworkDefs/excludePod1Umbrella/");
    }

    @Test
    @TestMetadata("explicitSubmodule")
    public void testExplicitSubmodule() {
      runTest("native/native.tests/testData/CInterop/framework/frameworkDefs/explicitSubmodule/");
    }

    @Test
    @TestMetadata("filterPod1")
    public void testFilterPod1() {
      runTest("native/native.tests/testData/CInterop/framework/frameworkDefs/filterPod1/");
    }

    @Test
    @TestMetadata("filterPod1A")
    public void testFilterPod1A() {
      runTest("native/native.tests/testData/CInterop/framework/frameworkDefs/filterPod1A/");
    }

    @Test
    @TestMetadata("filterPod1Umbrella")
    public void testFilterPod1Umbrella() {
      runTest("native/native.tests/testData/CInterop/framework/frameworkDefs/filterPod1Umbrella/");
    }

    @Test
    @TestMetadata("filterPod1UmbrellaPod1A")
    public void testFilterPod1UmbrellaPod1A() {
      runTest("native/native.tests/testData/CInterop/framework/frameworkDefs/filterPod1UmbrellaPod1A/");
    }

    @Test
    @TestMetadata("forwardEnum")
    public void testForwardEnum() {
      runTest("native/native.tests/testData/CInterop/framework/frameworkDefs/forwardEnum/");
    }

    @Test
    @TestMetadata("full")
    public void testFull() {
      runTest("native/native.tests/testData/CInterop/framework/frameworkDefs/full/");
    }

    @Test
    @TestMetadata("importsAngleAngle")
    public void testImportsAngleAngle() {
      runTest("native/native.tests/testData/CInterop/framework/frameworkDefs/importsAngleAngle/");
    }

    @Test
    @TestMetadata("importsAngleQuote")
    public void testImportsAngleQuote() {
      runTest("native/native.tests/testData/CInterop/framework/frameworkDefs/importsAngleQuote/");
    }

    @Test
    @TestMetadata("importsQuoteAngle")
    public void testImportsQuoteAngle() {
      runTest("native/native.tests/testData/CInterop/framework/frameworkDefs/importsQuoteAngle/");
    }

    @Test
    @TestMetadata("importsQuoteQuote")
    public void testImportsQuoteQuote() {
      runTest("native/native.tests/testData/CInterop/framework/frameworkDefs/importsQuoteQuote/");
    }

    @Test
    @TestMetadata("modulesPod1")
    public void testModulesPod1() {
      runTest("native/native.tests/testData/CInterop/framework/frameworkDefs/modulesPod1/");
    }

    @Test
    @TestMetadata("protocolDefs")
    public void testProtocolDefs() {
      runTest("native/native.tests/testData/CInterop/framework/frameworkDefs/protocolDefs/");
    }

    @Test
    @TestMetadata("twoChildren")
    public void testTwoChildren() {
      runTest("native/native.tests/testData/CInterop/framework/frameworkDefs/twoChildren/");
    }

    @Test
    @TestMetadata("visitOtherModules")
    public void testVisitOtherModules() {
      runTest("native/native.tests/testData/CInterop/framework/frameworkDefs/visitOtherModules/");
    }
  }

  @Nested
  @TestMetadata("native/native.tests/testData/CInterop/framework.macros/macrosDefs")
  @TestDataPath("$PROJECT_ROOT")
  public class MacrosDefs {
    @Test
    public void testAllFilesPresentInMacrosDefs() {
      KtTestUtil.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("native/native.tests/testData/CInterop/framework.macros/macrosDefs"), Pattern.compile("^([^_](.+))$"), null, false);
    }

    @Test
    @TestMetadata("modulesPod1")
    public void testModulesPod1() {
      runTest("native/native.tests/testData/CInterop/framework.macros/macrosDefs/modulesPod1/");
    }

    @Test
    @TestMetadata("myMacroType")
    public void testMyMacroType() {
      runTest("native/native.tests/testData/CInterop/framework.macros/macrosDefs/myMacroType/");
    }
  }

  @Nested
  @TestMetadata("native/native.tests/testData/CInterop/builtins/builtinsDefs")
  @TestDataPath("$PROJECT_ROOT")
  public class BuiltinsDefs {
    @Test
    public void testAllFilesPresentInBuiltinsDefs() {
      KtTestUtil.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("native/native.tests/testData/CInterop/builtins/builtinsDefs"), Pattern.compile("^([^_](.+))$"), null, false);
    }

    @Test
    @TestMetadata("filterA")
    public void testFilterA() {
      runTest("native/native.tests/testData/CInterop/builtins/builtinsDefs/filterA/");
    }

    @Test
    @TestMetadata("filterStdargH")
    public void testFilterStdargH() {
      runTest("native/native.tests/testData/CInterop/builtins/builtinsDefs/filterStdargH/");
    }

    @Test
    @TestMetadata("fullA")
    public void testFullA() {
      runTest("native/native.tests/testData/CInterop/builtins/builtinsDefs/fullA/");
    }

    @Test
    @TestMetadata("fullStdargH")
    public void testFullStdargH() {
      runTest("native/native.tests/testData/CInterop/builtins/builtinsDefs/fullStdargH/");
    }

    @Test
    @TestMetadata("modulesA")
    public void testModulesA() {
      runTest("native/native.tests/testData/CInterop/builtins/builtinsDefs/modulesA/");
    }
  }
}
