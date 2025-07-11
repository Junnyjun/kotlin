// RUN_PIPELINE_TILL: BACKEND
fun <T, E> foo(x: (T) -> E) {}
fun <T, E> foo2(x: (A, T) -> E) {}

class A {
    fun baz(x: String): Int = null!!
}

fun bar(x: String): Int = null!!

fun main() {
    foo(::bar)
    foo(A()::baz)
    foo2(A::baz)
}

/* GENERATED_FIR_TAGS: callableReference, checkNotNullCall, classDeclaration, functionDeclaration, functionalType,
nullableType, typeParameter */
