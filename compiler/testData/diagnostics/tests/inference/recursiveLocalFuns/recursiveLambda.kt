// RUN_PIPELINE_TILL: FRONTEND
fun foo() {
    fun bar() = {
        <!TYPECHECKER_HAS_RUN_INTO_RECURSIVE_PROBLEM_ERROR!><!DEBUG_INFO_MISSING_UNRESOLVED!>bar<!>()<!>
    }
}

/* GENERATED_FIR_TAGS: functionDeclaration, lambdaLiteral, localFunction */
