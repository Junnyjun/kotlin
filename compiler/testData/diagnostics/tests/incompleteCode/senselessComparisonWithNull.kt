// RUN_PIPELINE_TILL: FRONTEND
// DIAGNOSTICS: -UNUSED_EXPRESSION
package d

fun foo(a : IntArray) {
    if (<!SENSELESS_COMPARISON!>null == <!FUNCTION_EXPECTED!>a<!>()<!><!SYNTAX!><!>
<!SYNTAX!><!>}

/* GENERATED_FIR_TAGS: equalityExpression, functionDeclaration, ifExpression */
