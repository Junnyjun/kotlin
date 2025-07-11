// IGNORE_FIR_DIAGNOSTICS
// RUN_PIPELINE_TILL: FIR2IR
// LANGUAGE: +MultiPlatformProjects

// MODULE: common
// FILE: common.kt
expect val <!REDECLARATION, REDECLARATION{JVM}!>x1<!>: Int
expect val <!REDECLARATION, REDECLARATION{JVM}!>x1<!>: Int

expect val <!AMBIGUOUS_ACTUALS{JVM}, EXPECT_AND_ACTUAL_IN_THE_SAME_MODULE!>x2<!>: Int
val <!ACTUAL_MISSING{JVM}, EXPECT_AND_ACTUAL_IN_THE_SAME_MODULE, REDECLARATION{JVM}!>x2<!> = 2

expect val <!AMBIGUOUS_ACTUALS{JVM}!>x3<!>: Int

// MODULE: jvm()()(common)
// FILE: main.kt
actual val <!AMBIGUOUS_EXPECTS!>x1<!> = 1

actual val <!REDECLARATION!>x2<!> = 2

actual val <!REDECLARATION!>x3<!> = 3
val <!ACTUAL_MISSING, REDECLARATION!>x3<!> = 3

/* GENERATED_FIR_TAGS: actual, expect, integerLiteral, propertyDeclaration */
