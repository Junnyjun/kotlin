// IGNORE_FIR_DIAGNOSTICS
// RUN_PIPELINE_TILL: FRONTEND
// LANGUAGE: +MultiPlatformProjects
// WITH_STDLIB

// MODULE: common
// FILE: common.kt
expect class N

@JvmInline
value class A(val n: N)

expect class U

@JvmInline
value class B(val u: U)

// MODULE: jvm()()(common)
// FILE: jvm.kt
<!ACTUAL_TYPE_ALIAS_TO_NOTHING!>actual typealias <!NO_ACTUAL_CLASS_MEMBER_FOR_EXPECTED_CLASS!>N<!> = Nothing<!>

actual typealias U = Unit

/* GENERATED_FIR_TAGS: actual, classDeclaration, expect, primaryConstructor, propertyDeclaration, typeAliasDeclaration,
value */
