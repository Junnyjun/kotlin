// RUN_PIPELINE_TILL: FRONTEND
// FIR_IDENTICAL
annotation class Ann(vararg val i: Boolean)
fun foo() {
    val bool1 = true

    @Ann(<!ANNOTATION_ARGUMENT_MUST_BE_CONST!>bool1<!>) val a = bool1
}

/* GENERATED_FIR_TAGS: annotationDeclaration, functionDeclaration, localProperty, primaryConstructor,
propertyDeclaration, vararg */
