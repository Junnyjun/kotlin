// RUN_PIPELINE_TILL: FRONTEND
fun import() {
    <!FUNCTION_CALL_EXPECTED!>import<!> <!UNRESOLVED_REFERENCE!>a<!><!SYNTAX!>.<!><!DEBUG_INFO_MISSING_UNRESOLVED!>*<!><!SYNTAX!><!>
}

fun composite() {
    val s = 13+<!SYNTAX!>~<!><!DEBUG_INFO_MISSING_UNRESOLVED!>/<!>12
}

fun html() {
    <!SYNTAX!><<!><!FUNCTION_CALL_EXPECTED!>html<!><!UNRESOLVED_REFERENCE!>><!><!SYNTAX!><<!><!DEBUG_INFO_MISSING_UNRESOLVED!>/<!><!DEBUG_INFO_MISSING_UNRESOLVED!>html<!><!DEBUG_INFO_MISSING_UNRESOLVED!>><!><!SYNTAX!><!>
}

fun html1() {
    <!SYNTAX!><<!><!FUNCTION_CALL_EXPECTED!>html<!><!UNRESOLVED_REFERENCE!>><!><!SYNTAX!><<!><!DEBUG_INFO_MISSING_UNRESOLVED!>/<!><!DEBUG_INFO_MISSING_UNRESOLVED!>html<!><!DEBUG_INFO_MISSING_UNRESOLVED!>><!><!FUNCTION_CALL_EXPECTED!>html<!>
}

/* GENERATED_FIR_TAGS: additiveExpression, comparisonExpression, functionDeclaration, integerLiteral, localProperty,
multiplicativeExpression, propertyDeclaration */
