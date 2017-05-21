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

package org.jetbrains.kotlin.android.annotator;

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
@TestMetadata("idea/testData/android/gutterIcon")
@TestDataPath("$PROJECT_ROOT")
@RunWith(JUnit3RunnerWithInners.class)
public class AndroidGutterIconTestGenerated extends AbstractAndroidGutterIconTest {
    public void testAllFilesPresentInGutterIcon() throws Exception {
        KotlinTestUtils.assertAllTestsPresentByMetadata(this.getClass(), new File("idea/testData/android/gutterIcon"), Pattern.compile("^(.+)\\.kt$"), TargetBackend.ANY, true);
    }

    @TestMetadata("color.kt")
    public void testColor() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/testData/android/gutterIcon/color.kt");
        doTest(fileName);
    }

    @TestMetadata("drawable.kt")
    public void testDrawable() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/testData/android/gutterIcon/drawable.kt");
        doTest(fileName);
    }

    @TestMetadata("mipmap.kt")
    public void testMipmap() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/testData/android/gutterIcon/mipmap.kt");
        doTest(fileName);
    }

    @TestMetadata("systemColor.kt")
    public void testSystemColor() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/testData/android/gutterIcon/systemColor.kt");
        doTest(fileName);
    }

    @TestMetadata("systemDrawable.kt")
    public void testSystemDrawable() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/testData/android/gutterIcon/systemDrawable.kt");
        doTest(fileName);
    }

    @TestMetadata("idea/testData/android/gutterIcon/res")
    @TestDataPath("$PROJECT_ROOT")
    @RunWith(JUnit3RunnerWithInners.class)
    public static class Res extends AbstractAndroidGutterIconTest {
        public void testAllFilesPresentInRes() throws Exception {
            KotlinTestUtils.assertAllTestsPresentByMetadata(this.getClass(), new File("idea/testData/android/gutterIcon/res"), Pattern.compile("^(.+)\\.kt$"), TargetBackend.ANY, true);
        }
    }
}
