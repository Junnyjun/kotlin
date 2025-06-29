// RUN_PIPELINE_TILL: FRONTEND
// FIR_IDENTICAL
// FILE: JavaInterface.java

interface JavaInterface {
    void foo(int javaName);
}

// FILE: kotlin.kt

interface KotlinTrait {
    public fun foo(someOtherName: Int) {}
}

<!MANY_INTERFACES_MEMBER_NOT_IMPLEMENTED!>class BothTraitsSubclass<!> : JavaInterface, KotlinTrait

/* GENERATED_FIR_TAGS: classDeclaration, functionDeclaration, interfaceDeclaration, javaType */
