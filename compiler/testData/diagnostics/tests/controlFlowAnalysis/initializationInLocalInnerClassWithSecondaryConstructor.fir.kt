// RUN_PIPELINE_TILL: BACKEND
// ISSUE: KT-65299

val foo = object {
    inner class Inner {
        val field: Any

        constructor(field: Any) {
            this.field = field
        }

        val property get() = field
    }
}

val bar = object {
    inner class Inner {
        val field: Any

        constructor(field: Any) {
            this.field = field
        }

        fun function() = field
    }
}

/* GENERATED_FIR_TAGS: anonymousObjectExpression, assignment, classDeclaration, functionDeclaration, getter, inner,
localClass, propertyDeclaration, secondaryConstructor, thisExpression */
