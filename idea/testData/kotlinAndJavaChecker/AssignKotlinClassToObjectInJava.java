// KT-4355 IDEA complains when assigning Kotlin objects where java.lang.Object is expected
class AssignKotlinClassToObjectInJava {
    void test(KotlinTrait trait) {
        KotlinTrait trait1 = null;
        trait1.equals(trait1);
    }
}
