// RUN_PIPELINE_TILL: FRONTEND
// DIAGNOSTICS: -CONTEXT_RECEIVERS_DEPRECATED
// FIR_IDENTICAL
// LANGUAGE: +ContextReceivers

class A {
    fun h1() {}
}

class B {
    fun h2() {}
}

fun B.foo() {
    <!UNRESOLVED_REFERENCE!>h1<!>()
    h2()
}

context(A)
fun B.bar() {
    h1()
    h2()
}

/* GENERATED_FIR_TAGS: classDeclaration, funWithExtensionReceiver, functionDeclaration, functionDeclarationWithContext */
