/*
 * Copyright 2010-2017 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jetbrains.kotlin.idea.refactoring.rename;

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
@TestMetadata("idea/testData/refactoring/renameMultiModule")
@TestDataPath("$PROJECT_ROOT")
@RunWith(JUnit3RunnerWithInners.class)
public class MultiModuleRenameTestGenerated extends AbstractMultiModuleRenameTest {
    public void testAllFilesPresentInRenameMultiModule() throws Exception {
        KotlinTestUtils.assertAllTestsPresentInSingleGeneratedClass(this.getClass(), new File("idea/testData/refactoring/renameMultiModule"), Pattern.compile("^(.+)\\.test$"), TargetBackend.ANY);
    }

    @TestMetadata("fileNotUnderSourceRootWithNamesakeUnderSourceRoot/fileNotUnderSourceRootWithNamesakeUnderSourceRoot.test")
    public void testFileNotUnderSourceRootWithNamesakeUnderSourceRoot_FileNotUnderSourceRootWithNamesakeUnderSourceRoot() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/testData/refactoring/renameMultiModule/fileNotUnderSourceRootWithNamesakeUnderSourceRoot/fileNotUnderSourceRootWithNamesakeUnderSourceRoot.test");
        doTest(fileName);
    }

    @TestMetadata("headersAndImplsByHeaderClass/headersAndImplsByHeaderClass.test")
    public void testHeadersAndImplsByHeaderClass_HeadersAndImplsByHeaderClass() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/testData/refactoring/renameMultiModule/headersAndImplsByHeaderClass/headersAndImplsByHeaderClass.test");
        doTest(fileName);
    }

    @TestMetadata("headersAndImplsByHeaderFun/headersAndImplsByHeaderFun.test")
    public void testHeadersAndImplsByHeaderFun_HeadersAndImplsByHeaderFun() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/testData/refactoring/renameMultiModule/headersAndImplsByHeaderFun/headersAndImplsByHeaderFun.test");
        doTest(fileName);
    }

    @TestMetadata("headersAndImplsByHeaderVal/headersAndImplsByHeaderVal.test")
    public void testHeadersAndImplsByHeaderVal_HeadersAndImplsByHeaderVal() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/testData/refactoring/renameMultiModule/headersAndImplsByHeaderVal/headersAndImplsByHeaderVal.test");
        doTest(fileName);
    }

    @TestMetadata("headersAndImplsByImplClass/headersAndImplsByImplClass.test")
    public void testHeadersAndImplsByImplClass_HeadersAndImplsByImplClass() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/testData/refactoring/renameMultiModule/headersAndImplsByImplClass/headersAndImplsByImplClass.test");
        doTest(fileName);
    }

    @TestMetadata("headersAndImplsByImplFun/headersAndImplsByImplFun.test")
    public void testHeadersAndImplsByImplFun_HeadersAndImplsByImplFun() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/testData/refactoring/renameMultiModule/headersAndImplsByImplFun/headersAndImplsByImplFun.test");
        doTest(fileName);
    }

    @TestMetadata("headersAndImplsByImplVal/headersAndImplsByImplVal.test")
    public void testHeadersAndImplsByImplVal_HeadersAndImplsByImplVal() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/testData/refactoring/renameMultiModule/headersAndImplsByImplVal/headersAndImplsByImplVal.test");
        doTest(fileName);
    }
}
