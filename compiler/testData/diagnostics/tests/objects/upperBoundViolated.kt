// RUN_PIPELINE_TILL: FRONTEND
// FIR_IDENTICAL
interface Trait<N : Number>

object O1 : Trait<Int>

object O2 : Trait<<!UPPER_BOUND_VIOLATED!>String<!>>

class C {
    companion object : Trait<<!UPPER_BOUND_VIOLATED!>IntRange<!>>
}

/* GENERATED_FIR_TAGS: classDeclaration, companionObject, interfaceDeclaration, objectDeclaration, typeConstraint,
typeParameter */
