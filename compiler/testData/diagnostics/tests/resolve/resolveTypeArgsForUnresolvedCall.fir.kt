// RUN_PIPELINE_TILL: FRONTEND
fun foo() {
    <!UNRESOLVED_REFERENCE!>x<!>.yyy<<!UNRESOLVED_REFERENCE!>XXX<!>>()
    <!UNRESOLVED_REFERENCE!>x<!>.yyy<Int>()
}

/* GENERATED_FIR_TAGS: functionDeclaration */
