// RUN_PIPELINE_TILL: FRONTEND
// FIR_IDENTICAL
// DIAGNOSTICS: -UNUSED_PARAMETER -UNUSED_VARIABLE
// CHECK_TYPE
// FILE: a.kt
class B(x: String)

typealias A1 = B
private typealias A2 = B
private typealias A3 = B

fun A3(x: Any) = "OK"

fun bar() {
    A3("") checkType { _<B>() }
}

/* GENERATED_FIR_TAGS: classDeclaration, funWithExtensionReceiver, functionDeclaration, functionalType, infix,
lambdaLiteral, nullableType, primaryConstructor, stringLiteral, typeAliasDeclaration, typeParameter, typeWithExtension */
