// COMPILATION_ERRORS
val property: Int
    get() = 10

fun foo() {
    property<<caret>Int>
}
