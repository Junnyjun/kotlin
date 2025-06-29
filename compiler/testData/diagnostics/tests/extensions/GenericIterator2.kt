// RUN_PIPELINE_TILL: FRONTEND
// FIR_IDENTICAL
// CHECK_TYPE

import java.util.Enumeration

operator fun <T> java.util.Enumeration<T>.iterator() = object : Iterator<T> {
  public override fun hasNext(): Boolean = hasMoreElements()

  public override fun next() = nextElement()
}

fun a(e : java.util.Enumeration<Int>) {
    for (i in e) {
        checkSubtype<Int>(i)
    }
}

/* GENERATED_FIR_TAGS: anonymousObjectExpression, classDeclaration, flexibleType, forLoop, funWithExtensionReceiver,
functionDeclaration, functionalType, infix, localProperty, nullableType, operator, override, propertyDeclaration,
typeParameter, typeWithExtension */
