// RUN_PIPELINE_TILL: FRONTEND
package a

fun <V: U, U> foo(v: V, u: U) = u
fun <U, V: U> bar(v: V, u: U) = u

fun test(a: Any, s: String) {
    val b = foo(a, s)
    checkItIsExactlyAny(a, arrayListOf(b))
    val c = bar(a, s)
    checkItIsExactlyAny(a, arrayListOf(c))
}

fun <T> checkItIsExactlyAny(t: T, l: MutableList<T>) {}

fun <V : U, U> baz(v: V, u: MutableSet<U>) = u

fun test(a: Any, s: MutableSet<String>) {
    baz(a, <!TYPE_MISMATCH!>s<!>)
}

//from standard library
fun <T> arrayListOf(vararg t: T): MutableList<T> {<!NO_RETURN_IN_FUNCTION_WITH_BLOCK_BODY!>}<!>

/* GENERATED_FIR_TAGS: functionDeclaration, localProperty, nullableType, propertyDeclaration, typeConstraint,
typeParameter, vararg */
