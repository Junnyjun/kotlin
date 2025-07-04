// RUN_PIPELINE_TILL: BACKEND
// FIR_IDENTICAL
class Outer {
    class Nested {
        companion object {
            fun foo() = 42
        }
    }
    
    companion object {
        fun bar() = 239
    }
}

fun foo() = Outer.Nested.foo()
fun bar() = Outer.bar()

/* GENERATED_FIR_TAGS: classDeclaration, companionObject, functionDeclaration, integerLiteral, nestedClass,
objectDeclaration */
