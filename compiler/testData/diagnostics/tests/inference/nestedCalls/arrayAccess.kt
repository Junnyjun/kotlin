// RUN_PIPELINE_TILL: BACKEND
// FIR_IDENTICAL
package b

class A {
    operator fun <T> get(i: Int): List<T> = throw Exception("$i")
}

fun bar(l: List<Int>) = l

fun test(a: A) {
    bar(a[12])
}

/* GENERATED_FIR_TAGS: classDeclaration, functionDeclaration, integerLiteral, nullableType, operator, typeParameter */
