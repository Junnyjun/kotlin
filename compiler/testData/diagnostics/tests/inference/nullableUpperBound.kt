// RUN_PIPELINE_TILL: BACKEND
// FIR_IDENTICAL
package o

fun foo(): String? {
    return accept(JV<String?, Unit?>())
}

fun <R, D> accept(v: JV<R, D>): R? = null

open class JV<R, D>()

/* GENERATED_FIR_TAGS: classDeclaration, functionDeclaration, nullableType, primaryConstructor, typeParameter */
