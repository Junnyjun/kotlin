// RUN_PIPELINE_TILL: FRONTEND
// FIR_IDENTICAL
package b
//+JDK

import java.util.*
import java.util.Collections.*

fun foo(list: List<String>) : String {
    val w : String = max(list, comparator<String?> {o1, o2 -> 1
    })
    return w
}

//from library
fun <T> comparator(fn: (T,T) -> Int): Comparator<T> {<!NO_RETURN_IN_FUNCTION_WITH_BLOCK_BODY!>}<!>

/* GENERATED_FIR_TAGS: flexibleType, functionDeclaration, functionalType, integerLiteral, javaFunction, lambdaLiteral,
localProperty, nullableType, propertyDeclaration, typeParameter */
