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

package org.jetbrains.kotlin.idea.codeInsight.generate;

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
@TestMetadata("idea/testData/codeInsight/generate/secondaryConstructors")
@TestDataPath("$PROJECT_ROOT")
@RunWith(JUnit3RunnerWithInners.class)
public class CodeInsightActionTestGenerated extends AbstractCodeInsightActionTest {
    public void testAllFilesPresentInSecondaryConstructors() throws Exception {
        KotlinTestUtils.assertAllTestsPresentByMetadata(this.getClass(), new File("idea/testData/codeInsight/generate/secondaryConstructors"), Pattern.compile("^(.+)\\.kt$"), TargetBackend.ANY, true);
    }

    @TestMetadata("empty.kt")
    public void testEmpty() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/testData/codeInsight/generate/secondaryConstructors/empty.kt");
        doTest(fileName);
    }

    @TestMetadata("emptyExists.kt")
    public void testEmptyExists() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/testData/codeInsight/generate/secondaryConstructors/emptyExists.kt");
        doTest(fileName);
    }

    @TestMetadata("javaSupers.kt")
    public void testJavaSupers() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/testData/codeInsight/generate/secondaryConstructors/javaSupers.kt");
        doTest(fileName);
    }

    @TestMetadata("javaSupersWithGenerics.kt")
    public void testJavaSupersWithGenerics() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/testData/codeInsight/generate/secondaryConstructors/javaSupersWithGenerics.kt");
        doTest(fileName);
    }

    @TestMetadata("primaryExists.kt")
    public void testPrimaryExists() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/testData/codeInsight/generate/secondaryConstructors/primaryExists.kt");
        doTest(fileName);
    }

    @TestMetadata("properties.kt")
    public void testProperties() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/testData/codeInsight/generate/secondaryConstructors/properties.kt");
        doTest(fileName);
    }

    @TestMetadata("propertiesWithSupers.kt")
    public void testPropertiesWithSupers() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/testData/codeInsight/generate/secondaryConstructors/propertiesWithSupers.kt");
        doTest(fileName);
    }

    @TestMetadata("supers.kt")
    public void testSupers() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/testData/codeInsight/generate/secondaryConstructors/supers.kt");
        doTest(fileName);
    }

    @TestMetadata("supersAllExist.kt")
    public void testSupersAllExist() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/testData/codeInsight/generate/secondaryConstructors/supersAllExist.kt");
        doTest(fileName);
    }

    @TestMetadata("supersSomeExist.kt")
    public void testSupersSomeExist() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/testData/codeInsight/generate/secondaryConstructors/supersSomeExist.kt");
        doTest(fileName);
    }

    @TestMetadata("supersWithGenerics.kt")
    public void testSupersWithGenerics() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/testData/codeInsight/generate/secondaryConstructors/supersWithGenerics.kt");
        doTest(fileName);
    }

    @TestMetadata("supersWithVarargs.kt")
    public void testSupersWithVarargs() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/testData/codeInsight/generate/secondaryConstructors/supersWithVarargs.kt");
        doTest(fileName);
    }
}
