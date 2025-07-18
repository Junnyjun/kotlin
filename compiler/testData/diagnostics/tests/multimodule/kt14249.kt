// RUN_PIPELINE_TILL: BACKEND
// FIR_IDENTICAL
// MODULE: m1
// FILE: test/Foo.java

package test;

class Foo {
    static Foo create() { return new Foo(); }
    void takeFoo(Foo f) {}
}

// MODULE: m2(m1)
// FILE: test.kt

package test

fun test() {
    Foo()
    val a: Foo = Foo.create()
    Foo().takeFoo(a)
}

/* GENERATED_FIR_TAGS: flexibleType, functionDeclaration, javaFunction, localProperty, propertyDeclaration */
