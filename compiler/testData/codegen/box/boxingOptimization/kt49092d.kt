fun foo(x: Any, y: Any) {}

val y = true
val z = 1

fun box(): String {
    var q = "Failed"
    foo(if (y) { q = "OK"; z } else "", return q)
}