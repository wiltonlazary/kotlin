class Shape(var result: String) {

}

fun box(): String {
    var a : Shape? = Shape("fail");
    a?.result = "OK";

    return a!!.result
}