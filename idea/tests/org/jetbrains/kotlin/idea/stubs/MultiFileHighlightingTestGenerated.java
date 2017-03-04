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

package org.jetbrains.kotlin.idea.stubs;

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
@TestMetadata("idea/testData/multiFileHighlighting")
@TestDataPath("$PROJECT_ROOT")
@RunWith(JUnit3RunnerWithInners.class)
public class MultiFileHighlightingTestGenerated extends AbstractMultiFileHighlightingTest {
    public void testAllFilesPresentInMultiFileHighlighting() throws Exception {
        KotlinTestUtils.assertAllTestsPresentByMetadata(this.getClass(), new File("idea/testData/multiFileHighlighting"), Pattern.compile("^(.+)\\.kt$"), TargetBackend.ANY, false);
    }

    @TestMetadata("annotatedParameter.kt")
    public void testAnnotatedParameter() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/testData/multiFileHighlighting/annotatedParameter.kt");
        doTest(fileName);
    }

    @TestMetadata("copyResolveBeforeParams.kt")
    public void testCopyResolveBeforeParams() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/testData/multiFileHighlighting/copyResolveBeforeParams.kt");
        doTest(fileName);
    }

    @TestMetadata("delegatesReference.kt")
    public void testDelegatesReference() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/testData/multiFileHighlighting/delegatesReference.kt");
        doTest(fileName);
    }

    @TestMetadata("enumReference.kt")
    public void testEnumReference() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/testData/multiFileHighlighting/enumReference.kt");
        doTest(fileName);
    }

    @TestMetadata("referencesFunWithUnspecifiedType.kt")
    public void testReferencesFunWithUnspecifiedType() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/testData/multiFileHighlighting/referencesFunWithUnspecifiedType.kt");
        doTest(fileName);
    }

    @TestMetadata("topLevelMembersReference.kt")
    public void testTopLevelMembersReference() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/testData/multiFileHighlighting/topLevelMembersReference.kt");
        doTest(fileName);
    }
}
