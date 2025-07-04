// RUN_PIPELINE_TILL: BACKEND
// FIR_IDENTICAL
class A {
    private open class B
    fun f() {
        // Local from private: Ok
        class C : B()
    }
}

private open class D

fun f(): Int {
    // Local from private: Ok
    val x = object : D() { }
    return x.hashCode()
}

/* GENERATED_FIR_TAGS: anonymousObjectExpression, classDeclaration, functionDeclaration, localClass, localProperty,
nestedClass, propertyDeclaration */
