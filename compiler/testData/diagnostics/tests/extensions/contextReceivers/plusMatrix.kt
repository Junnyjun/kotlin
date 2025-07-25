// RUN_PIPELINE_TILL: BACKEND
// DIAGNOSTICS: -CONTEXT_RECEIVERS_DEPRECATED
// LANGUAGE: +ContextReceivers
// FIR_IDENTICAL
// DIAGNOSTICS: -UNUSED_PARAMETER

interface NumberOperations {
    fun Number.plus(other: Number): Number
}

class Matrix

context(NumberOperations) fun Matrix.plus(other: Matrix): Matrix = TODO()

fun NumberOperations.plusMatrix(m1: Matrix, m2: Matrix) {
    m1.plus(m2)
    m2.plus(m1)
}

/* GENERATED_FIR_TAGS: classDeclaration, funWithExtensionReceiver, functionDeclaration, functionDeclarationWithContext,
interfaceDeclaration */
