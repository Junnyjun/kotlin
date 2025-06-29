// RUN_PIPELINE_TILL: BACKEND
// FIR_IDENTICAL
// DIAGNOSTICS: -UNUSED_PARAMETER

fun withLambda(block : Int.(String) -> Unit) {
}

fun withLambda(o : Int, block : Int.(String) -> Unit) {
}

fun test() {
    withLambda {
        it.length
    }

    withLambda { r -> // no error should be here
        r.length
    }
}

/* GENERATED_FIR_TAGS: functionDeclaration, functionalType, lambdaLiteral, typeWithExtension */
