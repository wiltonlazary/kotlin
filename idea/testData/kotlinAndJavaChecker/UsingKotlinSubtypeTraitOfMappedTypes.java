// KT-4361 Can't assign traits object to Java class, if trait is a subclass of Java class that is wrapped in Kotlin

class UsingKotlinSubtypeTraitOfMappedTypes {
    void test(NumberTrait kotlinNumberTrait) {
        Number number = kotlinNumberTrait;
        Object numberObject = kotlinNumberTrait;
    }
}