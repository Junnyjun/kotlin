// RUN_PIPELINE_TILL: FRONTEND
// StackOverflow
val p = <!TYPECHECKER_HAS_RUN_INTO_RECURSIVE_PROBLEM!>::p<!>

fun foo() = <!TYPECHECKER_HAS_RUN_INTO_RECURSIVE_PROBLEM!>::foo<!>

/* GENERATED_FIR_TAGS: callableReference, functionDeclaration, propertyDeclaration */
