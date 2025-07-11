// RUN_PIPELINE_TILL: BACKEND
// LANGUAGE: +EnumEntries +PrioritizedEnumEntries
// WITH_STDLIB
// FIR_DUMP

package pckg

enum class A {
    ;

    companion object
}

val A.Companion.entries: Int get() = 0

fun test() {
    A.entries
    A.Companion.entries

    with(A) {
        this.entries
        entries
    }

    with(A.Companion) {
        entries
    }

    val aCompanion = A.Companion
    aCompanion.entries
}

/* GENERATED_FIR_TAGS: companionObject, enumDeclaration, functionDeclaration, getter, integerLiteral, lambdaLiteral,
localProperty, objectDeclaration, propertyDeclaration, propertyWithExtensionReceiver, thisExpression */
