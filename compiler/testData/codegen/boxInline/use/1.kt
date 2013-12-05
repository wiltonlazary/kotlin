import test.*

fun test1() : Long  {
    val input = Input()
    return input.use<Input, Long>{
        val output = Output()
        output.use<Output,Long>{
            input.copyTo(output)
        }
    }
}


fun box(): String {

    if (test1() != 100.toLong()) return "test1: ${test1()}"

    return "OK"
}
