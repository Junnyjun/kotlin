// RUN_PIPELINE_TILL: BACKEND
// FIR_IDENTICAL
// DIAGNOSTICS: -UNUSED_PARAMETER

object Right
object Wrong

interface A<T>
interface B<T> : A<T>

fun <T> foo(vararg t: T) = Wrong
fun <T> foo(t: A<T>) = Wrong
fun <T> foo(t: B<T>) = Right

fun test(b: B<Int>): Right = foo(b)

/* GENERATED_FIR_TAGS: functionDeclaration, interfaceDeclaration, nullableType, objectDeclaration, typeParameter, vararg */
