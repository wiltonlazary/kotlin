import A.Companion.run

open class Action<T> {
    fun run(t: T){}
}

open class A {
    companion object : Action<String>() {
    }
}

class B : A() {
    fun foo() {
        run("")
    }
}