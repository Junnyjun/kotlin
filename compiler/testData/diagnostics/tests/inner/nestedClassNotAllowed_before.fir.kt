// RUN_PIPELINE_TILL: FRONTEND
// LANGUAGE: -NestedClassesInEnumEntryShouldBeInner

class A {
    inner class B {
        <!NESTED_CLASS_NOT_ALLOWED!>class C<!>
    }
    
    fun foo() {
        class B {
            <!NESTED_CLASS_NOT_ALLOWED!>class C<!>
        }
    }
}

fun foo() {
    class B {
        <!NESTED_CLASS_NOT_ALLOWED!>class C<!>
    }
}


enum class E {
    E1 {
        <!NESTED_CLASS_NOT_ALLOWED!>class D<!>
    }
}

/* GENERATED_FIR_TAGS: classDeclaration, enumDeclaration, enumEntry, functionDeclaration, inner, localClass, nestedClass */
