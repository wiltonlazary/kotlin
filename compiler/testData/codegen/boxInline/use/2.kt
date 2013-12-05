package test

public class Input : Closeable() {
    public fun data() : Int = 100
}
public  class Output : Closeable() {
    public fun doOutput(data: Int): Int = data
}

public open class Closeable {
    open public fun close() {}
}

public inline fun <T: Closeable, R> T.use(block: (T)-> R) : R {
    var closed = false
    try {
        return block(this)
    } catch (e: Exception) {
        closed = true
        try {
            this.close()
        } catch (closeException: Exception) {

        }
        throw e
    } finally {
        if (!closed) {
            this.close()
        }
    }
}


public fun Input.copyTo(output: Output): Long {
    return output.doOutput(this.data()).toLong()
}
