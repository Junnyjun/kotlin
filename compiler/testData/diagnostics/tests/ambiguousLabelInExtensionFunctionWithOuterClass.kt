// RUN_PIPELINE_TILL: FRONTEND
// FIR_IDENTICAL

class Dup {
    fun String.Dup() : Unit {
        this<!AMBIGUOUS_LABEL!>@Dup<!>
    }
}

/* GENERATED_FIR_TAGS: classDeclaration, funWithExtensionReceiver, functionDeclaration, thisExpression */
