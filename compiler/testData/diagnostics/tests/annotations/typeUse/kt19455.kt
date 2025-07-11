// RUN_PIPELINE_TILL: BACKEND
// FIR_IDENTICAL
// LANGUAGE: +ProperCheckAnnotationsTargetInTypeUsePositions
// ISSUE: KT-19455

package test

@Target(AnnotationTarget.TYPE)
annotation class TypeAnn

open class TypeToken<T>

object Test : TypeToken<@TypeAnn String>() // (1)

val test = object : TypeToken<@TypeAnn String>() {} // (2)

/* GENERATED_FIR_TAGS: annotationDeclaration, anonymousObjectExpression, classDeclaration, nullableType,
objectDeclaration, propertyDeclaration, typeParameter */
