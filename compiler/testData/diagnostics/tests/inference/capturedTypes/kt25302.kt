// RUN_PIPELINE_TILL: BACKEND
// FIR_IDENTICAL
// DIAGNOSTICS: -UNUSED_PARAMETER

interface CollectorMock<A, R>

interface StreamMock {
    fun <R, A> collect(collector: CollectorMock<A, R>): R
}

fun <T> accept(s: T) {}
fun ofK(t: String): StreamMock = TODO()
fun <T> toSetK(): CollectorMock<*, T> = TODO()

class KotlinCollectionUser1 {
    fun use() {
        accept(ofK("").collect(toSetK<String>()))
    }
}

/* GENERATED_FIR_TAGS: capturedType, classDeclaration, functionDeclaration, interfaceDeclaration, nullableType,
starProjection, stringLiteral, typeParameter */
