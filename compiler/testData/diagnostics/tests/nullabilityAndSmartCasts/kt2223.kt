// RUN_PIPELINE_TILL: BACKEND
// FIR_IDENTICAL
//KT-2223 Comparing non-null value with null might produce helpful warning
package kt2223

fun foo() {
    val x: Int? = null
    if (x == null) return
    if (<!SENSELESS_COMPARISON!>x == null<!>) return
}

/* GENERATED_FIR_TAGS: equalityExpression, functionDeclaration, ifExpression, localProperty, nullableType,
propertyDeclaration, smartcast */
