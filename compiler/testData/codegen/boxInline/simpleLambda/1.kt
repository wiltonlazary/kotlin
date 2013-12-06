import test.*

class Z {}

fun test1() : Int {
    val input = Z()
    return input.use<Z, Int>{
        100
    }
}


fun box(): String {

    val result = test1()
    if (result != 100) return "test1: ${result}"

    return "OK"
}
