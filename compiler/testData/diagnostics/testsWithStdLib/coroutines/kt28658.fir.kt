// RUN_PIPELINE_TILL: FRONTEND
// OPT_IN: kotlin.RequiresOptIn
// DIAGNOSTICS: -UNUSED_EXPRESSION -UNUSED_PARAMETER -UNUSED_VARIABLE

import kotlin.experimental.ExperimentalTypeInference

fun test1() {
    <!CANNOT_INFER_PARAMETER_TYPE!>sequence<!> {
        val a: Array<Int> = arrayOf(1, 2, 3)
        val b = arrayOf(1, 2, 3)
    }
}

fun test2() = <!CANNOT_INFER_PARAMETER_TYPE!>sequence<!> { arrayOf(1, 2, 3) }


class Foo<T>

fun <T> f1(f: Foo<T>.() -> Unit) {}

@OptIn(ExperimentalTypeInference::class)
fun <T> f2(f: Foo<T>.() -> Unit) {
}

fun test3() {
    <!CANNOT_INFER_PARAMETER_TYPE!>f1<!> {
        val a: Array<Int> = arrayOf(1, 2, 3)
    }

    <!CANNOT_INFER_PARAMETER_TYPE!>f2<!> {
        val a: Array<Int> = arrayOf(1, 2, 3)
    }
}

/* GENERATED_FIR_TAGS: classDeclaration, classReference, functionDeclaration, functionalType, integerLiteral,
lambdaLiteral, localProperty, nullableType, propertyDeclaration, typeParameter, typeWithExtension */
