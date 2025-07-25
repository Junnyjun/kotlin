// RUN_PIPELINE_TILL: BACKEND
// FIR_IDENTICAL
package util

@Target(AnnotationTarget.TYPE)
annotation class Anno(val str: String)

const val prop = "str"

fun foo() {
    class Local {
        val foo get() = bar
        var bar: @Anno("bar $prop") List<@Anno("nested bar $prop") Collection<@Anno("nested nested bar $prop") Int>>? = null
        var foo2 = bar
    }
}

/* GENERATED_FIR_TAGS: annotationDeclaration, classDeclaration, const, functionDeclaration, getter, localClass,
nullableType, primaryConstructor, propertyDeclaration, stringLiteral */
