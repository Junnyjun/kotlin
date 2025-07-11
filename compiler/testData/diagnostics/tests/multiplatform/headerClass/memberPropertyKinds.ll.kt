// IGNORE_FIR_DIAGNOSTICS
// RUN_PIPELINE_TILL: FIR2IR
// MODULE: m1-common
// FILE: common.kt

expect class Foo {
    val justVal: String
    var justVar: String

    val String.extensionVal: Unit
    var <T> T.genericExtensionVar: T

    val valWithGet: String
        get
    var varWithGetSet: String
        get set

    val backingFieldVal: String = <!EXPECTED_PROPERTY_INITIALIZER!>"no"<!>
    var backingFieldVar: String = <!EXPECTED_PROPERTY_INITIALIZER!>"no"<!>

    val customAccessorVal: String
    <!EXPECTED_DECLARATION_WITH_BODY!>get()<!> = "no"
    var customAccessorVar: String
    <!EXPECTED_DECLARATION_WITH_BODY!>get()<!> = "no"
    <!EXPECTED_DECLARATION_WITH_BODY!>set(value)<!> {}

    <!EXPECTED_LATEINIT_PROPERTY!>lateinit<!> var lateinitVar: String

    val delegated: String by <!EXPECTED_DELEGATED_PROPERTY!>Delegate<!>
}

object Delegate { operator fun getValue(x: Any?, y: Any?): String = "" }

// MODULE: m1-jvm()()(m1-common)

/* GENERATED_FIR_TAGS: classDeclaration, expect, functionDeclaration, getter, lateinit, nullableType, objectDeclaration,
operator, propertyDeclaration, propertyDelegate, propertyWithExtensionReceiver, setter, stringLiteral, typeParameter */
