/*
 * Copyright 2010-2018 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license
 * that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.cfg;

import com.intellij.testFramework.TestDataPath;
import org.jetbrains.kotlin.test.JUnit3RunnerWithInners;
import org.jetbrains.kotlin.test.KotlinTestUtils;
import org.jetbrains.kotlin.test.TargetBackend;
import org.jetbrains.kotlin.test.TestMetadata;
import org.junit.runner.RunWith;

import java.io.File;
import java.util.regex.Pattern;

/** This class is generated by {@link org.jetbrains.kotlin.generators.tests.TestsPackage}. DO NOT MODIFY MANUALLY */
@SuppressWarnings("all")
@RunWith(JUnit3RunnerWithInners.class)
public class ControlFlowTestGenerated extends AbstractControlFlowTest {
    @TestMetadata("compiler/testData/cfg")
    @TestDataPath("$PROJECT_ROOT")
    @RunWith(JUnit3RunnerWithInners.class)
    public static class Cfg extends AbstractControlFlowTest {
        private void runTest(String testDataFilePath) throws Exception {
            KotlinTestUtils.runTest(this::doTest, TargetBackend.ANY, testDataFilePath);
        }

        public void testAllFilesPresentInCfg() throws Exception {
            KotlinTestUtils.assertAllTestsPresentByMetadata(this.getClass(), new File("compiler/testData/cfg"), Pattern.compile("^(.+)\\.kt$"), TargetBackend.ANY, true);
        }

        @TestMetadata("compiler/testData/cfg/arrays")
        @TestDataPath("$PROJECT_ROOT")
        @RunWith(JUnit3RunnerWithInners.class)
        public static class Arrays extends AbstractControlFlowTest {
            private void runTest(String testDataFilePath) throws Exception {
                KotlinTestUtils.runTest(this::doTest, TargetBackend.ANY, testDataFilePath);
            }

            public void testAllFilesPresentInArrays() throws Exception {
                KotlinTestUtils.assertAllTestsPresentByMetadata(this.getClass(), new File("compiler/testData/cfg/arrays"), Pattern.compile("^(.+)\\.kt$"), TargetBackend.ANY, true);
            }

            @TestMetadata("ArrayAccess.kt")
            public void testArrayAccess() throws Exception {
                runTest("compiler/testData/cfg/arrays/ArrayAccess.kt");
            }

            @TestMetadata("arrayAccessExpression.kt")
            public void testArrayAccessExpression() throws Exception {
                runTest("compiler/testData/cfg/arrays/arrayAccessExpression.kt");
            }

            @TestMetadata("arrayInc.kt")
            public void testArrayInc() throws Exception {
                runTest("compiler/testData/cfg/arrays/arrayInc.kt");
            }

            @TestMetadata("arrayIncUnresolved.kt")
            public void testArrayIncUnresolved() throws Exception {
                runTest("compiler/testData/cfg/arrays/arrayIncUnresolved.kt");
            }

            @TestMetadata("ArrayOfFunctions.kt")
            public void testArrayOfFunctions() throws Exception {
                runTest("compiler/testData/cfg/arrays/ArrayOfFunctions.kt");
            }

            @TestMetadata("arraySet.kt")
            public void testArraySet() throws Exception {
                runTest("compiler/testData/cfg/arrays/arraySet.kt");
            }

            @TestMetadata("arraySetNoRHS.kt")
            public void testArraySetNoRHS() throws Exception {
                runTest("compiler/testData/cfg/arrays/arraySetNoRHS.kt");
            }

            @TestMetadata("arraySetPlusAssign.kt")
            public void testArraySetPlusAssign() throws Exception {
                runTest("compiler/testData/cfg/arrays/arraySetPlusAssign.kt");
            }

            @TestMetadata("arraySetPlusAssignUnresolved.kt")
            public void testArraySetPlusAssignUnresolved() throws Exception {
                runTest("compiler/testData/cfg/arrays/arraySetPlusAssignUnresolved.kt");
            }

            @TestMetadata("arraySetUnresolved.kt")
            public void testArraySetUnresolved() throws Exception {
                runTest("compiler/testData/cfg/arrays/arraySetUnresolved.kt");
            }
        }

        @TestMetadata("compiler/testData/cfg/basic")
        @TestDataPath("$PROJECT_ROOT")
        @RunWith(JUnit3RunnerWithInners.class)
        public static class Basic extends AbstractControlFlowTest {
            private void runTest(String testDataFilePath) throws Exception {
                KotlinTestUtils.runTest(this::doTest, TargetBackend.ANY, testDataFilePath);
            }

            public void testAllFilesPresentInBasic() throws Exception {
                KotlinTestUtils.assertAllTestsPresentByMetadata(this.getClass(), new File("compiler/testData/cfg/basic"), Pattern.compile("^(.+)\\.kt$"), TargetBackend.ANY, true);
            }

            @TestMetadata("Basic.kt")
            public void testBasic() throws Exception {
                runTest("compiler/testData/cfg/basic/Basic.kt");
            }

            @TestMetadata("EmptyFunction.kt")
            public void testEmptyFunction() throws Exception {
                runTest("compiler/testData/cfg/basic/EmptyFunction.kt");
            }

            @TestMetadata("ShortFunction.kt")
            public void testShortFunction() throws Exception {
                runTest("compiler/testData/cfg/basic/ShortFunction.kt");
            }
        }

        @TestMetadata("compiler/testData/cfg/bugs")
        @TestDataPath("$PROJECT_ROOT")
        @RunWith(JUnit3RunnerWithInners.class)
        public static class Bugs extends AbstractControlFlowTest {
            private void runTest(String testDataFilePath) throws Exception {
                KotlinTestUtils.runTest(this::doTest, TargetBackend.ANY, testDataFilePath);
            }

            public void testAllFilesPresentInBugs() throws Exception {
                KotlinTestUtils.assertAllTestsPresentByMetadata(this.getClass(), new File("compiler/testData/cfg/bugs"), Pattern.compile("^(.+)\\.kt$"), TargetBackend.ANY, true);
            }

            @TestMetadata("functionalCallInEnumEntry.kt")
            public void testFunctionalCallInEnumEntry() throws Exception {
                runTest("compiler/testData/cfg/bugs/functionalCallInEnumEntry.kt");
            }

            @TestMetadata("jumpToOuterScope.kt")
            public void testJumpToOuterScope() throws Exception {
                runTest("compiler/testData/cfg/bugs/jumpToOuterScope.kt");
            }

            @TestMetadata("kt10105.kt")
            public void testKt10105() throws Exception {
                runTest("compiler/testData/cfg/bugs/kt10105.kt");
            }

            @TestMetadata("kt7761.kt")
            public void testKt7761() throws Exception {
                runTest("compiler/testData/cfg/bugs/kt7761.kt");
            }

            @TestMetadata("setWithTypeMismatch.kt")
            public void testSetWithTypeMismatch() throws Exception {
                runTest("compiler/testData/cfg/bugs/setWithTypeMismatch.kt");
            }

            @TestMetadata("unresolvedInvokeOnResolvedVar.kt")
            public void testUnresolvedInvokeOnResolvedVar() throws Exception {
                runTest("compiler/testData/cfg/bugs/unresolvedInvokeOnResolvedVar.kt");
            }
        }

        @TestMetadata("compiler/testData/cfg/controlStructures")
        @TestDataPath("$PROJECT_ROOT")
        @RunWith(JUnit3RunnerWithInners.class)
        public static class ControlStructures extends AbstractControlFlowTest {
            private void runTest(String testDataFilePath) throws Exception {
                KotlinTestUtils.runTest(this::doTest, TargetBackend.ANY, testDataFilePath);
            }

            public void testAllFilesPresentInControlStructures() throws Exception {
                KotlinTestUtils.assertAllTestsPresentByMetadata(this.getClass(), new File("compiler/testData/cfg/controlStructures"), Pattern.compile("^(.+)\\.kt$"), TargetBackend.ANY, true);
            }

            @TestMetadata("breakContinueInTryFinally.kt")
            public void testBreakContinueInTryFinally() throws Exception {
                runTest("compiler/testData/cfg/controlStructures/breakContinueInTryFinally.kt");
            }

            @TestMetadata("breakInsideLocal.kt")
            public void testBreakInsideLocal() throws Exception {
                runTest("compiler/testData/cfg/controlStructures/breakInsideLocal.kt");
            }

            @TestMetadata("continueInDoWhile.kt")
            public void testContinueInDoWhile() throws Exception {
                runTest("compiler/testData/cfg/controlStructures/continueInDoWhile.kt");
            }

            @TestMetadata("continueInFor.kt")
            public void testContinueInFor() throws Exception {
                runTest("compiler/testData/cfg/controlStructures/continueInFor.kt");
            }

            @TestMetadata("continueInWhile.kt")
            public void testContinueInWhile() throws Exception {
                runTest("compiler/testData/cfg/controlStructures/continueInWhile.kt");
            }

            @TestMetadata("Finally.kt")
            public void testFinally() throws Exception {
                runTest("compiler/testData/cfg/controlStructures/Finally.kt");
            }

            @TestMetadata("FinallyTestCopy.kt")
            public void testFinallyTestCopy() throws Exception {
                runTest("compiler/testData/cfg/controlStructures/FinallyTestCopy.kt");
            }

            @TestMetadata("For.kt")
            public void testFor() throws Exception {
                runTest("compiler/testData/cfg/controlStructures/For.kt");
            }

            @TestMetadata("If.kt")
            public void testIf() throws Exception {
                runTest("compiler/testData/cfg/controlStructures/If.kt");
            }

            @TestMetadata("incorrectIndex.kt")
            public void testIncorrectIndex() throws Exception {
                runTest("compiler/testData/cfg/controlStructures/incorrectIndex.kt");
            }

            @TestMetadata("InfiniteLoops.kt")
            public void testInfiniteLoops() throws Exception {
                runTest("compiler/testData/cfg/controlStructures/InfiniteLoops.kt");
            }

            @TestMetadata("localAndNonlocalReturnsWithFinally.kt")
            public void testLocalAndNonlocalReturnsWithFinally() throws Exception {
                runTest("compiler/testData/cfg/controlStructures/localAndNonlocalReturnsWithFinally.kt");
            }

            @TestMetadata("localFunctionInFinally.kt")
            public void testLocalFunctionInFinally() throws Exception {
                runTest("compiler/testData/cfg/controlStructures/localFunctionInFinally.kt");
            }

            @TestMetadata("OnlyWhileInFunctionBody.kt")
            public void testOnlyWhileInFunctionBody() throws Exception {
                runTest("compiler/testData/cfg/controlStructures/OnlyWhileInFunctionBody.kt");
            }

            @TestMetadata("returnsInWhen.kt")
            public void testReturnsInWhen() throws Exception {
                runTest("compiler/testData/cfg/controlStructures/returnsInWhen.kt");
            }

            @TestMetadata("whenConditions.kt")
            public void testWhenConditions() throws Exception {
                runTest("compiler/testData/cfg/controlStructures/whenConditions.kt");
            }

            @TestMetadata("whenExhaustive.kt")
            public void testWhenExhaustive() throws Exception {
                runTest("compiler/testData/cfg/controlStructures/whenExhaustive.kt");
            }
        }

        @TestMetadata("compiler/testData/cfg/conventions")
        @TestDataPath("$PROJECT_ROOT")
        @RunWith(JUnit3RunnerWithInners.class)
        public static class Conventions extends AbstractControlFlowTest {
            private void runTest(String testDataFilePath) throws Exception {
                KotlinTestUtils.runTest(this::doTest, TargetBackend.ANY, testDataFilePath);
            }

            public void testAllFilesPresentInConventions() throws Exception {
                KotlinTestUtils.assertAllTestsPresentByMetadata(this.getClass(), new File("compiler/testData/cfg/conventions"), Pattern.compile("^(.+)\\.kt$"), TargetBackend.ANY, true);
            }

            @TestMetadata("bothReceivers.kt")
            public void testBothReceivers() throws Exception {
                runTest("compiler/testData/cfg/conventions/bothReceivers.kt");
            }

            @TestMetadata("equals.kt")
            public void testEquals() throws Exception {
                runTest("compiler/testData/cfg/conventions/equals.kt");
            }

            @TestMetadata("incrementAtTheEnd.kt")
            public void testIncrementAtTheEnd() throws Exception {
                runTest("compiler/testData/cfg/conventions/incrementAtTheEnd.kt");
            }

            @TestMetadata("invoke.kt")
            public void testInvoke() throws Exception {
                runTest("compiler/testData/cfg/conventions/invoke.kt");
            }

            @TestMetadata("notEqual.kt")
            public void testNotEqual() throws Exception {
                runTest("compiler/testData/cfg/conventions/notEqual.kt");
            }
        }

        @TestMetadata("compiler/testData/cfg/deadCode")
        @TestDataPath("$PROJECT_ROOT")
        @RunWith(JUnit3RunnerWithInners.class)
        public static class DeadCode extends AbstractControlFlowTest {
            private void runTest(String testDataFilePath) throws Exception {
                KotlinTestUtils.runTest(this::doTest, TargetBackend.ANY, testDataFilePath);
            }

            public void testAllFilesPresentInDeadCode() throws Exception {
                KotlinTestUtils.assertAllTestsPresentByMetadata(this.getClass(), new File("compiler/testData/cfg/deadCode"), Pattern.compile("^(.+)\\.kt$"), TargetBackend.ANY, true);
            }

            @TestMetadata("DeadCode.kt")
            public void testDeadCode() throws Exception {
                runTest("compiler/testData/cfg/deadCode/DeadCode.kt");
            }

            @TestMetadata("notLocalReturn.kt")
            public void testNotLocalReturn() throws Exception {
                runTest("compiler/testData/cfg/deadCode/notLocalReturn.kt");
            }

            @TestMetadata("returnInElvis.kt")
            public void testReturnInElvis() throws Exception {
                runTest("compiler/testData/cfg/deadCode/returnInElvis.kt");
            }

            @TestMetadata("stringTemplate.kt")
            public void testStringTemplate() throws Exception {
                runTest("compiler/testData/cfg/deadCode/stringTemplate.kt");
            }

            @TestMetadata("throwInLambda.kt")
            public void testThrowInLambda() throws Exception {
                runTest("compiler/testData/cfg/deadCode/throwInLambda.kt");
            }
        }

        @TestMetadata("compiler/testData/cfg/declarations")
        @TestDataPath("$PROJECT_ROOT")
        @RunWith(JUnit3RunnerWithInners.class)
        public static class Declarations extends AbstractControlFlowTest {
            private void runTest(String testDataFilePath) throws Exception {
                KotlinTestUtils.runTest(this::doTest, TargetBackend.ANY, testDataFilePath);
            }

            public void testAllFilesPresentInDeclarations() throws Exception {
                KotlinTestUtils.assertAllTestsPresentByMetadata(this.getClass(), new File("compiler/testData/cfg/declarations"), Pattern.compile("^(.+)\\.kt$"), TargetBackend.ANY, true);
            }

            @TestMetadata("compiler/testData/cfg/declarations/classesAndObjects")
            @TestDataPath("$PROJECT_ROOT")
            @RunWith(JUnit3RunnerWithInners.class)
            public static class ClassesAndObjects extends AbstractControlFlowTest {
                private void runTest(String testDataFilePath) throws Exception {
                    KotlinTestUtils.runTest(this::doTest, TargetBackend.ANY, testDataFilePath);
                }

                public void testAllFilesPresentInClassesAndObjects() throws Exception {
                    KotlinTestUtils.assertAllTestsPresentByMetadata(this.getClass(), new File("compiler/testData/cfg/declarations/classesAndObjects"), Pattern.compile("^(.+)\\.kt$"), TargetBackend.ANY, true);
                }

                @TestMetadata("AnonymousInitializers.kt")
                public void testAnonymousInitializers() throws Exception {
                    runTest("compiler/testData/cfg/declarations/classesAndObjects/AnonymousInitializers.kt");
                }

                @TestMetadata("delegationByExpression.kt")
                public void testDelegationByExpression() throws Exception {
                    runTest("compiler/testData/cfg/declarations/classesAndObjects/delegationByExpression.kt");
                }

                @TestMetadata("delegationBySuperCall.kt")
                public void testDelegationBySuperCall() throws Exception {
                    runTest("compiler/testData/cfg/declarations/classesAndObjects/delegationBySuperCall.kt");
                }

                @TestMetadata("EnumEntryRefersCompanion.kt")
                public void testEnumEntryRefersCompanion() throws Exception {
                    runTest("compiler/testData/cfg/declarations/classesAndObjects/EnumEntryRefersCompanion.kt");
                }

                @TestMetadata("ObjectEnumQualifiers.kt")
                public void testObjectEnumQualifiers() throws Exception {
                    runTest("compiler/testData/cfg/declarations/classesAndObjects/ObjectEnumQualifiers.kt");
                }

                @TestMetadata("QualifierReceiverWithOthers.kt")
                public void testQualifierReceiverWithOthers() throws Exception {
                    runTest("compiler/testData/cfg/declarations/classesAndObjects/QualifierReceiverWithOthers.kt");
                }
            }

            @TestMetadata("compiler/testData/cfg/declarations/functionLiterals")
            @TestDataPath("$PROJECT_ROOT")
            @RunWith(JUnit3RunnerWithInners.class)
            public static class FunctionLiterals extends AbstractControlFlowTest {
                private void runTest(String testDataFilePath) throws Exception {
                    KotlinTestUtils.runTest(this::doTest, TargetBackend.ANY, testDataFilePath);
                }

                public void testAllFilesPresentInFunctionLiterals() throws Exception {
                    KotlinTestUtils.assertAllTestsPresentByMetadata(this.getClass(), new File("compiler/testData/cfg/declarations/functionLiterals"), Pattern.compile("^(.+)\\.kt$"), TargetBackend.ANY, true);
                }

                @TestMetadata("unusedFunctionLiteral.kt")
                public void testUnusedFunctionLiteral() throws Exception {
                    runTest("compiler/testData/cfg/declarations/functionLiterals/unusedFunctionLiteral.kt");
                }
            }

            @TestMetadata("compiler/testData/cfg/declarations/functions")
            @TestDataPath("$PROJECT_ROOT")
            @RunWith(JUnit3RunnerWithInners.class)
            public static class Functions extends AbstractControlFlowTest {
                private void runTest(String testDataFilePath) throws Exception {
                    KotlinTestUtils.runTest(this::doTest, TargetBackend.ANY, testDataFilePath);
                }

                public void testAllFilesPresentInFunctions() throws Exception {
                    KotlinTestUtils.assertAllTestsPresentByMetadata(this.getClass(), new File("compiler/testData/cfg/declarations/functions"), Pattern.compile("^(.+)\\.kt$"), TargetBackend.ANY, true);
                }

                @TestMetadata("anonymousFunctionInBlock.kt")
                public void testAnonymousFunctionInBlock() throws Exception {
                    runTest("compiler/testData/cfg/declarations/functions/anonymousFunctionInBlock.kt");
                }

                @TestMetadata("FailFunction.kt")
                public void testFailFunction() throws Exception {
                    runTest("compiler/testData/cfg/declarations/functions/FailFunction.kt");
                }

                @TestMetadata("functionAsExpression.kt")
                public void testFunctionAsExpression() throws Exception {
                    runTest("compiler/testData/cfg/declarations/functions/functionAsExpression.kt");
                }

                @TestMetadata("namedFunctionInBlock.kt")
                public void testNamedFunctionInBlock() throws Exception {
                    runTest("compiler/testData/cfg/declarations/functions/namedFunctionInBlock.kt");
                }

                @TestMetadata("typeParameter.kt")
                public void testTypeParameter() throws Exception {
                    runTest("compiler/testData/cfg/declarations/functions/typeParameter.kt");
                }
            }

            @TestMetadata("compiler/testData/cfg/declarations/local")
            @TestDataPath("$PROJECT_ROOT")
            @RunWith(JUnit3RunnerWithInners.class)
            public static class Local extends AbstractControlFlowTest {
                private void runTest(String testDataFilePath) throws Exception {
                    KotlinTestUtils.runTest(this::doTest, TargetBackend.ANY, testDataFilePath);
                }

                public void testAllFilesPresentInLocal() throws Exception {
                    KotlinTestUtils.assertAllTestsPresentByMetadata(this.getClass(), new File("compiler/testData/cfg/declarations/local"), Pattern.compile("^(.+)\\.kt$"), TargetBackend.ANY, true);
                }

                @TestMetadata("localClass.kt")
                public void testLocalClass() throws Exception {
                    runTest("compiler/testData/cfg/declarations/local/localClass.kt");
                }

                @TestMetadata("LocalDeclarations.kt")
                public void testLocalDeclarations() throws Exception {
                    runTest("compiler/testData/cfg/declarations/local/LocalDeclarations.kt");
                }

                @TestMetadata("localDelegatedVal.kt")
                public void testLocalDelegatedVal() throws Exception {
                    runTest("compiler/testData/cfg/declarations/local/localDelegatedVal.kt");
                }

                @TestMetadata("localFunction.kt")
                public void testLocalFunction() throws Exception {
                    runTest("compiler/testData/cfg/declarations/local/localFunction.kt");
                }

                @TestMetadata("localProperty.kt")
                public void testLocalProperty() throws Exception {
                    runTest("compiler/testData/cfg/declarations/local/localProperty.kt");
                }

                @TestMetadata("ObjectExpression.kt")
                public void testObjectExpression() throws Exception {
                    runTest("compiler/testData/cfg/declarations/local/ObjectExpression.kt");
                }
            }

            @TestMetadata("compiler/testData/cfg/declarations/multiDeclaration")
            @TestDataPath("$PROJECT_ROOT")
            @RunWith(JUnit3RunnerWithInners.class)
            public static class MultiDeclaration extends AbstractControlFlowTest {
                private void runTest(String testDataFilePath) throws Exception {
                    KotlinTestUtils.runTest(this::doTest, TargetBackend.ANY, testDataFilePath);
                }

                public void testAllFilesPresentInMultiDeclaration() throws Exception {
                    KotlinTestUtils.assertAllTestsPresentByMetadata(this.getClass(), new File("compiler/testData/cfg/declarations/multiDeclaration"), Pattern.compile("^(.+)\\.kt$"), TargetBackend.ANY, true);
                }

                @TestMetadata("MultiDecl.kt")
                public void testMultiDecl() throws Exception {
                    runTest("compiler/testData/cfg/declarations/multiDeclaration/MultiDecl.kt");
                }

                @TestMetadata("multiDeclarationWithError.kt")
                public void testMultiDeclarationWithError() throws Exception {
                    runTest("compiler/testData/cfg/declarations/multiDeclaration/multiDeclarationWithError.kt");
                }
            }

            @TestMetadata("compiler/testData/cfg/declarations/properties")
            @TestDataPath("$PROJECT_ROOT")
            @RunWith(JUnit3RunnerWithInners.class)
            public static class Properties extends AbstractControlFlowTest {
                private void runTest(String testDataFilePath) throws Exception {
                    KotlinTestUtils.runTest(this::doTest, TargetBackend.ANY, testDataFilePath);
                }

                public void testAllFilesPresentInProperties() throws Exception {
                    KotlinTestUtils.assertAllTestsPresentByMetadata(this.getClass(), new File("compiler/testData/cfg/declarations/properties"), Pattern.compile("^(.+)\\.kt$"), TargetBackend.ANY, true);
                }

                @TestMetadata("DelegatedProperty.kt")
                public void testDelegatedProperty() throws Exception {
                    runTest("compiler/testData/cfg/declarations/properties/DelegatedProperty.kt");
                }

                @TestMetadata("unreachableDelegation.kt")
                public void testUnreachableDelegation() throws Exception {
                    runTest("compiler/testData/cfg/declarations/properties/unreachableDelegation.kt");
                }
            }
        }

        @TestMetadata("compiler/testData/cfg/expressions")
        @TestDataPath("$PROJECT_ROOT")
        @RunWith(JUnit3RunnerWithInners.class)
        public static class Expressions extends AbstractControlFlowTest {
            private void runTest(String testDataFilePath) throws Exception {
                KotlinTestUtils.runTest(this::doTest, TargetBackend.ANY, testDataFilePath);
            }

            public void testAllFilesPresentInExpressions() throws Exception {
                KotlinTestUtils.assertAllTestsPresentByMetadata(this.getClass(), new File("compiler/testData/cfg/expressions"), Pattern.compile("^(.+)\\.kt$"), TargetBackend.ANY, true);
            }

            @TestMetadata("assignmentToThis.kt")
            public void testAssignmentToThis() throws Exception {
                runTest("compiler/testData/cfg/expressions/assignmentToThis.kt");
            }

            @TestMetadata("Assignments.kt")
            public void testAssignments() throws Exception {
                runTest("compiler/testData/cfg/expressions/Assignments.kt");
            }

            @TestMetadata("callableReferences.kt")
            public void testCallableReferences() throws Exception {
                runTest("compiler/testData/cfg/expressions/callableReferences.kt");
            }

            @TestMetadata("casts.kt")
            public void testCasts() throws Exception {
                runTest("compiler/testData/cfg/expressions/casts.kt");
            }

            @TestMetadata("chainedQualifiedExpression.kt")
            public void testChainedQualifiedExpression() throws Exception {
                runTest("compiler/testData/cfg/expressions/chainedQualifiedExpression.kt");
            }

            @TestMetadata("expressionAsFunction.kt")
            public void testExpressionAsFunction() throws Exception {
                runTest("compiler/testData/cfg/expressions/expressionAsFunction.kt");
            }

            @TestMetadata("incdec.kt")
            public void testIncdec() throws Exception {
                runTest("compiler/testData/cfg/expressions/incdec.kt");
            }

            @TestMetadata("invalidVariableCall.kt")
            public void testInvalidVariableCall() throws Exception {
                runTest("compiler/testData/cfg/expressions/invalidVariableCall.kt");
            }

            @TestMetadata("labeledExpression.kt")
            public void testLabeledExpression() throws Exception {
                runTest("compiler/testData/cfg/expressions/labeledExpression.kt");
            }

            @TestMetadata("LazyBooleans.kt")
            public void testLazyBooleans() throws Exception {
                runTest("compiler/testData/cfg/expressions/LazyBooleans.kt");
            }

            @TestMetadata("nothingExpr.kt")
            public void testNothingExpr() throws Exception {
                runTest("compiler/testData/cfg/expressions/nothingExpr.kt");
            }

            @TestMetadata("parenthesizedSelector.kt")
            public void testParenthesizedSelector() throws Exception {
                runTest("compiler/testData/cfg/expressions/parenthesizedSelector.kt");
            }

            @TestMetadata("propertySafeCall.kt")
            public void testPropertySafeCall() throws Exception {
                runTest("compiler/testData/cfg/expressions/propertySafeCall.kt");
            }

            @TestMetadata("qualifiedExpressionWithoutSelector.kt")
            public void testQualifiedExpressionWithoutSelector() throws Exception {
                runTest("compiler/testData/cfg/expressions/qualifiedExpressionWithoutSelector.kt");
            }

            @TestMetadata("ReturnFromExpression.kt")
            public void testReturnFromExpression() throws Exception {
                runTest("compiler/testData/cfg/expressions/ReturnFromExpression.kt");
            }

            @TestMetadata("thisExpression.kt")
            public void testThisExpression() throws Exception {
                runTest("compiler/testData/cfg/expressions/thisExpression.kt");
            }

            @TestMetadata("unresolvedCall.kt")
            public void testUnresolvedCall() throws Exception {
                runTest("compiler/testData/cfg/expressions/unresolvedCall.kt");
            }

            @TestMetadata("unresolvedCalls.kt")
            public void testUnresolvedCalls() throws Exception {
                runTest("compiler/testData/cfg/expressions/unresolvedCalls.kt");
            }

            @TestMetadata("unresolvedCallsWithReceiver.kt")
            public void testUnresolvedCallsWithReceiver() throws Exception {
                runTest("compiler/testData/cfg/expressions/unresolvedCallsWithReceiver.kt");
            }

            @TestMetadata("unresolvedProperty.kt")
            public void testUnresolvedProperty() throws Exception {
                runTest("compiler/testData/cfg/expressions/unresolvedProperty.kt");
            }

            @TestMetadata("unresolvedWriteLHS.kt")
            public void testUnresolvedWriteLHS() throws Exception {
                runTest("compiler/testData/cfg/expressions/unresolvedWriteLHS.kt");
            }

            @TestMetadata("unsupportedReturns.kt")
            public void testUnsupportedReturns() throws Exception {
                runTest("compiler/testData/cfg/expressions/unsupportedReturns.kt");
            }

            @TestMetadata("unusedExpressionSimpleName.kt")
            public void testUnusedExpressionSimpleName() throws Exception {
                runTest("compiler/testData/cfg/expressions/unusedExpressionSimpleName.kt");
            }
        }

        @TestMetadata("compiler/testData/cfg/functions")
        @TestDataPath("$PROJECT_ROOT")
        @RunWith(JUnit3RunnerWithInners.class)
        public static class Functions extends AbstractControlFlowTest {
            private void runTest(String testDataFilePath) throws Exception {
                KotlinTestUtils.runTest(this::doTest, TargetBackend.ANY, testDataFilePath);
            }

            public void testAllFilesPresentInFunctions() throws Exception {
                KotlinTestUtils.assertAllTestsPresentByMetadata(this.getClass(), new File("compiler/testData/cfg/functions"), Pattern.compile("^(.+)\\.kt$"), TargetBackend.ANY, true);
            }

            @TestMetadata("DefaultValuesForArguments.kt")
            public void testDefaultValuesForArguments() throws Exception {
                runTest("compiler/testData/cfg/functions/DefaultValuesForArguments.kt");
            }

            @TestMetadata("unmappedArgs.kt")
            public void testUnmappedArgs() throws Exception {
                runTest("compiler/testData/cfg/functions/unmappedArgs.kt");
            }
        }

        @TestMetadata("compiler/testData/cfg/secondaryConstructors")
        @TestDataPath("$PROJECT_ROOT")
        @RunWith(JUnit3RunnerWithInners.class)
        public static class SecondaryConstructors extends AbstractControlFlowTest {
            private void runTest(String testDataFilePath) throws Exception {
                KotlinTestUtils.runTest(this::doTest, TargetBackend.ANY, testDataFilePath);
            }

            public void testAllFilesPresentInSecondaryConstructors() throws Exception {
                KotlinTestUtils.assertAllTestsPresentByMetadata(this.getClass(), new File("compiler/testData/cfg/secondaryConstructors"), Pattern.compile("^(.+)\\.kt$"), TargetBackend.ANY, true);
            }

            @TestMetadata("withPrimary.kt")
            public void testWithPrimary() throws Exception {
                runTest("compiler/testData/cfg/secondaryConstructors/withPrimary.kt");
            }

            @TestMetadata("withPrimarySuper.kt")
            public void testWithPrimarySuper() throws Exception {
                runTest("compiler/testData/cfg/secondaryConstructors/withPrimarySuper.kt");
            }

            @TestMetadata("withReturn.kt")
            public void testWithReturn() throws Exception {
                runTest("compiler/testData/cfg/secondaryConstructors/withReturn.kt");
            }

            @TestMetadata("withoutPrimary.kt")
            public void testWithoutPrimary() throws Exception {
                runTest("compiler/testData/cfg/secondaryConstructors/withoutPrimary.kt");
            }

            @TestMetadata("withoutPrimarySuper.kt")
            public void testWithoutPrimarySuper() throws Exception {
                runTest("compiler/testData/cfg/secondaryConstructors/withoutPrimarySuper.kt");
            }
        }

        @TestMetadata("compiler/testData/cfg/tailCalls")
        @TestDataPath("$PROJECT_ROOT")
        @RunWith(JUnit3RunnerWithInners.class)
        public static class TailCalls extends AbstractControlFlowTest {
            private void runTest(String testDataFilePath) throws Exception {
                KotlinTestUtils.runTest(this::doTest, TargetBackend.ANY, testDataFilePath);
            }

            public void testAllFilesPresentInTailCalls() throws Exception {
                KotlinTestUtils.assertAllTestsPresentByMetadata(this.getClass(), new File("compiler/testData/cfg/tailCalls"), Pattern.compile("^(.+)\\.kt$"), TargetBackend.ANY, true);
            }

            @TestMetadata("finally.kt")
            public void testFinally() throws Exception {
                runTest("compiler/testData/cfg/tailCalls/finally.kt");
            }

            @TestMetadata("finallyWithReturn.kt")
            public void testFinallyWithReturn() throws Exception {
                runTest("compiler/testData/cfg/tailCalls/finallyWithReturn.kt");
            }

            @TestMetadata("sum.kt")
            public void testSum() throws Exception {
                runTest("compiler/testData/cfg/tailCalls/sum.kt");
            }

            @TestMetadata("try.kt")
            public void testTry() throws Exception {
                runTest("compiler/testData/cfg/tailCalls/try.kt");
            }

            @TestMetadata("tryCatchFinally.kt")
            public void testTryCatchFinally() throws Exception {
                runTest("compiler/testData/cfg/tailCalls/tryCatchFinally.kt");
            }
        }
    }

    @TestMetadata("compiler/testData/cfgWithStdLib")
    @TestDataPath("$PROJECT_ROOT")
    @RunWith(JUnit3RunnerWithInners.class)
    public static class CfgWithStdLib extends AbstractControlFlowTest {
        private void runTest(String testDataFilePath) throws Exception {
            KotlinTestUtils.runTest(this::doTestWithStdLib, TargetBackend.ANY, testDataFilePath);
        }

        public void testAllFilesPresentInCfgWithStdLib() throws Exception {
            KotlinTestUtils.assertAllTestsPresentByMetadata(this.getClass(), new File("compiler/testData/cfgWithStdLib"), Pattern.compile("^(.+)\\.kt$"), TargetBackend.ANY, true);
        }

        @TestMetadata("compiler/testData/cfgWithStdLib/contracts")
        @TestDataPath("$PROJECT_ROOT")
        @RunWith(JUnit3RunnerWithInners.class)
        public static class Contracts extends AbstractControlFlowTest {
            private void runTest(String testDataFilePath) throws Exception {
                KotlinTestUtils.runTest(this::doTestWithStdLib, TargetBackend.ANY, testDataFilePath);
            }

            public void testAllFilesPresentInContracts() throws Exception {
                KotlinTestUtils.assertAllTestsPresentByMetadata(this.getClass(), new File("compiler/testData/cfgWithStdLib/contracts"), Pattern.compile("^(.+)\\.kt$"), TargetBackend.ANY, true);
            }

            @TestMetadata("labeledReturns.kt")
            public void testLabeledReturns() throws Exception {
                runTest("compiler/testData/cfgWithStdLib/contracts/labeledReturns.kt");
            }

            @TestMetadata("nonReturningInlinedLambda.kt")
            public void testNonReturningInlinedLambda() throws Exception {
                runTest("compiler/testData/cfgWithStdLib/contracts/nonReturningInlinedLambda.kt");
            }

            @TestMetadata("returnsAndCalls.kt")
            public void testReturnsAndCalls() throws Exception {
                runTest("compiler/testData/cfgWithStdLib/contracts/returnsAndCalls.kt");
            }

            @TestMetadata("throwIfNotCalled.kt")
            public void testThrowIfNotCalled() throws Exception {
                runTest("compiler/testData/cfgWithStdLib/contracts/throwIfNotCalled.kt");
            }

            @TestMetadata("tryCatchFinally.kt")
            public void testTryCatchFinally() throws Exception {
                runTest("compiler/testData/cfgWithStdLib/contracts/tryCatchFinally.kt");
            }
        }
    }
}
