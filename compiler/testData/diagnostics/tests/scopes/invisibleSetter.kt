// RUN_PIPELINE_TILL: FRONTEND
class A<T> {
    public var x: Int = 0
        private set
}

fun main() {
    val a = A<Any>()
    <!INVISIBLE_SETTER!>a.x<!> = 1
}

/* GENERATED_FIR_TAGS: assignment, classDeclaration, functionDeclaration, integerLiteral, localProperty, nullableType,
propertyDeclaration, typeParameter */
