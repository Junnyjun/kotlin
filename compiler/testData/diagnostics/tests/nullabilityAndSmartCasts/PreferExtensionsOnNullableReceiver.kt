// RUN_PIPELINE_TILL: BACKEND
// FIR_IDENTICAL
class Foo {
    fun foo() {}
}

fun Any?.foo() {}

fun test(f : Foo?) {
  f.foo()
}

/* GENERATED_FIR_TAGS: classDeclaration, funWithExtensionReceiver, functionDeclaration, nullableType */
