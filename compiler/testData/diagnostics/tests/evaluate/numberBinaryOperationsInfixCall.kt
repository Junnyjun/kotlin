// RUN_PIPELINE_TILL: FRONTEND
fun fooInt(p: Int) = p
fun fooLong(p: Long) = p
fun fooByte(p: Byte) = p
fun fooShort(p: Short) = p

fun test() {
    fooInt(1 <!INFIX_MODIFIER_REQUIRED!>plus<!> 1)
    fooByte(<!INTEGER_OPERATOR_RESOLVE_WILL_CHANGE!>1 <!INFIX_MODIFIER_REQUIRED!>plus<!> 1<!>)
    fooLong(1 <!INFIX_MODIFIER_REQUIRED!>plus<!> 1)
    fooShort(<!INTEGER_OPERATOR_RESOLVE_WILL_CHANGE!>1 <!INFIX_MODIFIER_REQUIRED!>plus<!> 1<!>)

    fooInt(1 <!INFIX_MODIFIER_REQUIRED!>times<!> 1)
    fooByte(<!INTEGER_OPERATOR_RESOLVE_WILL_CHANGE!>1 <!INFIX_MODIFIER_REQUIRED!>times<!> 1<!>)
    fooLong(1 <!INFIX_MODIFIER_REQUIRED!>times<!> 1)
    fooShort(<!INTEGER_OPERATOR_RESOLVE_WILL_CHANGE!>1 <!INFIX_MODIFIER_REQUIRED!>times<!> 1<!>)

    fooInt(1 <!INFIX_MODIFIER_REQUIRED!>div<!> 1)
    fooByte(<!INTEGER_OPERATOR_RESOLVE_WILL_CHANGE!>1 <!INFIX_MODIFIER_REQUIRED!>div<!> 1<!>)
    fooLong(1 <!INFIX_MODIFIER_REQUIRED!>div<!> 1)
    fooShort(<!INTEGER_OPERATOR_RESOLVE_WILL_CHANGE!>1 <!INFIX_MODIFIER_REQUIRED!>div<!> 1<!>)

    fooInt(1 <!INFIX_MODIFIER_REQUIRED!>rem<!> 1)
    fooByte(<!INTEGER_OPERATOR_RESOLVE_WILL_CHANGE!>1 <!INFIX_MODIFIER_REQUIRED!>rem<!> 1<!>)
    fooLong(1 <!INFIX_MODIFIER_REQUIRED!>rem<!> 1)
    fooShort(<!INTEGER_OPERATOR_RESOLVE_WILL_CHANGE!>1 <!INFIX_MODIFIER_REQUIRED!>rem<!> 1<!>)
}

/* GENERATED_FIR_TAGS: functionDeclaration, integerLiteral */
