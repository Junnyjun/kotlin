// RUN_PIPELINE_TILL: FRONTEND
// FIR_IDENTICAL
// CHECK_TYPE
// FILE: A.java
public enum A {
    ENTRY;
}

// FILE: test.kt
fun main() {
    checkSubtype<A>(A.valueOf("ENTRY"))
}

/* GENERATED_FIR_TAGS: classDeclaration, funWithExtensionReceiver, functionDeclaration, functionalType, infix,
javaFunction, javaType, nullableType, stringLiteral, typeParameter, typeWithExtension */
