interface Foo1 {
    fun foo()
    fun bar()
    val str: String
}

interface Foo2 : Foo1 {
    fun foo(i: Int)
    fun bar(s: String)
    val isBoo: Boolean
}

abstract class Usage : Foo2 {
    override fun foo() {}
    override val s<caret>tr = ""
}
