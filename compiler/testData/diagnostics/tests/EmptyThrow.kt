// RUN_PIPELINE_TILL: FRONTEND
// FIR_IDENTICAL
fun someFun(i:Int) {
    val x = i ?: throw<!SYNTAX!><!>
}

/* GENERATED_FIR_TAGS: elvisExpression, functionDeclaration, localProperty, propertyDeclaration */
