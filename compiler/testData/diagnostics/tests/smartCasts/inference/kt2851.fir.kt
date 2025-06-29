// RUN_PIPELINE_TILL: BACKEND
//KT-2851 Type inference failed passing in not-null after smart-cast value in Pair
package a

fun main() {
    val value: String? = ""
    if (value != null) {
        foo(Pair("val", value))
        foo(Pair("val", value<!UNNECESSARY_NOT_NULL_ASSERTION!>!!<!>))
        foo(Pair<String, String>("val", value))
    }
}

fun foo(map: Pair<String, String>) {}


//from library
public class Pair<out A, out B> (
        public val first: A,
        public val second: B
)

/* GENERATED_FIR_TAGS: checkNotNullCall, classDeclaration, equalityExpression, functionDeclaration, ifExpression,
localProperty, nullableType, out, primaryConstructor, propertyDeclaration, smartcast, stringLiteral, typeParameter */
