
// FILE: script.kts

val a = 42

class A

enum class E {
    V
}

object O {
    val v = 42
}

fun foo() = 42

// FILE: main.kt

val b =  a

val ca = A()

val ev = E.V

val ov = O.v

val rfoo = foo()
