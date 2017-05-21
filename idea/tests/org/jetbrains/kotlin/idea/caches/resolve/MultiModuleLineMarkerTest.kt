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

package org.jetbrains.kotlin.idea.caches.resolve

import org.jetbrains.kotlin.config.JvmTarget
import org.jetbrains.kotlin.config.TargetPlatformKind

class MultiModuleLineMarkerTest : AbstractMultiModuleLineMarkerTest() {

    fun testFromCommonToJvmHeader() {
        doMultiPlatformTest(TargetPlatformKind.Jvm[JvmTarget.JVM_1_6])
    }

    fun testFromCommonToJvmImpl() {
        doMultiPlatformTest(TargetPlatformKind.Jvm[JvmTarget.JVM_1_6])
    }

    fun testFromClassToAlias() {
        doMultiPlatformTest(TargetPlatformKind.Jvm[JvmTarget.JVM_1_6])
    }

    fun testWithOverloads() {
        doMultiPlatformTest(TargetPlatformKind.Jvm[JvmTarget.JVM_1_6])
    }
}