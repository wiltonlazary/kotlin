// "Make 'Foo' data class" "false"
// ACTION: Create extension function 'Foo.component1'
// ACTION: Create extension function 'Foo.component2'
// ACTION: Create member function 'Foo.component1'
// ACTION: Create member function 'Foo.component2'
// ACTION: Put arguments on separate lines
// ERROR: Destructuring declaration initializer of type Foo must have a 'component1()' function
// ERROR: Destructuring declaration initializer of type Foo must have a 'component2()' function
class Foo(protected val bar: String, var baz: Int)

fun test() {
    var (bar, baz) = Foo("A", 1)<caret>
}