// RUN_PIPELINE_TILL: FRONTEND
// FIR_IDENTICAL
fun <<!VARIANCE_ON_TYPE_PARAMETER_NOT_ALLOWED!>in<!> T> f() {
    
}

fun <<!VARIANCE_ON_TYPE_PARAMETER_NOT_ALLOWED!>out<!> T> g() {

}

fun <<!VARIANCE_ON_TYPE_PARAMETER_NOT_ALLOWED!>out<!> T, <!VARIANCE_ON_TYPE_PARAMETER_NOT_ALLOWED!>in<!> X, Y> h() {

}

val <<!VARIANCE_ON_TYPE_PARAMETER_NOT_ALLOWED!>out<!> T> T.x: Int
    get() = 1

val <<!VARIANCE_ON_TYPE_PARAMETER_NOT_ALLOWED!>in<!> T> T.y: Int
    get() = 1

/* GENERATED_FIR_TAGS: functionDeclaration, getter, in, integerLiteral, nullableType, out, propertyDeclaration,
propertyWithExtensionReceiver, typeParameter */
