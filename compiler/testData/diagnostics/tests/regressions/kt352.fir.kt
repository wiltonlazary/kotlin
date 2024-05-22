//KT-352 Function variable declaration type isn't checked inside a function body

package kt352

val f : (Any) -> Unit = <!INITIALIZER_TYPE_MISMATCH!>{  -> }<!>  //type mismatch

fun foo() {
    val f : (Any) -> Unit = <!INITIALIZER_TYPE_MISMATCH!>{ -> }<!>  //!!! no error
}

class A() {
    val f : (Any) -> Unit = <!INITIALIZER_TYPE_MISMATCH!>{ -> }<!>  //type mismatch
}

//more tests
val g : () -> Unit = { 42 }
val gFunction : () -> Unit = <!INITIALIZER_TYPE_MISMATCH!>fun(): Int = 1<!>

val h : () -> Unit = { doSmth() }

fun doSmth(): Int = 42
fun doSmth(a: String) {}

val testIt : (Any) -> Unit = {
    if (it is String) {
        doSmth(it)
    }
}
