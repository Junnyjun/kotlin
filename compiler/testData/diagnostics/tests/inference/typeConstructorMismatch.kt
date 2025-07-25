// RUN_PIPELINE_TILL: FRONTEND
package typeConstructorMismatch
//+JDK

import java.util.*

fun test(set: Set<String>) {
    elemAndList("2", <!TYPE_MISMATCH!>set<!>)

    "".elemAndListWithReceiver("", <!TYPE_MISMATCH!>set<!>)
}

fun <R> elemAndList(r: R, t: List<R>): R = r
fun <R> R.elemAndListWithReceiver(r: R, t: List<R>): R = r

/* GENERATED_FIR_TAGS: funWithExtensionReceiver, functionDeclaration, nullableType, stringLiteral, typeParameter */
