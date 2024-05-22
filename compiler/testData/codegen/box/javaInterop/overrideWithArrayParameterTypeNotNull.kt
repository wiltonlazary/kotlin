// TARGET_BACKEND: JVM
// FILE: J.java

import org.jetbrains.annotations.NotNull;

interface J {
    String foo(@NotNull String[] a);
}

// FILE: 1.kt

class Inv : J {
    override fun foo(a: Array<String>): String = a[0]
}

class Out : J {
    override fun foo(a: Array<out String>): String = a[0]
}

class Vararg : J {
    override fun foo(vararg a: String): String = a[0]
}

class InvNullableElement : J {
    override fun foo(a: Array<String?>): String = a[0] ?: "Fail"
}

fun box(): String {
    if (Inv().foo(arrayOf("OK")) != "OK") return "Fail Inv"
    if (Out().foo(arrayOf("OK")) != "OK") return "Fail Out"
    if (Vararg().foo("OK") != "OK") return "Fail Vararg"
    if (InvNullableElement().foo(arrayOf("OK")) != "OK") return "Fail InvNullableElement"
    return "OK"
}
