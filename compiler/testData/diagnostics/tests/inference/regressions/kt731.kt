// RUN_PIPELINE_TILL: FRONTEND
// CHECK_TYPE

//KT-731 Missing error from type inference
package a

import checkSubtype

class A<T>(x: T) {
    val p = x
}

fun <T, G> A<T>.foo(x: (T)-> G): G {
    return x(this.p)
}

fun main() {
    val a = A(1)
    val t: String = <!TYPE_MISMATCH!>a.foo({p -> <!TYPE_MISMATCH!>p<!>})<!>
    checkSubtype<String>(t)
}

/* GENERATED_FIR_TAGS: classDeclaration, funWithExtensionReceiver, functionDeclaration, functionalType, infix,
integerLiteral, lambdaLiteral, localProperty, nullableType, primaryConstructor, propertyDeclaration, thisExpression,
typeParameter, typeWithExtension */
