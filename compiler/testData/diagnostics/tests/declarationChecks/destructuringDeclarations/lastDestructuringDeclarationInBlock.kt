// RUN_PIPELINE_TILL: BACKEND
// FIR_IDENTICAL
// DIAGNOSTICS: -UNUSED_VARIABLE, -UNUSED_PARAMETER

fun test(list: A) {
    if (true) {
        val (c) = list
    }
    else {}

    if (true) {
        Unit
        val (c) = list
    }
    else {}

    when (1) {
        1 -> {
            val (c) = list
        }
    }

    fn { it ->
        val (a) = it
    }
}

class A {
    operator fun component1() = 1
}

fun fn(x: (A) -> Unit) {}

/* GENERATED_FIR_TAGS: classDeclaration, destructuringDeclaration, equalityExpression, functionDeclaration,
functionalType, ifExpression, integerLiteral, lambdaLiteral, localProperty, operator, propertyDeclaration,
whenExpression, whenWithSubject */
