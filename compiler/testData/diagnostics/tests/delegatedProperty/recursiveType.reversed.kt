// RUN_PIPELINE_TILL: FRONTEND
// DIAGNOSTICS: -UNUSED_PARAMETER
// NI_EXPECTED_FILE

import kotlin.reflect.KProperty

val a by <!RECURSION_IN_IMPLICIT_TYPES, TYPECHECKER_HAS_RUN_INTO_RECURSIVE_PROBLEM!>a<!>

val b by Delegate(<!TYPECHECKER_HAS_RUN_INTO_RECURSIVE_PROBLEM!>b<!>)

val c by <!RECURSION_IN_IMPLICIT_TYPES, TYPECHECKER_HAS_RUN_INTO_RECURSIVE_PROBLEM!>d<!>
val d by c

class Delegate(i: Int) {
  operator fun getValue(t: Any?, p: KProperty<*>): Int {
    return 1
  }
}

/* GENERATED_FIR_TAGS: classDeclaration, functionDeclaration, integerLiteral, nullableType, operator, primaryConstructor,
propertyDeclaration, propertyDelegate, starProjection */
