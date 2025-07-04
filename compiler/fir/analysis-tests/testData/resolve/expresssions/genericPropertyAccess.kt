// RUN_PIPELINE_TILL: BACKEND
abstract class Base<T>(val x: T) {
    abstract fun foo(): T
}

class Derived<T>(x: T) : Base<T>(x) {
    override fun foo(): T = x
}

/* GENERATED_FIR_TAGS: classDeclaration, functionDeclaration, nullableType, override, primaryConstructor,
propertyDeclaration, typeParameter */
