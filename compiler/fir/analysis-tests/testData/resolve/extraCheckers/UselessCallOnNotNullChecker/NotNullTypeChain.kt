// RUN_PIPELINE_TILL: BACKEND
// WITH_STDLIB

val list1: List<Int> = listOf(1)
val list = list1.<!USELESS_CALL_ON_NOT_NULL!>orEmpty()<!>.map { "$it" }

/* GENERATED_FIR_TAGS: integerLiteral, lambdaLiteral, propertyDeclaration */
