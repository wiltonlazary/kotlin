// TARGET_BACKEND: JVM
// FULL_JDK
// WITH_STDLIB

// JVM_ABI_K1_K2_DIFF: KT-63864

val xsl = arrayListOf("a", "b", "c", "d")
val xs = xsl.asSequence()

fun box(): String {
    val s = StringBuilder()

    var cmeThrown = false
    try {
        for ((index, x) in xs.withIndex()) {
            s.append("$index:$x;")
            xsl.clear()
        }
    } catch (e: java.util.ConcurrentModificationException) {
        cmeThrown = true
    }

    if (!cmeThrown) return "Fail: CME should be thrown"

    val ss = s.toString()
    return if (ss == "0:a;") "OK" else "fail: '$ss'"
}