// RUN_PIPELINE_TILL: FRONTEND
class <!PACKAGE_OR_CLASSIFIER_REDECLARATION!>A<!>
class <!PACKAGE_OR_CLASSIFIER_REDECLARATION!>A<!> {
    constructor()
}

class B
class Outer {
    class B {
        constructor()
    }
}

/* GENERATED_FIR_TAGS: classDeclaration, nestedClass, secondaryConstructor */
