// RUN_PIPELINE_TILL: FRONTEND
// ISSUE: KT-65812
class Controller<T> {
    fun yield(t: T): Boolean = true
}

fun <S> generate(g: suspend Controller<S>.() -> Unit): S = TODO()

fun Controller<*>.foo() {}
fun <V, Y> V.bar(y: Y) {}


fun main() {
    <!CANNOT_INFER_PARAMETER_TYPE!>generate<!> {
        // This call contributes an inference error to the shared CS
        <!UNRESOLVED_REFERENCE!>MyUnresolvedClass<!>().<!CANNOT_INFER_PARAMETER_TYPE, CANNOT_INFER_PARAMETER_TYPE!>bar<!>(this)

        // A lot of calls using shared CS
        // Previoisly, for each of them we copied all the previous errors and adding them to the shared CS
        // Thus, having an exponential number of errors
        <!UNRESOLVED_REFERENCE_WRONG_RECEIVER!>foo<!>()
        <!UNRESOLVED_REFERENCE_WRONG_RECEIVER!>foo<!>()
        <!UNRESOLVED_REFERENCE_WRONG_RECEIVER!>foo<!>()
        <!UNRESOLVED_REFERENCE_WRONG_RECEIVER!>foo<!>()
        <!UNRESOLVED_REFERENCE_WRONG_RECEIVER!>foo<!>()
        <!UNRESOLVED_REFERENCE_WRONG_RECEIVER!>foo<!>()
        <!UNRESOLVED_REFERENCE_WRONG_RECEIVER!>foo<!>()
        <!UNRESOLVED_REFERENCE_WRONG_RECEIVER!>foo<!>()
        <!UNRESOLVED_REFERENCE_WRONG_RECEIVER!>foo<!>()
        <!UNRESOLVED_REFERENCE_WRONG_RECEIVER!>foo<!>()
        <!UNRESOLVED_REFERENCE_WRONG_RECEIVER!>foo<!>()
        <!UNRESOLVED_REFERENCE_WRONG_RECEIVER!>foo<!>()
        <!UNRESOLVED_REFERENCE_WRONG_RECEIVER!>foo<!>()
        <!UNRESOLVED_REFERENCE_WRONG_RECEIVER!>foo<!>()
        <!UNRESOLVED_REFERENCE_WRONG_RECEIVER!>foo<!>()
        <!UNRESOLVED_REFERENCE_WRONG_RECEIVER!>foo<!>()
        <!UNRESOLVED_REFERENCE_WRONG_RECEIVER!>foo<!>()
        <!UNRESOLVED_REFERENCE_WRONG_RECEIVER!>foo<!>()
        <!UNRESOLVED_REFERENCE_WRONG_RECEIVER!>foo<!>()
        <!UNRESOLVED_REFERENCE_WRONG_RECEIVER!>foo<!>()
        <!UNRESOLVED_REFERENCE_WRONG_RECEIVER!>foo<!>()
        <!UNRESOLVED_REFERENCE_WRONG_RECEIVER!>foo<!>()
        <!UNRESOLVED_REFERENCE_WRONG_RECEIVER!>foo<!>()
        <!UNRESOLVED_REFERENCE_WRONG_RECEIVER!>foo<!>()
        <!UNRESOLVED_REFERENCE_WRONG_RECEIVER!>foo<!>()
        <!UNRESOLVED_REFERENCE_WRONG_RECEIVER!>foo<!>()
        <!UNRESOLVED_REFERENCE_WRONG_RECEIVER!>foo<!>()
        <!UNRESOLVED_REFERENCE_WRONG_RECEIVER!>foo<!>()
        <!UNRESOLVED_REFERENCE_WRONG_RECEIVER!>foo<!>()
        <!UNRESOLVED_REFERENCE_WRONG_RECEIVER!>foo<!>()
        <!UNRESOLVED_REFERENCE_WRONG_RECEIVER!>foo<!>()
        <!UNRESOLVED_REFERENCE_WRONG_RECEIVER!>foo<!>()
        <!UNRESOLVED_REFERENCE_WRONG_RECEIVER!>foo<!>()
        <!UNRESOLVED_REFERENCE_WRONG_RECEIVER!>foo<!>()
        <!UNRESOLVED_REFERENCE_WRONG_RECEIVER!>foo<!>()
        <!UNRESOLVED_REFERENCE_WRONG_RECEIVER!>foo<!>()
        <!UNRESOLVED_REFERENCE_WRONG_RECEIVER!>foo<!>()
        <!UNRESOLVED_REFERENCE_WRONG_RECEIVER!>foo<!>()
        <!UNRESOLVED_REFERENCE_WRONG_RECEIVER!>foo<!>()
        <!UNRESOLVED_REFERENCE_WRONG_RECEIVER!>foo<!>()
        <!UNRESOLVED_REFERENCE_WRONG_RECEIVER!>foo<!>()
        <!UNRESOLVED_REFERENCE_WRONG_RECEIVER!>foo<!>()
        <!UNRESOLVED_REFERENCE_WRONG_RECEIVER!>foo<!>()
        <!UNRESOLVED_REFERENCE_WRONG_RECEIVER!>foo<!>()
        <!UNRESOLVED_REFERENCE_WRONG_RECEIVER!>foo<!>()
        <!UNRESOLVED_REFERENCE_WRONG_RECEIVER!>foo<!>()
        <!UNRESOLVED_REFERENCE_WRONG_RECEIVER!>foo<!>()
        <!UNRESOLVED_REFERENCE_WRONG_RECEIVER!>foo<!>()
        <!UNRESOLVED_REFERENCE_WRONG_RECEIVER!>foo<!>()
        <!UNRESOLVED_REFERENCE_WRONG_RECEIVER!>foo<!>()
        <!UNRESOLVED_REFERENCE_WRONG_RECEIVER!>foo<!>()
        <!UNRESOLVED_REFERENCE_WRONG_RECEIVER!>foo<!>()
        <!UNRESOLVED_REFERENCE_WRONG_RECEIVER!>foo<!>()
        <!UNRESOLVED_REFERENCE_WRONG_RECEIVER!>foo<!>()
        <!UNRESOLVED_REFERENCE_WRONG_RECEIVER!>foo<!>()
        <!UNRESOLVED_REFERENCE_WRONG_RECEIVER!>foo<!>()
        <!UNRESOLVED_REFERENCE_WRONG_RECEIVER!>foo<!>()
        <!UNRESOLVED_REFERENCE_WRONG_RECEIVER!>foo<!>()
        <!UNRESOLVED_REFERENCE_WRONG_RECEIVER!>foo<!>()
        <!UNRESOLVED_REFERENCE_WRONG_RECEIVER!>foo<!>()
        <!UNRESOLVED_REFERENCE_WRONG_RECEIVER!>foo<!>()
        <!UNRESOLVED_REFERENCE_WRONG_RECEIVER!>foo<!>()
        <!UNRESOLVED_REFERENCE_WRONG_RECEIVER!>foo<!>()
        <!UNRESOLVED_REFERENCE_WRONG_RECEIVER!>foo<!>()
        <!UNRESOLVED_REFERENCE_WRONG_RECEIVER!>foo<!>()
        <!UNRESOLVED_REFERENCE_WRONG_RECEIVER!>foo<!>()
        <!UNRESOLVED_REFERENCE_WRONG_RECEIVER!>foo<!>()
        <!UNRESOLVED_REFERENCE_WRONG_RECEIVER!>foo<!>()
    }
}

/* GENERATED_FIR_TAGS: classDeclaration, funWithExtensionReceiver, functionDeclaration, functionalType, lambdaLiteral,
nullableType, starProjection, suspend, thisExpression, typeParameter, typeWithExtension */
