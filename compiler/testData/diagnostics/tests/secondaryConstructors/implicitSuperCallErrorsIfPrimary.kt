// RUN_PIPELINE_TILL: FRONTEND
// FIR_IDENTICAL
// DIAGNOSTICS: -UNUSED_PARAMETER
open class A(p1: String)

class B() : A("") {
    <!PRIMARY_CONSTRUCTOR_DELEGATION_CALL_EXPECTED!>constructor(s: String)<!> {
    }
}

/* GENERATED_FIR_TAGS: classDeclaration, primaryConstructor, secondaryConstructor, stringLiteral */
