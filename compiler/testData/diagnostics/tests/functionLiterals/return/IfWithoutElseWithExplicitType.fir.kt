// RUN_PIPELINE_TILL: FRONTEND
// LATEST_LV_DIFFERENCE
val flag = true

val a: () -> Int = l@ {
    <!RETURN_TYPE_MISMATCH!>if (flag) return@l 4<!>
}

val b: () -> Unit = l@ {
    if (flag) return@l <!RETURN_TYPE_MISMATCH!>4<!>
}

val c: () -> Any = l@ {
    if (flag) return@l 4
}

val d: () -> Int = l@ {
    if (flag) return@l 4
    5
}

val e: () -> Int = <!INITIALIZER_TYPE_MISMATCH!>l@ {
    if (flag) 4
}<!>

/* GENERATED_FIR_TAGS: functionalType, ifExpression, integerLiteral, lambdaLiteral, propertyDeclaration */
