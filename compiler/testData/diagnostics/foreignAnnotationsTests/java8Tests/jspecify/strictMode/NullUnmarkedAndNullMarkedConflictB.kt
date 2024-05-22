// FIR_IDENTICAL
// JSPECIFY_STATE: strict
// DIAGNOSTICS: -UNUSED_PARAMETER

// FILE: conflictinglyannotatedpackage/package-info.java

@NullMarked
@NullUnmarked
package conflictinglyannotatedpackage;

import org.jspecify.annotations.*;

// FILE: conflictinglyannotatedpackage/UnannotatedType.java

package conflictinglyannotatedpackage;

public interface UnannotatedType {
    public String unannotatedProduce();
    public void unannotatedConsume(String arg);
}

// FILE: unannotatedpackage/ConflictinglyAnnotatedType.java

package unannotatedpackage;

import org.jspecify.annotations.*;

@NullMarked
@NullUnmarked
public interface ConflictinglyAnnotatedType {
    public String unannotatedProduce();
    public void unannotatedConsume(String arg);
}

// FILE: unannotatedpackage/UnannotatedType.java

package unannotatedpackage;

import org.jspecify.annotations.*;

public interface UnannotatedType {
    @NullMarked
    @NullUnmarked
    public String conflictinglyAnnotatedProduce();
    @NullMarked
    @NullUnmarked
    public void conflictinglyAnnotatedConsume(String arg);
}

// FILE: unannotatedpackage/UnannotatedTypeWithConflictinglyAnnotatedConstructor.java

package unannotatedpackage;

import org.jspecify.annotations.*;

public class UnannotatedTypeWithConflictinglyAnnotatedConstructor {
    @NullMarked
    @NullUnmarked
    public UnannotatedTypeWithConflictinglyAnnotatedConstructor(String arg) {}
}

// FILE: kotlin.kt

interface TestA: conflictinglyannotatedpackage.UnannotatedType {
    override fun unannotatedProduce(): String?
}

interface TestB: unannotatedpackage.ConflictinglyAnnotatedType {
    override fun unannotatedProduce(): String?
}

interface TestC: unannotatedpackage.UnannotatedType {
    override fun conflictinglyAnnotatedProduce(): String?
}

fun test(
    a: conflictinglyannotatedpackage.UnannotatedType,
    b: unannotatedpackage.ConflictinglyAnnotatedType,
    c: unannotatedpackage.UnannotatedType
) {
    a.unannotatedConsume(null)
    b.unannotatedConsume(null)
    c.conflictinglyAnnotatedConsume(null)
    unannotatedpackage.UnannotatedTypeWithConflictinglyAnnotatedConstructor(null)
}
