// RUN_PIPELINE_TILL: FRONTEND
// ISSUE: KT-22499

fun test(x: Any, y: Any) =
    x is Float && y is Double && x == y

fun test(x: Float, y: Double) =
    <!EQUALITY_NOT_APPLICABLE!>x == y<!>

fun fest(x: Any, y: Any) =
    x is Float && y is Double && x === y

fun fest(x: Float, y: Double) =
    <!EQUALITY_NOT_APPLICABLE!>x === y<!>

/* GENERATED_FIR_TAGS: andExpression, equalityExpression, functionDeclaration, isExpression, smartcast */
