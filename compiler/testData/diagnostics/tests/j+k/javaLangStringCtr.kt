// RUN_PIPELINE_TILL: FRONTEND
// WITH_STDLIB
// ISSUE: KT-51670

val s = String(byteArrayOf(1, 2, 3), 4, 5, <!CONSTANT_EXPECTED_TYPE_MISMATCH!>6<!>)

/* GENERATED_FIR_TAGS: integerLiteral, propertyDeclaration */
