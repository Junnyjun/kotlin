// RUN_PIPELINE_TILL: BACKEND
// FIR_IDENTICAL
// SKIP_TXT
// DIAGNOSTICS: -UNUSED_PARAMETER -UNUSED_ANONYMOUS_PARAMETER -CAST_NEVER_SUCCEEDS

interface Foo<out K, out V> {}

fun <K, V> fooOf(vararg pairs: Pair<K, V>) = null as Foo<K, V>

fun <A, B> to(th: A, that: B): Pair<A, B> = Pair(th, that)

class A {
    val x: Foo<(Int, String, String, String) -> Unit, (String, String, String, String) -> Unit> =
        fooOf(
            to({ a, b, c, d -> }, { a, b, c, d -> }),
            to({ a, b, c, d -> }, { a, b, c, d -> }),
            to({ a, b, c, d -> }, { a, b, c, d -> }),
            to({ a, b, c, d -> }, { a, b, c, d -> }),
            to({ a, b, c, d -> }, { a, b, c, d -> }),
            to({ a, b, c, d -> }, { a, b, c, d -> }),
            to({ a, b, c, d -> }, { a, b, c, d -> }),
            to({ a, b, c, d -> }, { a, b, c, d -> }),
            to({ a, b, c, d -> }, { a, b, c, d -> }),
            to({ a, b, c, d -> }, { a, b, c, d -> }),
            to({ a, b, c, d -> }, { a, b, c, d -> }),
            to({ a, b, c, d -> }, { a, b, c, d -> }),
            to({ a, b, c, d -> }, { a, b, c, d -> }),
            to({ a, b, c, d -> }, { a, b, c, d -> }),
            to({ a, b, c, d -> }, { a, b, c, d -> }),
            to({ a, b, c, d -> }, { a, b, c, d -> }),
            to({ a, b, c, d -> }, { a, b, c, d -> }),
            to({ a, b, c, d -> }, { a, b, c, d -> }),
            to({ a, b, c, d -> }, { a, b, c, d -> }),
            to({ a, b, c, d -> }, { a, b, c, d -> }),
            to({ a, b, c, d -> }, { a, b, c, d -> }),
            to({ a, b, c, d -> }, { a, b, c, d -> }),
            to({ a, b, c, d -> }, { a, b, c, d -> }),
            to({ a, b, c, d -> }, { a, b, c, d -> }),
            to({ a, b, c, d -> }, { a, b, c, d -> }),
            to({ a, b, c, d -> }, { a, b, c, d -> }),
            to({ a, b, c, d -> }, { a, b, c, d -> }),
        )
}

/* GENERATED_FIR_TAGS: asExpression, classDeclaration, functionDeclaration, interfaceDeclaration, lambdaLiteral,
nullableType, out, propertyDeclaration, typeParameter, vararg */
