// RUN_PIPELINE_TILL: FRONTEND
sealed class My(open val x: Int?) {
    init {
        if (<!DEBUG_INFO_LEAKING_THIS!>x<!> != null) {
            // Should be error: property is open
            <!DEBUG_INFO_LEAKING_THIS, SMARTCAST_IMPOSSIBLE!>x<!>.hashCode()
        }
    }
}

/* GENERATED_FIR_TAGS: classDeclaration, equalityExpression, ifExpression, init, nullableType, primaryConstructor,
propertyDeclaration, sealed, smartcast */
