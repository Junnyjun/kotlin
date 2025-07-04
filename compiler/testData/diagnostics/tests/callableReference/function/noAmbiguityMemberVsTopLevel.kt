// RUN_PIPELINE_TILL: FRONTEND
// CHECK_TYPE
// LANGUAGE: +CallableReferencesToClassMembersWithEmptyLHS

import kotlin.reflect.KFunction0

fun expectFunction0Unit(f: () -> Unit) = f
fun expectFunction0String(f: () -> String) = f
fun expectFunction1Unit(f: (A) -> Unit) = f
fun expectFunction1String(f: (A) -> String) = f

fun foo(): String = ""

class A {
    fun foo() {}

    fun main() {
        val x = ::foo

        checkSubtype<KFunction0<Unit>>(x)

        expectFunction0Unit(x)
        expectFunction0String(<!TYPE_MISMATCH!>x<!>)
        expectFunction1Unit(<!TYPE_MISMATCH!>x<!>)
        expectFunction1String(<!TYPE_MISMATCH!>x<!>)

        expectFunction0Unit(::foo)
        expectFunction0String(::foo)
        expectFunction1Unit(<!TYPE_MISMATCH!>::foo<!>)
        expectFunction1String(<!TYPE_MISMATCH!>::foo<!>)
    }
}

/* GENERATED_FIR_TAGS: callableReference, classDeclaration, funWithExtensionReceiver, functionDeclaration,
functionalType, infix, localProperty, nullableType, propertyDeclaration, stringLiteral, typeParameter, typeWithExtension */
