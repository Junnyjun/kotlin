// RUN_PIPELINE_TILL: FRONTEND
fun interface Bar {
    fun invoke(): String
}

operator fun Bar.plus(b: Bar): String = invoke() + b.invoke()

fun box(): String {
    return { "O" } <!DEBUG_INFO_UNRESOLVED_WITH_TARGET, UNRESOLVED_REFERENCE_WRONG_RECEIVER!>+<!> { "K" }
}

/* GENERATED_FIR_TAGS: additiveExpression, funInterface, funWithExtensionReceiver, functionDeclaration,
interfaceDeclaration, lambdaLiteral, operator, samConversion, stringLiteral */
