// RUN_PIPELINE_TILL: FRONTEND
//KT-1127 Wrong type computed for Arrays.asList()

package d

fun <T> asList(t: T) : List<T>? {<!NO_RETURN_IN_FUNCTION_WITH_BLOCK_BODY!>}<!>

fun main() {
    val list : List<String> = <!INITIALIZER_TYPE_MISMATCH!>asList("")<!>
}

/* GENERATED_FIR_TAGS: functionDeclaration, localProperty, nullableType, propertyDeclaration, stringLiteral,
typeParameter */
