// RUN_PIPELINE_TILL: BACKEND
// FIR_IDENTICAL
// DIAGNOSTICS: -UNUSED_PARAMETER
// ISSUE: KT-32249

import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class Wrapper<T>(val name: String, val defaultValue: T)

private fun <T> wrapper(defaultValue: T) = object : ReadOnlyProperty<Any, Wrapper<T>> {
    override fun getValue(thisRef: Any, property: KProperty<*>): Wrapper<T> = null!!
}

object Foo {
    val x by wrapper(true)
    val y: Wrapper<Boolean> by wrapper(true)
}

/* GENERATED_FIR_TAGS: anonymousObjectExpression, checkNotNullCall, classDeclaration, functionDeclaration, nullableType,
objectDeclaration, operator, override, primaryConstructor, propertyDeclaration, propertyDelegate, starProjection,
typeParameter */
