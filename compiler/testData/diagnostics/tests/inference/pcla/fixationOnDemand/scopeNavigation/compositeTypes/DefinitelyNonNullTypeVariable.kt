// RUN_PIPELINE_TILL: FRONTEND
fun test() {
    val resultA = pcla { otvOwner ->
        otvOwner.constrain(ScopeOwner.Nullable())
        // expected: ScopeOwner
        <!DEBUG_INFO_EXPRESSION_TYPE("TypeVariable(OT) & Any")!>otvOwner.provide()<!>
        // should fix OTv := ScopeOwner? for scope navigation
        otvOwner.provide().<!DEBUG_INFO_ELEMENT_WITH_ERROR_TYPE, DEBUG_INFO_UNRESOLVED_WITH_TARGET, UNRESOLVED_REFERENCE!>function<!>()
        // expected: Interloper </: ScopeOwner?
        otvOwner.constrain(Interloper)
    }
    // expected: ScopeOwner?
    <!DEBUG_INFO_EXPRESSION_TYPE("BaseType?")!>resultA<!>
}


class TypeVariableOwner<T> {
    fun constrain(subtypeValue: T) {}
    fun provide(): T & Any = null!!
}

fun <OT> pcla(lambda: (TypeVariableOwner<OT>) -> Unit): OT = null!!

interface BaseType

class ScopeOwner: BaseType {
    fun function() {}
    companion object {
        fun Nullable(): ScopeOwner? = null
    }
}

object Interloper: BaseType

/* GENERATED_FIR_TAGS: checkNotNullCall, classDeclaration, companionObject, dnnType, functionDeclaration, functionalType,
interfaceDeclaration, lambdaLiteral, localProperty, nullableType, objectDeclaration, propertyDeclaration, typeParameter */
