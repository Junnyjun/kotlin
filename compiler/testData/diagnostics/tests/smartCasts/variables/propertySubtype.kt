// RUN_PIPELINE_TILL: BACKEND
// FIR_IDENTICAL
class MyClass(var p: Any)

fun bar(s: Any): Int {
    return s.hashCode()
}

fun foo(m: MyClass): Int {
    m.p = "xyz"
    return bar(m.p)
}

/* GENERATED_FIR_TAGS: assignment, classDeclaration, functionDeclaration, primaryConstructor, propertyDeclaration,
stringLiteral */
