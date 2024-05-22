/*
 * Copyright 2010-2024 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.js.test.ir;

import com.intellij.testFramework.TestDataPath;
import org.jetbrains.kotlin.test.util.KtTestUtil;
import org.jetbrains.kotlin.test.TargetBackend;
import org.jetbrains.kotlin.test.TestMetadata;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.regex.Pattern;

/** This class is generated by {@link org.jetbrains.kotlin.generators.tests.GenerateJsTestsKt}. DO NOT MODIFY MANUALLY */
@SuppressWarnings("all")
@TestMetadata("compiler/testData/debug/localVariables")
@TestDataPath("$PROJECT_ROOT")
public class IrJsLocalVariableTestGenerated extends AbstractIrJsLocalVariableTest {
  @Test
  public void testAllFilesPresentInLocalVariables() {
    KtTestUtil.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("compiler/testData/debug/localVariables"), Pattern.compile("^(.+)\\.kt$"), null, TargetBackend.JS_IR, true, "inlineScopes");
  }

  @Test
  @TestMetadata("assignment.kt")
  public void testAssignment() {
    runTest("compiler/testData/debug/localVariables/assignment.kt");
  }

  @Test
  @TestMetadata("catchClause.kt")
  public void testCatchClause() {
    runTest("compiler/testData/debug/localVariables/catchClause.kt");
  }

  @Test
  @TestMetadata("copyFunction.kt")
  public void testCopyFunction() {
    runTest("compiler/testData/debug/localVariables/copyFunction.kt");
  }

  @Test
  @TestMetadata("directInvoke.kt")
  public void testDirectInvoke() {
    runTest("compiler/testData/debug/localVariables/directInvoke.kt");
  }

  @Test
  @TestMetadata("doWhile.kt")
  public void testDoWhile() {
    runTest("compiler/testData/debug/localVariables/doWhile.kt");
  }

  @Test
  @TestMetadata("emptyFun.kt")
  public void testEmptyFun() {
    runTest("compiler/testData/debug/localVariables/emptyFun.kt");
  }

  @Test
  @TestMetadata("forLoopMultiline.kt")
  public void testForLoopMultiline() {
    runTest("compiler/testData/debug/localVariables/forLoopMultiline.kt");
  }

  @Test
  @TestMetadata("inlineFunInObject.kt")
  public void testInlineFunInObject() {
    runTest("compiler/testData/debug/localVariables/inlineFunInObject.kt");
  }

  @Test
  @TestMetadata("inlineProperty.kt")
  public void testInlineProperty() {
    runTest("compiler/testData/debug/localVariables/inlineProperty.kt");
  }

  @Test
  @TestMetadata("jsCode.kt")
  public void testJsCode() {
    runTest("compiler/testData/debug/localVariables/jsCode.kt");
  }

  @Test
  @TestMetadata("lambdaInObject.kt")
  public void testLambdaInObject() {
    runTest("compiler/testData/debug/localVariables/lambdaInObject.kt");
  }

  @Test
  @TestMetadata("lambdaWithLambdaParameter.kt")
  public void testLambdaWithLambdaParameter() {
    runTest("compiler/testData/debug/localVariables/lambdaWithLambdaParameter.kt");
  }

  @Test
  @TestMetadata("localFun.kt")
  public void testLocalFun() {
    runTest("compiler/testData/debug/localVariables/localFun.kt");
  }

  @Test
  @TestMetadata("localFunUnused.kt")
  public void testLocalFunUnused() {
    runTest("compiler/testData/debug/localVariables/localFunUnused.kt");
  }

  @Test
  @TestMetadata("manyInlineFunsInObject.kt")
  public void testManyInlineFunsInObject() {
    runTest("compiler/testData/debug/localVariables/manyInlineFunsInObject.kt");
  }

  @Test
  @TestMetadata("tryFinally.kt")
  public void testTryFinally() {
    runTest("compiler/testData/debug/localVariables/tryFinally.kt");
  }

  @Test
  @TestMetadata("tryFinally10.kt")
  public void testTryFinally10() {
    runTest("compiler/testData/debug/localVariables/tryFinally10.kt");
  }

  @Test
  @TestMetadata("tryFinally11.kt")
  public void testTryFinally11() {
    runTest("compiler/testData/debug/localVariables/tryFinally11.kt");
  }

  @Test
  @TestMetadata("tryFinally12.kt")
  public void testTryFinally12() {
    runTest("compiler/testData/debug/localVariables/tryFinally12.kt");
  }

  @Test
  @TestMetadata("tryFinally13.kt")
  public void testTryFinally13() {
    runTest("compiler/testData/debug/localVariables/tryFinally13.kt");
  }

  @Test
  @TestMetadata("tryFinally14.kt")
  public void testTryFinally14() {
    runTest("compiler/testData/debug/localVariables/tryFinally14.kt");
  }

  @Test
  @TestMetadata("tryFinally15.kt")
  public void testTryFinally15() {
    runTest("compiler/testData/debug/localVariables/tryFinally15.kt");
  }

  @Test
  @TestMetadata("tryFinally16.kt")
  public void testTryFinally16() {
    runTest("compiler/testData/debug/localVariables/tryFinally16.kt");
  }

  @Test
  @TestMetadata("tryFinally17.kt")
  public void testTryFinally17() {
    runTest("compiler/testData/debug/localVariables/tryFinally17.kt");
  }

  @Test
  @TestMetadata("tryFinally2.kt")
  public void testTryFinally2() {
    runTest("compiler/testData/debug/localVariables/tryFinally2.kt");
  }

  @Test
  @TestMetadata("tryFinally3.kt")
  public void testTryFinally3() {
    runTest("compiler/testData/debug/localVariables/tryFinally3.kt");
  }

  @Test
  @TestMetadata("tryFinally4.kt")
  public void testTryFinally4() {
    runTest("compiler/testData/debug/localVariables/tryFinally4.kt");
  }

  @Test
  @TestMetadata("tryFinally5.kt")
  public void testTryFinally5() {
    runTest("compiler/testData/debug/localVariables/tryFinally5.kt");
  }

  @Test
  @TestMetadata("tryFinally6_1.kt")
  public void testTryFinally6_1() {
    runTest("compiler/testData/debug/localVariables/tryFinally6_1.kt");
  }

  @Test
  @TestMetadata("tryFinally6_2.kt")
  public void testTryFinally6_2() {
    runTest("compiler/testData/debug/localVariables/tryFinally6_2.kt");
  }

  @Test
  @TestMetadata("tryFinally7.kt")
  public void testTryFinally7() {
    runTest("compiler/testData/debug/localVariables/tryFinally7.kt");
  }

  @Test
  @TestMetadata("tryFinally8.kt")
  public void testTryFinally8() {
    runTest("compiler/testData/debug/localVariables/tryFinally8.kt");
  }

  @Test
  @TestMetadata("tryFinally9.kt")
  public void testTryFinally9() {
    runTest("compiler/testData/debug/localVariables/tryFinally9.kt");
  }

  @Test
  @TestMetadata("underscoreNames.kt")
  public void testUnderscoreNames() {
    runTest("compiler/testData/debug/localVariables/underscoreNames.kt");
  }

  @Nested
  @TestMetadata("compiler/testData/debug/localVariables/constructors")
  @TestDataPath("$PROJECT_ROOT")
  public class Constructors {
    @Test
    public void testAllFilesPresentInConstructors() {
      KtTestUtil.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("compiler/testData/debug/localVariables/constructors"), Pattern.compile("^(.+)\\.kt$"), null, TargetBackend.JS_IR, true);
    }

    @Test
    @TestMetadata("multipleConstructors.kt")
    public void testMultipleConstructors() {
      runTest("compiler/testData/debug/localVariables/constructors/multipleConstructors.kt");
    }

    @Test
    @TestMetadata("property.kt")
    public void testProperty() {
      runTest("compiler/testData/debug/localVariables/constructors/property.kt");
    }
  }

  @Nested
  @TestMetadata("compiler/testData/debug/localVariables/destructuring")
  @TestDataPath("$PROJECT_ROOT")
  public class Destructuring {
    @Test
    public void testAllFilesPresentInDestructuring() {
      KtTestUtil.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("compiler/testData/debug/localVariables/destructuring"), Pattern.compile("^(.+)\\.kt$"), null, TargetBackend.JS_IR, true);
    }

    @Test
    @TestMetadata("assignment.kt")
    public void testAssignment() {
      runTest("compiler/testData/debug/localVariables/destructuring/assignment.kt");
    }

    @Test
    @TestMetadata("assignmentCustomComponentNs.kt")
    public void testAssignmentCustomComponentNs() {
      runTest("compiler/testData/debug/localVariables/destructuring/assignmentCustomComponentNs.kt");
    }

    @Test
    @TestMetadata("assignmentCustomComponentNsMultiline.kt")
    public void testAssignmentCustomComponentNsMultiline() {
      runTest("compiler/testData/debug/localVariables/destructuring/assignmentCustomComponentNsMultiline.kt");
    }

    @Test
    @TestMetadata("assignmentMultiline.kt")
    public void testAssignmentMultiline() {
      runTest("compiler/testData/debug/localVariables/destructuring/assignmentMultiline.kt");
    }

    @Test
    @TestMetadata("assignmentUnderscoreNames.kt")
    public void testAssignmentUnderscoreNames() {
      runTest("compiler/testData/debug/localVariables/destructuring/assignmentUnderscoreNames.kt");
    }

    @Test
    @TestMetadata("assignmentUnderscoreNamesMultiline.kt")
    public void testAssignmentUnderscoreNamesMultiline() {
      runTest("compiler/testData/debug/localVariables/destructuring/assignmentUnderscoreNamesMultiline.kt");
    }

    @Test
    @TestMetadata("forLoop.kt")
    public void testForLoop() {
      runTest("compiler/testData/debug/localVariables/destructuring/forLoop.kt");
    }

    @Test
    @TestMetadata("forLoopMultiline.kt")
    public void testForLoopMultiline() {
      runTest("compiler/testData/debug/localVariables/destructuring/forLoopMultiline.kt");
    }

    @Test
    @TestMetadata("lambda.kt")
    public void testLambda() {
      runTest("compiler/testData/debug/localVariables/destructuring/lambda.kt");
    }

    @Test
    @TestMetadata("lambdaCustomComponentNs.kt")
    public void testLambdaCustomComponentNs() {
      runTest("compiler/testData/debug/localVariables/destructuring/lambdaCustomComponentNs.kt");
    }

    @Test
    @TestMetadata("lambdaCustomComponentNsMultiline.kt")
    public void testLambdaCustomComponentNsMultiline() {
      runTest("compiler/testData/debug/localVariables/destructuring/lambdaCustomComponentNsMultiline.kt");
    }

    @Test
    @TestMetadata("lambdaMultiline.kt")
    public void testLambdaMultiline() {
      runTest("compiler/testData/debug/localVariables/destructuring/lambdaMultiline.kt");
    }

    @Test
    @TestMetadata("lambdaMultipleDestructs.kt")
    public void testLambdaMultipleDestructs() {
      runTest("compiler/testData/debug/localVariables/destructuring/lambdaMultipleDestructs.kt");
    }
  }

  @Nested
  @TestMetadata("compiler/testData/debug/localVariables/receiverMangling")
  @TestDataPath("$PROJECT_ROOT")
  public class ReceiverMangling {
    @Test
    public void testAllFilesPresentInReceiverMangling() {
      KtTestUtil.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("compiler/testData/debug/localVariables/receiverMangling"), Pattern.compile("^(.+)\\.kt$"), null, TargetBackend.JS_IR, true);
    }

    @Test
    @TestMetadata("capturedThisField.kt")
    public void testCapturedThisField() {
      runTest("compiler/testData/debug/localVariables/receiverMangling/capturedThisField.kt");
    }

    @Test
    @TestMetadata("labeledThisParameterLabel.kt")
    public void testLabeledThisParameterLabel() {
      runTest("compiler/testData/debug/localVariables/receiverMangling/labeledThisParameterLabel.kt");
    }

    @Test
    @TestMetadata("lambdaWithExtensionReceiver.kt")
    public void testLambdaWithExtensionReceiver() {
      runTest("compiler/testData/debug/localVariables/receiverMangling/lambdaWithExtensionReceiver.kt");
    }

    @Test
    @TestMetadata("receiverParameter.kt")
    public void testReceiverParameter() {
      runTest("compiler/testData/debug/localVariables/receiverMangling/receiverParameter.kt");
    }

    @Test
    @TestMetadata("simple.kt")
    public void testSimple() {
      runTest("compiler/testData/debug/localVariables/receiverMangling/simple.kt");
    }

    @Test
    @TestMetadata("simpleCapturedReceiver.kt")
    public void testSimpleCapturedReceiver() {
      runTest("compiler/testData/debug/localVariables/receiverMangling/simpleCapturedReceiver.kt");
    }

    @Test
    @TestMetadata("simpleCapturedReceiverWithLabel.kt")
    public void testSimpleCapturedReceiverWithLabel() {
      runTest("compiler/testData/debug/localVariables/receiverMangling/simpleCapturedReceiverWithLabel.kt");
    }

    @Test
    @TestMetadata("simpleCapturedReceiverWithParenthesis.kt")
    public void testSimpleCapturedReceiverWithParenthesis() {
      runTest("compiler/testData/debug/localVariables/receiverMangling/simpleCapturedReceiverWithParenthesis.kt");
    }
  }

  @Nested
  @TestMetadata("compiler/testData/debug/localVariables/suspend")
  @TestDataPath("$PROJECT_ROOT")
  public class Suspend {
    @Test
    public void testAllFilesPresentInSuspend() {
      KtTestUtil.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("compiler/testData/debug/localVariables/suspend"), Pattern.compile("^(.+)\\.kt$"), null, TargetBackend.JS_IR, true);
    }

    @Test
    @TestMetadata("inlineLocalsStateMachineTransform.kt")
    public void testInlineLocalsStateMachineTransform() {
      runTest("compiler/testData/debug/localVariables/suspend/inlineLocalsStateMachineTransform.kt");
    }

    @Test
    @TestMetadata("localsStateMachineTransform.kt")
    public void testLocalsStateMachineTransform() {
      runTest("compiler/testData/debug/localVariables/suspend/localsStateMachineTransform.kt");
    }

    @Test
    @TestMetadata("mergeLvt.kt")
    public void testMergeLvt() {
      runTest("compiler/testData/debug/localVariables/suspend/mergeLvt.kt");
    }

    @Test
    @TestMetadata("nestedInsideSuspendUnintercepted.kt")
    public void testNestedInsideSuspendUnintercepted() {
      runTest("compiler/testData/debug/localVariables/suspend/nestedInsideSuspendUnintercepted.kt");
    }

    @Test
    @TestMetadata("nestedSuspendUnintercepted.kt")
    public void testNestedSuspendUnintercepted() {
      runTest("compiler/testData/debug/localVariables/suspend/nestedSuspendUnintercepted.kt");
    }

    @Test
    @TestMetadata("nestedSuspendUninterceptedWithDeepLambdaCall.kt")
    public void testNestedSuspendUninterceptedWithDeepLambdaCall() {
      runTest("compiler/testData/debug/localVariables/suspend/nestedSuspendUninterceptedWithDeepLambdaCall.kt");
    }

    @Test
    @TestMetadata("simple.kt")
    public void testSimple() {
      runTest("compiler/testData/debug/localVariables/suspend/simple.kt");
    }

    @Test
    @TestMetadata("underscoreNames.kt")
    public void testUnderscoreNames() {
      runTest("compiler/testData/debug/localVariables/suspend/underscoreNames.kt");
    }

    @Nested
    @TestMetadata("compiler/testData/debug/localVariables/suspend/completion")
    @TestDataPath("$PROJECT_ROOT")
    public class Completion {
      @Test
      public void testAllFilesPresentInCompletion() {
        KtTestUtil.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("compiler/testData/debug/localVariables/suspend/completion"), Pattern.compile("^(.+)\\.kt$"), null, TargetBackend.JS_IR, true);
      }

      @Test
      @TestMetadata("nonStaticSimple.kt")
      public void testNonStaticSimple() {
        runTest("compiler/testData/debug/localVariables/suspend/completion/nonStaticSimple.kt");
      }

      @Test
      @TestMetadata("nonStaticStateMachine.kt")
      public void testNonStaticStateMachine() {
        runTest("compiler/testData/debug/localVariables/suspend/completion/nonStaticStateMachine.kt");
      }

      @Test
      @TestMetadata("staticSimple.kt")
      public void testStaticSimple() {
        runTest("compiler/testData/debug/localVariables/suspend/completion/staticSimple.kt");
      }

      @Test
      @TestMetadata("staticSimpleReceiver.kt")
      public void testStaticSimpleReceiver() {
        runTest("compiler/testData/debug/localVariables/suspend/completion/staticSimpleReceiver.kt");
      }

      @Test
      @TestMetadata("staticStateMachine.kt")
      public void testStaticStateMachine() {
        runTest("compiler/testData/debug/localVariables/suspend/completion/staticStateMachine.kt");
      }

      @Test
      @TestMetadata("staticStateMachineReceiver.kt")
      public void testStaticStateMachineReceiver() {
        runTest("compiler/testData/debug/localVariables/suspend/completion/staticStateMachineReceiver.kt");
      }
    }
  }
}
