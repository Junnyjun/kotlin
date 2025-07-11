// RUN_PIPELINE_TILL: BACKEND
// See also KT-7817

fun <R> synchronized(lock: Any, block: () -> R): R = block()

class My {
    val test: String
        get() = synchronized(this) {
            var x: String? = ""
            if (x == null) {
                x = "s"
            }
            <!DEBUG_INFO_SMARTCAST!>x<!>
        }
}

/* GENERATED_FIR_TAGS: assignment, classDeclaration, equalityExpression, functionDeclaration, functionalType, getter,
ifExpression, lambdaLiteral, localProperty, nullableType, propertyDeclaration, smartcast, stringLiteral, thisExpression,
typeParameter */
