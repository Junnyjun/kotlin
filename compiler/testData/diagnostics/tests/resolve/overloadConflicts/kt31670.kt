// RUN_PIPELINE_TILL: FRONTEND
// FIR_IDENTICAL

open class A<T>(val value: T)
class B<T>(value: T) : A<T>(value)

fun <T> A<T>.foo(block: (T?) -> Unit) {
    block(value)
}
fun <T> B<T>.foo(block: (T) -> Unit) {
    block(value)
}

fun main() {
    B("string").<!OVERLOAD_RESOLUTION_AMBIGUITY!>foo<!> {  }
}

/* GENERATED_FIR_TAGS: classDeclaration, funWithExtensionReceiver, functionDeclaration, functionalType, lambdaLiteral,
nullableType, primaryConstructor, propertyDeclaration, stringLiteral, typeParameter */
