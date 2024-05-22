/*
 * Copyright 2010-2023 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.generators.tests.native.swift.sir

import org.jetbrains.kotlin.generators.generateTestGroupSuiteWithJUnit5
import org.jetbrains.kotlin.sir.bridge.AbstractKotlinSirBridgeTest
import org.jetbrains.kotlin.swiftexport.standalone.AbstractKlibBasedSwiftRunnerTest
import org.jetbrains.kotlin.swiftexport.standalone.AbstractSourceBasedSwiftRunnerTest


fun main() {
    System.setProperty("java.awt.headless", "true")
    generateTestGroupSuiteWithJUnit5 {
        testGroup(
            "native/swift/sir-compiler-bridge/tests-gen/",
            "native/swift/sir-compiler-bridge/testData"
        ) {
            testClass<AbstractKotlinSirBridgeTest>(
                suiteTestClassName = "SirCompilerBridgeTestGenerated"
            ) {
                model("", extension = null, recursive = false)
            }
        }
        testGroup(
            "native/swift/swift-export-standalone/tests-gen/",
            "native/swift/swift-export-standalone/testData"
        ) {
            testClass<AbstractSourceBasedSwiftRunnerTest>(
                suiteTestClassName = "SourceBasedSwiftExportRunnerTest"
            ) {
                model("", extension = null, recursive = false)
            }
            testClass<AbstractKlibBasedSwiftRunnerTest>(
                suiteTestClassName = "KlibBasedSwiftExportRunnerTest"
            ) {
                model("", extension = null, recursive = false, excludeDirs = listOf("source_based_only"))
            }
        }
    }
}
