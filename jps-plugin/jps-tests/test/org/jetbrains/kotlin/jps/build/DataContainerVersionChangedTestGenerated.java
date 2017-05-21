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

package org.jetbrains.kotlin.jps.build;

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
@TestMetadata("jps-plugin/testData/incremental/cacheVersionChanged")
@TestDataPath("$PROJECT_ROOT")
@RunWith(JUnit3RunnerWithInners.class)
public class DataContainerVersionChangedTestGenerated extends AbstractDataContainerVersionChangedTest {
    public void testAllFilesPresentInCacheVersionChanged() throws Exception {
        KotlinTestUtils.assertAllTestsPresentByMetadata(this.getClass(), new File("jps-plugin/testData/incremental/cacheVersionChanged"), Pattern.compile("^([^\\.]+)$"), TargetBackend.ANY, true);
    }

    @TestMetadata("clearedHasKotlin")
    public void testClearedHasKotlin() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("jps-plugin/testData/incremental/cacheVersionChanged/clearedHasKotlin/");
        doTest(fileName);
    }

    @TestMetadata("exportedModule")
    public void testExportedModule() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("jps-plugin/testData/incremental/cacheVersionChanged/exportedModule/");
        doTest(fileName);
    }

    @TestMetadata("javaOnlyModulesAreNotAffected")
    public void testJavaOnlyModulesAreNotAffected() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("jps-plugin/testData/incremental/cacheVersionChanged/javaOnlyModulesAreNotAffected/");
        doTest(fileName);
    }

    @TestMetadata("module1Modified")
    public void testModule1Modified() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("jps-plugin/testData/incremental/cacheVersionChanged/module1Modified/");
        doTest(fileName);
    }

    @TestMetadata("module2Modified")
    public void testModule2Modified() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("jps-plugin/testData/incremental/cacheVersionChanged/module2Modified/");
        doTest(fileName);
    }

    @TestMetadata("moduleWithConstantModified")
    public void testModuleWithConstantModified() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("jps-plugin/testData/incremental/cacheVersionChanged/moduleWithConstantModified/");
        doTest(fileName);
    }

    @TestMetadata("moduleWithInlineModified")
    public void testModuleWithInlineModified() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("jps-plugin/testData/incremental/cacheVersionChanged/moduleWithInlineModified/");
        doTest(fileName);
    }

    @TestMetadata("touchedFile")
    public void testTouchedFile() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("jps-plugin/testData/incremental/cacheVersionChanged/touchedFile/");
        doTest(fileName);
    }

    @TestMetadata("touchedOnlyJavaFile")
    public void testTouchedOnlyJavaFile() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("jps-plugin/testData/incremental/cacheVersionChanged/touchedOnlyJavaFile/");
        doTest(fileName);
    }

    @TestMetadata("untouchedFiles")
    public void testUntouchedFiles() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("jps-plugin/testData/incremental/cacheVersionChanged/untouchedFiles/");
        doTest(fileName);
    }

    @TestMetadata("withError")
    public void testWithError() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("jps-plugin/testData/incremental/cacheVersionChanged/withError/");
        doTest(fileName);
    }
}
