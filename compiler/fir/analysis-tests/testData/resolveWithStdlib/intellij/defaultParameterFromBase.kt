// RUN_PIPELINE_TILL: BACKEND
import Derived.foo

open class Base {
    open fun foo(arg: Int, def: String = "") {}
}
object Derived : Base() {
    override fun foo(arg: Int, def: String) {}
}
fun test() {
    foo(42) // No value passed for parameter 'def'
}

/* GENERATED_FIR_TAGS: classDeclaration, functionDeclaration, integerLiteral, objectDeclaration, override, stringLiteral */
