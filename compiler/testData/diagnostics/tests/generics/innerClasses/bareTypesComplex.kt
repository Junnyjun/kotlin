// RUN_PIPELINE_TILL: FRONTEND
// FIR_IDENTICAL
open class SuperOuter<E> {
    inner open class SuperInner<F>
}

class DerivedOuter<G> : SuperOuter<G>() {
    inner class DerivedInner<H> : SuperOuter<G>.SuperInner<H>()
}

fun bare(x: SuperOuter<*>.SuperInner<*>, y: Any?) {
    if (<!USELESS_IS_CHECK!>x is SuperOuter.SuperInner<!>) return
    if (y is <!NO_TYPE_ARGUMENTS_ON_RHS!>SuperOuter.SuperInner<!>) {
        return
    }
}

/* GENERATED_FIR_TAGS: classDeclaration, functionDeclaration, ifExpression, inner, isExpression, nullableType,
starProjection, typeParameter */
