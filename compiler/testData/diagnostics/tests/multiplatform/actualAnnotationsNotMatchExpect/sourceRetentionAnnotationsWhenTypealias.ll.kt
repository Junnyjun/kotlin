// LL_FIR_DIVERGENCE
// Bug KT-62886
// LL_FIR_DIVERGENCE
// IGNORE_FIR_DIAGNOSTICS
// RUN_PIPELINE_TILL: BACKEND
// WITH_STDLIB
// DIAGNOSTICS: -ACTUAL_TYPEALIAS_TO_SPECIAL_ANNOTATION
// MODULE: m1-common
// FILE: common.kt
@Retention(AnnotationRetention.SOURCE)
annotation class Ann

@Ann
expect class SourceAvailable {
    @Ann
    fun foo()
}

@Ann
expect annotation class FromLib

// MODULE: m1-jvm()()(m1-common)
// FILE: jvm.kt
class SourceAvailableImpl {
    fun foo() {}
}

actual typealias <!ACTUAL_ANNOTATIONS_NOT_MATCH_EXPECT!>SourceAvailable<!> = SourceAvailableImpl

actual typealias <!ACTUAL_ANNOTATIONS_NOT_MATCH_EXPECT!>FromLib<!> = kotlin.SinceKotlin

/* GENERATED_FIR_TAGS: actual, annotationDeclaration, classDeclaration, expect, functionDeclaration,
typeAliasDeclaration */
