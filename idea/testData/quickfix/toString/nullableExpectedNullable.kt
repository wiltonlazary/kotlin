// "Add safe '?.toString()' call" "true"
// ACTION: Add 'toString()' call
// ACTION: Add safe '?.toString()' call

fun foo() {
    bar(null as Any?<caret>)
}

fun bar(a: String?) {
}