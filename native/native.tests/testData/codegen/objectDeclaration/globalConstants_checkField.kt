/*
 * Copyright 2010-2021 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

import kotlin.test.*
import kotlin.native.internal.*

object ClassWithField {
    val x = 4
}

fun box(): String {
    assertFalse(ClassWithField.isPermanent())

    return "OK"
}
