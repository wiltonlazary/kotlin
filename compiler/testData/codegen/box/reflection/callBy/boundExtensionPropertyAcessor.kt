// TODO: muted automatically, investigate should it be ran for JS or not
// IGNORE_BACKEND: JS

// WITH_REFLECT

import kotlin.test.assertEquals

val String.plusK: String
    get() = this + "K"

fun box(): String =
        ("O"::plusK).getter.callBy(mapOf())