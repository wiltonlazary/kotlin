// FIR_IDENTICAL
fun x(): Boolean { return true }

fun y(): Boolean { return false }

public fun foo(p: String?): Int {
    do {
        if (y()) continue
        // We do not always reach this statement
        p!!.length
    } while (!x())
    // Here we have do while loop but p is still nullable due to continue before
    return p<!UNSAFE_CALL!>.<!>length
}
