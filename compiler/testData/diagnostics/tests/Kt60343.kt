// RUN_PIPELINE_TILL: FRONTEND
fun <T> test(x: Any) {
    <!UNRESOLVED_REFERENCE!>println<!>(<!RESOLUTION_TO_CLASSIFIER!>T<!><T>)
}

/* GENERATED_FIR_TAGS: functionDeclaration, nullableType, typeParameter */
