// RUN_PIPELINE_TILL: BACKEND
// FIR_IDENTICAL
// KT-128 Support passing only the last closure if all the other parameters have default values

fun div(c : String = "", f : () -> Unit) {}
fun f() {
    div { // Nothing passed, but could have been...
      // ...
    }

    div (c = "foo") { // More things could have been passed
      // ...
    }
}

/* GENERATED_FIR_TAGS: functionDeclaration, functionalType, lambdaLiteral, stringLiteral */
