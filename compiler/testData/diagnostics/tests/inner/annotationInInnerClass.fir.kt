// RUN_PIPELINE_TILL: FRONTEND
class Outer {
    inner class Inner {
        annotation <!NESTED_CLASS_NOT_ALLOWED!>class TestNestedAnnotation<!>
    }
}

/* GENERATED_FIR_TAGS: annotationDeclaration, classDeclaration, inner, nestedClass */
