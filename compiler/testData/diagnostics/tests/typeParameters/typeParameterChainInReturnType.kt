// RUN_PIPELINE_TILL: FRONTEND
// FIR_IDENTICAL
// ISSUE: KT-56212
fun <F> foo(): F.<!UNRESOLVED_REFERENCE!>X<!> = TODO()

/* GENERATED_FIR_TAGS: functionDeclaration, nullableType, typeParameter */
