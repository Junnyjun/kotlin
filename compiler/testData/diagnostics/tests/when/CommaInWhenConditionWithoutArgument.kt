// RUN_PIPELINE_TILL: FRONTEND
// FIR_IDENTICAL
/*
 * KOTLIN DIAGNOSTICS SPEC TEST (POSITIVE)
 *
 * SPEC VERSION: 0.1-313
 * PRIMARY LINKS: expressions, when-expression -> paragraph 2 -> sentence 1
 * expressions, when-expression -> paragraph 5 -> sentence 1
 */
fun foo(x: Int, y: Int): Int =
        when {
            x > 0<!COMMA_IN_WHEN_CONDITION_WITHOUT_ARGUMENT!>,<!> y > 0<!COMMA_IN_WHEN_CONDITION_WITHOUT_ARGUMENT!>,<!><!SYNTAX!>,<!> x < 0 -> 1
            else -> 0
        }

fun bar(x: Int): Int =
        when (x) {
            0 -> 0
            else -> 1
        }

/* GENERATED_FIR_TAGS: comparisonExpression, disjunctionExpression, equalityExpression, functionDeclaration,
integerLiteral, whenExpression, whenWithSubject */
