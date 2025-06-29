// RUN_PIPELINE_TILL: BACKEND
// ISSUE: KT-41982

import kotlin.reflect.KProperty

class Delegate<R, T> {
    operator fun getValue(thisRef: R, property: KProperty<*>): T = null!!

    operator fun setValue(thisRef: R, property: KProperty<*>, value: T) {}
}

abstract class DelegateProvider<in Type>

fun <Type : Base, Base : DelegateProvider<Base>> Type.long(initializer: (() -> Long?)? = null): Delegate<Type, Long> = null!!

class Test : DelegateProvider<Any>() {
    var start by long { 0 }
}

/* GENERATED_FIR_TAGS: checkNotNullCall, classDeclaration, funWithExtensionReceiver, functionDeclaration, functionalType,
in, lambdaLiteral, nullableType, operator, propertyDeclaration, propertyDelegate, setter, starProjection, typeConstraint,
typeParameter */
