/*
 * Copyright 2010-2018 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license
 * that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.noarg;

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
@TestMetadata("plugins/noarg/noarg-cli/testData/box")
@TestDataPath("$PROJECT_ROOT")
@RunWith(JUnit3RunnerWithInners.class)
public class BlackBoxCodegenTestForNoArgGenerated extends AbstractBlackBoxCodegenTestForNoArg {
    private void runTest(String testDataFilePath) throws Exception {
        KotlinTestUtils.runTest(this::doTest, TargetBackend.JVM, testDataFilePath);
    }

    public void testAllFilesPresentInBox() throws Exception {
        KotlinTestUtils.assertAllTestsPresentByMetadata(this.getClass(), new File("plugins/noarg/noarg-cli/testData/box"), Pattern.compile("^(.+)\\.kt$"), TargetBackend.JVM, true);
    }

    @TestMetadata("initializers.kt")
    public void testInitializers() throws Exception {
        runTest("plugins/noarg/noarg-cli/testData/box/initializers.kt");
    }

    @TestMetadata("initializersWithoutInvokeInitializers.kt")
    public void testInitializersWithoutInvokeInitializers() throws Exception {
        runTest("plugins/noarg/noarg-cli/testData/box/initializersWithoutInvokeInitializers.kt");
    }

    @TestMetadata("kt18245.kt")
    public void testKt18245() throws Exception {
        runTest("plugins/noarg/noarg-cli/testData/box/kt18245.kt");
    }

    @TestMetadata("kt18667.kt")
    public void testKt18667() throws Exception {
        runTest("plugins/noarg/noarg-cli/testData/box/kt18667.kt");
    }

    @TestMetadata("kt18668.kt")
    public void testKt18668() throws Exception {
        runTest("plugins/noarg/noarg-cli/testData/box/kt18668.kt");
    }

    @TestMetadata("simple.kt")
    public void testSimple() throws Exception {
        runTest("plugins/noarg/noarg-cli/testData/box/simple.kt");
    }
}
