// RUN_PIPELINE_TILL: FRONTEND
// FIR_IDENTICAL
// DIAGNOSTICS: -MUST_BE_INITIALIZED -TYPE_PARAMETER_OF_PROPERTY_NOT_USED_IN_RECEIVER
fun <<!CYCLIC_GENERIC_UPPER_BOUND!>T: T?<!>> foo() {}
val <<!CYCLIC_GENERIC_UPPER_BOUND!>T: T?<!>> prop

/* GENERATED_FIR_TAGS: functionDeclaration, propertyDeclaration, typeConstraint, typeParameter */
