// RUN_PIPELINE_TILL: BACKEND
class C {
    fun foo(@Suppress("REDUNDANT_NULLABLE", "UNNECESSARY_NOT_NULL_ASSERTION") p: String?? = ""!! <!USELESS_CAST!>as String??<!>) = p
}

/* GENERATED_FIR_TAGS: asExpression, checkNotNullCall, classDeclaration, functionDeclaration, nullableType,
stringLiteral */
