// RUN_PIPELINE_TILL: BACKEND
// ISSUE: KT-51759

fun testBreak(b: Boolean, s: String?) {
    while (b) {
        val x: String?
        try {
            x = s ?: break
        } finally {
        }
        x!!.length
    }
}

fun testContinue(b: Boolean, s: String?) {
    while (b) {
        val x: String?
        try {
            x = s ?: continue
        } finally {
        }
        x!!.length
    }
}

/* GENERATED_FIR_TAGS: assignment, break, checkNotNullCall, continue, elvisExpression, functionDeclaration,
localProperty, nullableType, propertyDeclaration, smartcast, tryExpression, whileLoop */
