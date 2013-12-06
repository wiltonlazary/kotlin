package test

public class Data()

public inline fun <T, R> T.use(block: (T)-> R) : R {
    return block(this)
}


