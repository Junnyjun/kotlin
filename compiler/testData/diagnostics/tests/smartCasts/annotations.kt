// RUN_PIPELINE_TILL: BACKEND
// ISSUE: KT-68803

interface Foo<out T> {
    val foo: T
}

interface ImplA<T> {
    val fooOrNull: T?
        get() = if (this is Foo<*>) {
            @Suppress("UNCHECKED_CAST")
            <!DEBUG_INFO_IMPLICIT_RECEIVER_SMARTCAST!>foo<!> as T
        } else null
}

/* GENERATED_FIR_TAGS: asExpression, getter, ifExpression, interfaceDeclaration, intersectionType, isExpression,
nullableType, out, propertyDeclaration, smartcast, starProjection, stringLiteral, thisExpression, typeParameter */
