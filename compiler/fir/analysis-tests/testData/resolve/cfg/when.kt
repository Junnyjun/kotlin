// RUN_PIPELINE_TILL: BACKEND
// DUMP_CFG
fun test_1(x: Int) {
    val y = when {
        x == 1 -> 10
        x % 2 == 0 -> 20
        1 - 1 == 0 -> return
        else -> 5
    }
}

interface A
interface B

fun test_2(x: Any?) {
    if (x is A && x is B) {
        <!USELESS_IS_CHECK!>x is A<!>
    }
}

/* GENERATED_FIR_TAGS: andExpression, equalityExpression, functionDeclaration, ifExpression, integerLiteral,
interfaceDeclaration, intersectionType, isExpression, localProperty, multiplicativeExpression, nullableType,
propertyDeclaration, smartcast, whenExpression */
