// RUN_PIPELINE_TILL: FRONTEND
// FIR_IDENTICAL
fun foo1() = <!EXPRESSION_EXPECTED!>while (b()) {}<!>

fun foo2() = <!EXPRESSION_EXPECTED!>for (i in <!ITERATOR_MISSING!>10<!>) {}<!>

fun foo3() = when (b()) {
    true -> 1
    else -> 0
}

fun b(): Boolean = true

/* GENERATED_FIR_TAGS: equalityExpression, forLoop, functionDeclaration, integerLiteral, localProperty,
propertyDeclaration, whenExpression, whenWithSubject, whileLoop */
