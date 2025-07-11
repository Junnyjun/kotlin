// RUN_PIPELINE_TILL: BACKEND
open class A {
    operator fun invoke(f: () -> Unit): Int = 1
}

class B {
    operator fun invoke(f: () -> Unit): CharSequence = ""
}

open class C
val C.attr: A get() = TODO()

open class D: C()
val D.attr: B get() = TODO()

fun box(d: D) {
    (d.attr {}).length
}

/* GENERATED_FIR_TAGS: classDeclaration, functionDeclaration, functionalType, getter, integerLiteral, lambdaLiteral,
operator, propertyDeclaration, propertyWithExtensionReceiver, stringLiteral */
