// RUN_PIPELINE_TILL: BACKEND
class ExplicitAccessorForAnnotation {
    val tt: String? = "good"
        get

    fun foo(): String {
        if (tt is String) {
            return <!SMARTCAST_IMPOSSIBLE!>tt<!>
        }
        return ""
    }
}

/* GENERATED_FIR_TAGS: classDeclaration, functionDeclaration, ifExpression, isExpression, nullableType,
propertyDeclaration, smartcast, stringLiteral */
