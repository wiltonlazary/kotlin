// FIR_IDENTICAL
// FILE: Bar.java

public class Bar {
    public static final int BAR = Foo.FOO + 1;
}

// FILE: Test.kt

class Foo {
    companion object {
        val FOO = Baz.BAZ + 1
    }
}

class Baz {
    companion object {
        val BAZ = Bar.BAR + 1
    }
}