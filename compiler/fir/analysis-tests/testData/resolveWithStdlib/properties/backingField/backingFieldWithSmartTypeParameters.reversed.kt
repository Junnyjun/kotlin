// RUN_PIPELINE_TILL: BACKEND
val items: List<String>
    field = mutableListOf()

fun test() {
    items.<!UNRESOLVED_REFERENCE!>add<!>("one more item")
}

/* GENERATED_FIR_TAGS: functionDeclaration, propertyDeclaration, stringLiteral */
