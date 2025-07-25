// RUN_PIPELINE_TILL: FRONTEND
// LANGUAGE: +ProhibitNonConstValuesAsVarargsInAnnotations

annotation class AnnE(val i: MyEnum)

@AnnE(<!ANNOTATION_ARGUMENT_MUST_BE_ENUM_CONST!>e<!>)
class Test

val e: MyEnum = MyEnum.A

enum class MyEnum {
    A
}

@AnnE(<!ANNOTATION_ARGUMENT_MUST_BE_CONST, TYPE_MISMATCH!>Test()<!>)
class Test2

/* GENERATED_FIR_TAGS: annotationDeclaration, classDeclaration, enumDeclaration, enumEntry, primaryConstructor,
propertyDeclaration */
