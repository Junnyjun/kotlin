// RUN_PIPELINE_TILL: FRONTEND
// KT-9051: Allow smart cast for captured variables if they are not modified

fun foo(y: String) {
    var x: String? = null
    y.let { x = it }
    x<!UNSAFE_CALL!>.<!>length // Smart cast is not possible
}

/* GENERATED_FIR_TAGS: assignment, functionDeclaration, lambdaLiteral, localProperty, nullableType, propertyDeclaration */
