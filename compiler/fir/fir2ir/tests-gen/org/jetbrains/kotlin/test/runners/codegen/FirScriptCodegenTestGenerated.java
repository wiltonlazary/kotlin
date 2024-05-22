/*
 * Copyright 2010-2024 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.test.runners.codegen;

import com.intellij.testFramework.TestDataPath;
import org.jetbrains.kotlin.test.util.KtTestUtil;
import org.jetbrains.kotlin.test.TargetBackend;
import org.jetbrains.kotlin.test.TestMetadata;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.regex.Pattern;

/** This class is generated by {@link org.jetbrains.kotlin.test.generators.GenerateCompilerTestsKt}. DO NOT MODIFY MANUALLY */
@SuppressWarnings("all")
@TestMetadata("compiler/testData/codegen/script")
@TestDataPath("$PROJECT_ROOT")
public class FirScriptCodegenTestGenerated extends AbstractFirScriptCodegenTest {
  @Test
  @TestMetadata("adder.kts")
  public void testAdder() {
    runTest("compiler/testData/codegen/script/adder.kts");
  }

  @Test
  public void testAllFilesPresentInScript() {
    KtTestUtil.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("compiler/testData/codegen/script"), Pattern.compile("^(.*)\\.kts?$"), Pattern.compile("^(.+)\\.(reversed|fir|ll)\\.kts?$"), TargetBackend.JVM_IR, true);
  }

  @Test
  @TestMetadata("classLiteralInsideFunction.kts")
  public void testClassLiteralInsideFunction() {
    runTest("compiler/testData/codegen/script/classLiteralInsideFunction.kts");
  }

  @Test
  @TestMetadata("destructuringDeclaration.kts")
  public void testDestructuringDeclaration() {
    runTest("compiler/testData/codegen/script/destructuringDeclaration.kts");
  }

  @Test
  @TestMetadata("destructuringDeclarationUnderscore.kts")
  public void testDestructuringDeclarationUnderscore() {
    runTest("compiler/testData/codegen/script/destructuringDeclarationUnderscore.kts");
  }

  @Test
  @TestMetadata("empty.kts")
  public void testEmpty() {
    runTest("compiler/testData/codegen/script/empty.kts");
  }

  @Test
  @TestMetadata("helloWorld.kts")
  public void testHelloWorld() {
    runTest("compiler/testData/codegen/script/helloWorld.kts");
  }

  @Test
  @TestMetadata("inline.kts")
  public void testInline() {
    runTest("compiler/testData/codegen/script/inline.kts");
  }

  @Test
  @TestMetadata("innerClass.kts")
  public void testInnerClass() {
    runTest("compiler/testData/codegen/script/innerClass.kts");
  }

  @Test
  @TestMetadata("kt20707.kts")
  public void testKt20707() {
    runTest("compiler/testData/codegen/script/kt20707.kts");
  }

  @Test
  @TestMetadata("kt22029.kts")
  public void testKt22029() {
    runTest("compiler/testData/codegen/script/kt22029.kts");
  }

  @Test
  @TestMetadata("kt48025.kts")
  public void testKt48025() {
    runTest("compiler/testData/codegen/script/kt48025.kts");
  }

  @Test
  @TestMetadata("localDelegatedProperty.kts")
  public void testLocalDelegatedProperty() {
    runTest("compiler/testData/codegen/script/localDelegatedProperty.kts");
  }

  @Test
  @TestMetadata("localDelegatedPropertyInLambda.kts")
  public void testLocalDelegatedPropertyInLambda() {
    runTest("compiler/testData/codegen/script/localDelegatedPropertyInLambda.kts");
  }

  @Test
  @TestMetadata("localDelegatedPropertyNoExplicitType.kts")
  public void testLocalDelegatedPropertyNoExplicitType() {
    runTest("compiler/testData/codegen/script/localDelegatedPropertyNoExplicitType.kts");
  }

  @Test
  @TestMetadata("localFunction.kts")
  public void testLocalFunction() {
    runTest("compiler/testData/codegen/script/localFunction.kts");
  }

  @Test
  @TestMetadata("outerCapture.kts")
  public void testOuterCapture() {
    runTest("compiler/testData/codegen/script/outerCapture.kts");
  }

  @Test
  @TestMetadata("parameter.kts")
  public void testParameter() {
    runTest("compiler/testData/codegen/script/parameter.kts");
  }

  @Test
  @TestMetadata("parameterArray.kts")
  public void testParameterArray() {
    runTest("compiler/testData/codegen/script/parameterArray.kts");
  }

  @Test
  @TestMetadata("parameterClosure.kts")
  public void testParameterClosure() {
    runTest("compiler/testData/codegen/script/parameterClosure.kts");
  }

  @Test
  @TestMetadata("parameterLong.kts")
  public void testParameterLong() {
    runTest("compiler/testData/codegen/script/parameterLong.kts");
  }

  @Test
  @TestMetadata("secondLevelFunction.kts")
  public void testSecondLevelFunction() {
    runTest("compiler/testData/codegen/script/secondLevelFunction.kts");
  }

  @Test
  @TestMetadata("secondLevelFunctionClosure.kts")
  public void testSecondLevelFunctionClosure() {
    runTest("compiler/testData/codegen/script/secondLevelFunctionClosure.kts");
  }

  @Test
  @TestMetadata("secondLevelVal.kts")
  public void testSecondLevelVal() {
    runTest("compiler/testData/codegen/script/secondLevelVal.kts");
  }

  @Test
  @TestMetadata("simpleClass.kts")
  public void testSimpleClass() {
    runTest("compiler/testData/codegen/script/simpleClass.kts");
  }

  @Test
  @TestMetadata("string.kts")
  public void testString() {
    runTest("compiler/testData/codegen/script/string.kts");
  }

  @Test
  @TestMetadata("topLevelFunction.kts")
  public void testTopLevelFunction() {
    runTest("compiler/testData/codegen/script/topLevelFunction.kts");
  }

  @Test
  @TestMetadata("topLevelFunctionClosure.kts")
  public void testTopLevelFunctionClosure() {
    runTest("compiler/testData/codegen/script/topLevelFunctionClosure.kts");
  }

  @Test
  @TestMetadata("topLevelLocalDelegatedProperty.kts")
  public void testTopLevelLocalDelegatedProperty() {
    runTest("compiler/testData/codegen/script/topLevelLocalDelegatedProperty.kts");
  }

  @Test
  @TestMetadata("topLevelPropertiesWithGetSet.kts")
  public void testTopLevelPropertiesWithGetSet() {
    runTest("compiler/testData/codegen/script/topLevelPropertiesWithGetSet.kts");
  }

  @Test
  @TestMetadata("topLevelProperty.kts")
  public void testTopLevelProperty() {
    runTest("compiler/testData/codegen/script/topLevelProperty.kts");
  }

  @Test
  @TestMetadata("topLevelPropertyWithProvideDelegate.kts")
  public void testTopLevelPropertyWithProvideDelegate() {
    runTest("compiler/testData/codegen/script/topLevelPropertyWithProvideDelegate.kts");
  }

  @Test
  @TestMetadata("topLevelTypealias.kts")
  public void testTopLevelTypealias() {
    runTest("compiler/testData/codegen/script/topLevelTypealias.kts");
  }

  @Test
  @TestMetadata("twoDestructuringDeclarations.kts")
  public void testTwoDestructuringDeclarations() {
    runTest("compiler/testData/codegen/script/twoDestructuringDeclarations.kts");
  }

  @Nested
  @TestMetadata("compiler/testData/codegen/script/scriptInstanceCapturing")
  @TestDataPath("$PROJECT_ROOT")
  public class ScriptInstanceCapturing {
    @Test
    public void testAllFilesPresentInScriptInstanceCapturing() {
      KtTestUtil.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("compiler/testData/codegen/script/scriptInstanceCapturing"), Pattern.compile("^(.*)\\.kts?$"), Pattern.compile("^(.+)\\.(reversed|fir|ll)\\.kts?$"), TargetBackend.JVM_IR, true);
    }

    @Test
    @TestMetadata("anonymousObjectCapturesProperty.kts")
    public void testAnonymousObjectCapturesProperty() {
      runTest("compiler/testData/codegen/script/scriptInstanceCapturing/anonymousObjectCapturesProperty.kts");
    }

    @Test
    @TestMetadata("classCapturesExtensionIndirect.kts")
    public void testClassCapturesExtensionIndirect() {
      runTest("compiler/testData/codegen/script/scriptInstanceCapturing/classCapturesExtensionIndirect.kts");
    }

    @Test
    @TestMetadata("classCapturesExtensionIndirect2x.kts")
    public void testClassCapturesExtensionIndirect2x() {
      runTest("compiler/testData/codegen/script/scriptInstanceCapturing/classCapturesExtensionIndirect2x.kts");
    }

    @Test
    @TestMetadata("classCapturesFunction.kts")
    public void testClassCapturesFunction() {
      runTest("compiler/testData/codegen/script/scriptInstanceCapturing/classCapturesFunction.kts");
    }

    @Test
    @TestMetadata("classCapturesProperty.kts")
    public void testClassCapturesProperty() {
      runTest("compiler/testData/codegen/script/scriptInstanceCapturing/classCapturesProperty.kts");
    }

    @Test
    @TestMetadata("classCapturesPropertyInStringTemplate.kts")
    public void testClassCapturesPropertyInStringTemplate() {
      runTest("compiler/testData/codegen/script/scriptInstanceCapturing/classCapturesPropertyInStringTemplate.kts");
    }

    @Test
    @TestMetadata("classCapturesPropertyIndirect.kts")
    public void testClassCapturesPropertyIndirect() {
      runTest("compiler/testData/codegen/script/scriptInstanceCapturing/classCapturesPropertyIndirect.kts");
    }

    @Test
    @TestMetadata("classCapturesPropertyIndirect2x.kts")
    public void testClassCapturesPropertyIndirect2x() {
      runTest("compiler/testData/codegen/script/scriptInstanceCapturing/classCapturesPropertyIndirect2x.kts");
    }

    @Test
    @TestMetadata("companionCapturesProperty.kts")
    public void testCompanionCapturesProperty() {
      runTest("compiler/testData/codegen/script/scriptInstanceCapturing/companionCapturesProperty.kts");
    }

    @Test
    @TestMetadata("enumCapturesProperty.kts")
    public void testEnumCapturesProperty() {
      runTest("compiler/testData/codegen/script/scriptInstanceCapturing/enumCapturesProperty.kts");
    }

    @Test
    @TestMetadata("enumEntryCapturesProperty.kts")
    public void testEnumEntryCapturesProperty() {
      runTest("compiler/testData/codegen/script/scriptInstanceCapturing/enumEntryCapturesProperty.kts");
    }

    @Test
    @TestMetadata("innerClassesHierarchyCaptureProperty.kts")
    public void testInnerClassesHierarchyCaptureProperty() {
      runTest("compiler/testData/codegen/script/scriptInstanceCapturing/innerClassesHierarchyCaptureProperty.kts");
    }

    @Test
    @TestMetadata("interfaceCapturesProperty.kts")
    public void testInterfaceCapturesProperty() {
      runTest("compiler/testData/codegen/script/scriptInstanceCapturing/interfaceCapturesProperty.kts");
    }

    @Test
    @TestMetadata("nestedAndOuterClassesCaptureProperty.kts")
    public void testNestedAndOuterClassesCaptureProperty() {
      runTest("compiler/testData/codegen/script/scriptInstanceCapturing/nestedAndOuterClassesCaptureProperty.kts");
    }

    @Test
    @TestMetadata("nestedClassCapturesProperty.kts")
    public void testNestedClassCapturesProperty() {
      runTest("compiler/testData/codegen/script/scriptInstanceCapturing/nestedClassCapturesProperty.kts");
    }

    @Test
    @TestMetadata("nestedInnerClassCapturesProperty.kts")
    public void testNestedInnerClassCapturesProperty() {
      runTest("compiler/testData/codegen/script/scriptInstanceCapturing/nestedInnerClassCapturesProperty.kts");
    }

    @Test
    @TestMetadata("nestedToObjectClassCapturesProperty.kts")
    public void testNestedToObjectClassCapturesProperty() {
      runTest("compiler/testData/codegen/script/scriptInstanceCapturing/nestedToObjectClassCapturesProperty.kts");
    }

    @Test
    @TestMetadata("objectCapturesProperty.kts")
    public void testObjectCapturesProperty() {
      runTest("compiler/testData/codegen/script/scriptInstanceCapturing/objectCapturesProperty.kts");
    }

    @Test
    @TestMetadata("objectCapturesPropertyIndirect.kts")
    public void testObjectCapturesPropertyIndirect() {
      runTest("compiler/testData/codegen/script/scriptInstanceCapturing/objectCapturesPropertyIndirect.kts");
    }

    @Test
    @TestMetadata("objectCapturesPropertyViaExtension.kts")
    public void testObjectCapturesPropertyViaExtension() {
      runTest("compiler/testData/codegen/script/scriptInstanceCapturing/objectCapturesPropertyViaExtension.kts");
    }
  }
}
