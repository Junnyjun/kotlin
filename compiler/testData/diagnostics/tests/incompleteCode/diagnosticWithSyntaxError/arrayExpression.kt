// RUN_PIPELINE_TILL: FRONTEND
package bar

fun main() {
    class Some

    <!DEBUG_INFO_MISSING_UNRESOLVED!><!NO_COMPANION_OBJECT!>Some<!>[<!SYNTAX!><!>]<!> <!DEBUG_INFO_MISSING_UNRESOLVED!>names<!> <!DEBUG_INFO_MISSING_UNRESOLVED!><!SYNTAX!>=<!> ["ads"]<!>
}

/* GENERATED_FIR_TAGS: classDeclaration, functionDeclaration, localClass, stringLiteral */
