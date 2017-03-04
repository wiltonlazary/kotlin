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

package org.jetbrains.kotlin.idea.filters;

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
@TestMetadata("idea/testData/debugger/exceptionFilter")
@TestDataPath("$PROJECT_ROOT")
@RunWith(JUnit3RunnerWithInners.class)
public class KotlinExceptionFilterTestGenerated extends AbstractKotlinExceptionFilterTest {
    public void testAllFilesPresentInExceptionFilter() throws Exception {
        KotlinTestUtils.assertAllTestsPresentByMetadata(this.getClass(), new File("idea/testData/debugger/exceptionFilter"), Pattern.compile("^([^\\.]+)$"), TargetBackend.ANY, false);
    }

    @TestMetadata("breakpointReachedAt")
    public void testBreakpointReachedAt() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/testData/debugger/exceptionFilter/breakpointReachedAt/");
        doTest(fileName);
    }

    @TestMetadata("inlineFunCallInLibrary")
    public void testInlineFunCallInLibrary() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/testData/debugger/exceptionFilter/inlineFunCallInLibrary/");
        doTest(fileName);
    }

    @TestMetadata("inlineFunFromLibrary")
    public void testInlineFunFromLibrary() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/testData/debugger/exceptionFilter/inlineFunFromLibrary/");
        doTest(fileName);
    }

    @TestMetadata("inlineFunInnerClassFromLibrary")
    public void testInlineFunInnerClassFromLibrary() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/testData/debugger/exceptionFilter/inlineFunInnerClassFromLibrary/");
        doTest(fileName);
    }

    @TestMetadata("inlineFunctionAnotherFile")
    public void testInlineFunctionAnotherFile() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/testData/debugger/exceptionFilter/inlineFunctionAnotherFile/");
        doTest(fileName);
    }

    @TestMetadata("inlineFunctionAnotherFileWithSmapApplied")
    public void testInlineFunctionAnotherFileWithSmapApplied() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/testData/debugger/exceptionFilter/inlineFunctionAnotherFileWithSmapApplied/");
        doTest(fileName);
    }

    @TestMetadata("inlineFunctionInnerClass")
    public void testInlineFunctionInnerClass() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/testData/debugger/exceptionFilter/inlineFunctionInnerClass/");
        doTest(fileName);
    }

    @TestMetadata("inlineFunctionObjectLiteral")
    public void testInlineFunctionObjectLiteral() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/testData/debugger/exceptionFilter/inlineFunctionObjectLiteral/");
        doTest(fileName);
    }

    @TestMetadata("inlineFunctionSameFile")
    public void testInlineFunctionSameFile() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/testData/debugger/exceptionFilter/inlineFunctionSameFile/");
        doTest(fileName);
    }

    @TestMetadata("kotlinClass")
    public void testKotlinClass() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/testData/debugger/exceptionFilter/kotlinClass/");
        doTest(fileName);
    }

    @TestMetadata("kt2489")
    public void testKt2489() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/testData/debugger/exceptionFilter/kt2489/");
        doTest(fileName);
    }

    @TestMetadata("kt2489_2")
    public void testKt2489_2() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/testData/debugger/exceptionFilter/kt2489_2/");
        doTest(fileName);
    }

    @TestMetadata("librarySources1")
    public void testLibrarySources1() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/testData/debugger/exceptionFilter/librarySources1/");
        doTest(fileName);
    }

    @TestMetadata("librarySources2")
    public void testLibrarySources2() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/testData/debugger/exceptionFilter/librarySources2/");
        doTest(fileName);
    }

    @TestMetadata("librarySources3")
    public void testLibrarySources3() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/testData/debugger/exceptionFilter/librarySources3/");
        doTest(fileName);
    }

    @TestMetadata("librarySources4")
    public void testLibrarySources4() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/testData/debugger/exceptionFilter/librarySources4/");
        doTest(fileName);
    }

    @TestMetadata("multiSamePackage")
    public void testMultiSamePackage() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/testData/debugger/exceptionFilter/multiSamePackage/");
        doTest(fileName);
    }

    @TestMetadata("simple")
    public void testSimple() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/testData/debugger/exceptionFilter/simple/");
        doTest(fileName);
    }
}
