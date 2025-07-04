// RUN_PIPELINE_TILL: BACKEND
// FIR_IDENTICAL
// FILE: A.java
public class A {}

// FILE: X.java
public class X<T> {
    T foo() {return null;}
    void bar(T a) {}
}

// FILE: Y.java
public class Y extends X<String> {

}

// FILE: test.kt

fun main() {
    Y().foo().length
    Y().bar(null)
}

/* GENERATED_FIR_TAGS: flexibleType, functionDeclaration, javaFunction, javaType, nullableType */
