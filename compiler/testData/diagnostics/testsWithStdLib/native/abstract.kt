// RUN_PIPELINE_TILL: FRONTEND
// FIR_IDENTICAL
import kotlin.jvm.*

abstract class C {
    <!EXTERNAL_DECLARATION_CANNOT_BE_ABSTRACT!>abstract<!> external fun foo()
}

fun test() {
    abstract class Local {
        <!EXTERNAL_DECLARATION_CANNOT_BE_ABSTRACT!>abstract<!> external fun foo()
    }
}

/* GENERATED_FIR_TAGS: classDeclaration, external, functionDeclaration, localClass */
