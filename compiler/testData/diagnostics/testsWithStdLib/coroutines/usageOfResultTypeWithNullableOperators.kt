// RUN_PIPELINE_TILL: BACKEND
// FIR_IDENTICAL
// DIAGNOSTICS: -UNUSED_EXPRESSION

fun <T> id(x: T): T = x

private val asFun: () -> Result<Int>? = TODO()
private val Int.intResult: Result<Int>?
    get() = null

fun returnInt(): Int? = 0


fun nullableOperators(r1: Result<Int>?, b: Boolean) {
    if (b) {
        r1!!
        asFun()!!
        returnInt()?.intResult!!.toString()
    }

    if (b) {
        id(r1)!!
    }

    if (b) {
        r1?.toString()
        r1?.let { }
        returnInt()?.intResult?.toString()
        asFun()?.toString()
        id(r1)?.toString()
    }

    if (b) {
        r1 ?: 0
        r1 ?: r1
        asFun() ?: r1 ?: 0
        id(asFun()) ?: 0

        returnInt() ?: returnInt() ?: asFun() ?: 0
    }
}

/* GENERATED_FIR_TAGS: checkNotNullCall, elvisExpression, functionDeclaration, functionalType, getter, ifExpression,
integerLiteral, lambdaLiteral, nullableType, propertyDeclaration, propertyWithExtensionReceiver, safeCall, smartcast,
typeParameter */
