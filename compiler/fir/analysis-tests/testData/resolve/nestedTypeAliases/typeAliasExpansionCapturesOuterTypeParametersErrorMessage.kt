// RUN_PIPELINE_TILL: FRONTEND
// RENDER_DIAGNOSTICS_FULL_TEXT

class C<T> {
    inner class Inner
    typealias TA = <!TYPEALIAS_EXPANSION_CAPTURES_OUTER_TYPE_PARAMETERS!>Inner<!>
}

/* GENERATED_FIR_TAGS: classDeclaration, inner, nullableType, typeAliasDeclaration, typeParameter */
