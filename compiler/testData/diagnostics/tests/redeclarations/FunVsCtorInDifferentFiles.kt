// RUN_PIPELINE_TILL: FRONTEND
// FIR_IDENTICAL
// FILE: test1.kt
class <!CONFLICTING_OVERLOADS!>A<!>
class B<!CONFLICTING_OVERLOADS!>(val x: Int)<!> {
    <!CONFLICTING_OVERLOADS!>constructor(x: Int, y: Int)<!>: this(x + y)
}

// FILE: test2.kt
<!CONFLICTING_OVERLOADS!>fun A()<!> {}
<!CONFLICTING_OVERLOADS!>fun B(x: Int)<!> = x
<!CONFLICTING_OVERLOADS!>fun B(x: Int, y: Int)<!> = x + y

/* GENERATED_FIR_TAGS: additiveExpression, classDeclaration, functionDeclaration, primaryConstructor,
propertyDeclaration, secondaryConstructor */
