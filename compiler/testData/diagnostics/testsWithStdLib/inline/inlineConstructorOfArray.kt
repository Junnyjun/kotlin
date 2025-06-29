// RUN_PIPELINE_TILL: FRONTEND
// FIR_IDENTICAL
fun testArray(b: Boolean) {
    Array(5) { i ->
        if (b) return
        i
    }
    throw AssertionError()
}

fun testMyArray(b: Boolean) {
    MyArray(5) { i ->
        if (b) <!RETURN_NOT_ALLOWED!>return<!>
        i
    }
    throw AssertionError()
}

class MyArray<T> {
    constructor(size: Int, init: (Int) -> T)
}

/* GENERATED_FIR_TAGS: classDeclaration, functionDeclaration, functionalType, ifExpression, integerLiteral,
lambdaLiteral, nullableType, secondaryConstructor, typeParameter */
