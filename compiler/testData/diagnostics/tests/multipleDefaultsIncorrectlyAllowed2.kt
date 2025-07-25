// RUN_PIPELINE_TILL: BACKEND
// ISSUE: KT-36188

interface A {
    fun foo(a: String = "Fail"): String
}
interface A2 : A
interface B {
    fun foo(a: String = "OK"): String
}
class Impl : A2, B {
    override fun foo(a: String) = a
}

fun box(): String = Impl().foo()

/* GENERATED_FIR_TAGS: classDeclaration, functionDeclaration, interfaceDeclaration, override, stringLiteral */
