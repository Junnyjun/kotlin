// MODULE: lib
// FILE: A.kt
private val privateVal: String = "OK"

@Suppress("NON_PUBLIC_CALL_FROM_PUBLIC_INLINE")
public inline fun publicInlineFunction() = ::privateVal

// MODULE: main()(lib)
// FILE: main.kt
fun box(): String {
    return publicInlineFunction().invoke()
}
