// RUN_PIPELINE_TILL: FRONTEND
// No supertype at all

fun Any.extension(arg: Any?) {}

class A1 {
    fun test() {
        super.<!UNRESOLVED_REFERENCE!>extension<!>(null) // Call to an extension function
    }
}

/* GENERATED_FIR_TAGS: classDeclaration, funWithExtensionReceiver, functionDeclaration, nullableType, superExpression */
