// RUN_PIPELINE_TILL: FRONTEND
// FIR_IDENTICAL
// LANGUAGE: +BreakContinueInInlineLambdas


inline fun foo(crossinline block: () -> Unit) = block()


fun test1() {
    L1@ while (true) {
        foo { <!BREAK_OR_CONTINUE_JUMPS_ACROSS_FUNCTION_BOUNDARY!>break<!> }

        foo { <!BREAK_OR_CONTINUE_JUMPS_ACROSS_FUNCTION_BOUNDARY!>break@L1<!> }

        foo { <!BREAK_OR_CONTINUE_JUMPS_ACROSS_FUNCTION_BOUNDARY!>continue<!> }

        foo { <!BREAK_OR_CONTINUE_JUMPS_ACROSS_FUNCTION_BOUNDARY!>continue@L1<!> }
    }
}

/* GENERATED_FIR_TAGS: break, continue, crossinline, functionDeclaration, functionalType, inline, lambdaLiteral,
whileLoop */
