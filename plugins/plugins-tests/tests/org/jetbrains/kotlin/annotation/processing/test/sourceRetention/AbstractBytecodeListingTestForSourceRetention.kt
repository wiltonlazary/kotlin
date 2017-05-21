/*
 * Copyright 2010-2016 JetBrains s.r.o.
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

package org.jetbrains.kotlin.annotation.processing.test.sourceRetention

import org.jetbrains.kotlin.codegen.AbstractBytecodeListingTest
import org.jetbrains.kotlin.codegen.ClassBuilderFactories
import org.jetbrains.kotlin.codegen.ClassBuilderFactory

abstract class AbstractBytecodeListingTestForSourceRetention : AbstractBytecodeListingTest() {
    override fun getClassBuilderFactory(): ClassBuilderFactory {
        return if (getTestName(true).contains("withSource", ignoreCase = true))
            ClassBuilderFactories.TEST_WITH_SOURCE_RETENTION_ANNOTATIONS
        else
            ClassBuilderFactories.TEST
    }

    override fun verifyWithDex(): Boolean {
        return false
    }
}