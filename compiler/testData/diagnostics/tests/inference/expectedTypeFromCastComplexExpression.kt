// RUN_PIPELINE_TILL: BACKEND
// FIR_IDENTICAL
// LANGUAGE: +ExpectedTypeFromCast

package pp

class A {
    fun <T> foo(): T = TODO()

    companion object {
        fun <T> foo2(): T = TODO()
    }
}

val x = A().foo() as String
val y = A.foo2() as String
val z = pp.A.foo2() as String

/* GENERATED_FIR_TAGS: asExpression, classDeclaration, companionObject, functionDeclaration, nullableType,
objectDeclaration, propertyDeclaration, typeParameter */
