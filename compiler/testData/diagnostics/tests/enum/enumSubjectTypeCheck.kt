// RUN_PIPELINE_TILL: BACKEND
// See KT-14705

enum class En { A, B, С }

fun foo() {
    // nullable variable
    val en2: Any? = En.A
    if (en2 is En) {
        when (<!DEBUG_INFO_SMARTCAST!>en2<!>) {
            En.A -> {}
            En.B -> {}
            En.С -> {}
        }
    }

    // not nullable variable
    val en1: Any = En.A
    if (en1 is En) {
        when (<!DEBUG_INFO_SMARTCAST!>en1<!>) {
            En.A -> {}
            En.B -> {}
            En.С -> {}
        }
    }
}

enum class En2 { D, E, F }

fun useEn(x: En) = x
fun useEn2(x: En2) = x

fun bar(x: Any) {
    if (x is En && <!USELESS_IS_CHECK!>x is En2<!>) {
        when (<!DEBUG_INFO_SMARTCAST!>x<!>) {
            En.A -> useEn(<!DEBUG_INFO_SMARTCAST!>x<!>)
            En2.D -> useEn2(<!DEBUG_INFO_SMARTCAST!>x<!>)
            else -> {}
        }
    }
}

/* GENERATED_FIR_TAGS: andExpression, enumDeclaration, enumEntry, equalityExpression, functionDeclaration, ifExpression,
intersectionType, isExpression, localProperty, nullableType, propertyDeclaration, smartcast, whenExpression,
whenWithSubject */
