// RUN_PIPELINE_TILL: FRONTEND
open class SomeClass<T>
class TestSome<P> {
    companion object : SomeClass<<!UNRESOLVED_REFERENCE!>P<!>>() {
    }
}

class Test {
    companion object : <!INNER_CLASS_CONSTRUCTOR_NO_RECEIVER!>InnerClass<!>() {
        val a = object: <!INNER_CLASS_CONSTRUCTOR_NO_RECEIVER!>InnerClass<!>() {
        }

        fun more(): InnerClass {
            val b = <!INNER_CLASS_CONSTRUCTOR_NO_RECEIVER!>InnerClass<!>()

            val testVal = <!UNRESOLVED_REFERENCE!>inClass<!>
            <!UNRESOLVED_REFERENCE!>foo<!>()

            return b
        }
    }

    val inClass = 12
    fun foo() {}

    open inner class InnerClass
}

/* GENERATED_FIR_TAGS: anonymousObjectExpression, classDeclaration, companionObject, functionDeclaration, inner,
integerLiteral, localProperty, nullableType, objectDeclaration, propertyDeclaration, typeParameter */
