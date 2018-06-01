/*
 * Copyright 2010-2018 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license
 * that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.idea.refactoring.copy;

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
@TestMetadata("idea/testData/refactoring/copyMultiModule")
@TestDataPath("$PROJECT_ROOT")
@RunWith(JUnit3RunnerWithInners.class)
public class MultiModuleCopyTestGenerated extends AbstractMultiModuleCopyTest {
    private void runTest(String testDataFilePath) throws Exception {
        KotlinTestUtils.runTest(this::doTest, TargetBackend.ANY, testDataFilePath);
    }

    public void testAllFilesPresentInCopyMultiModule() throws Exception {
        KotlinTestUtils.assertAllTestsPresentInSingleGeneratedClass(this.getClass(), new File("idea/testData/refactoring/copyMultiModule"), Pattern.compile("^(.+)\\.test$"), TargetBackend.ANY);
    }

    @TestMetadata("fileNotUnderSourceRoot/fileNotUnderSourceRoot.test")
    public void testFileNotUnderSourceRoot_FileNotUnderSourceRoot() throws Exception {
        runTest("idea/testData/refactoring/copyMultiModule/fileNotUnderSourceRoot/fileNotUnderSourceRoot.test");
    }

    @TestMetadata("internalReferencesToAnotherModule2/internalReferencesToAnotherModule.test")
    public void testInternalReferencesToAnotherModule2_InternalReferencesToAnotherModule() throws Exception {
        runTest("idea/testData/refactoring/copyMultiModule/internalReferencesToAnotherModule2/internalReferencesToAnotherModule.test");
    }

    @TestMetadata("referencesToUnrelatedModule/referencesToUnrelatedModule.test")
    public void testReferencesToUnrelatedModule_ReferencesToUnrelatedModule() throws Exception {
        runTest("idea/testData/refactoring/copyMultiModule/referencesToUnrelatedModule/referencesToUnrelatedModule.test");
    }
}
