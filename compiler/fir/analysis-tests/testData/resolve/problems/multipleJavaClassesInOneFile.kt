// RUN_PIPELINE_TILL: FRONTEND
// ISSUE: KT-4455

// FILE: foo/Some.java

package foo;

class Some {}

class Another {}

// FILE: main.kt
package foo

fun test() {
    val some = Some()
    val another = <!UNRESOLVED_REFERENCE!>Another<!>()
}

/* GENERATED_FIR_TAGS: functionDeclaration, javaFunction, javaType, localProperty, propertyDeclaration */
