// RUN_PIPELINE_TILL: FRONTEND
// LANGUAGE: +AllowContractsForCustomFunctions +UseReturnsEffect
// OPT_IN: kotlin.contracts.ExperimentalContracts
// DIAGNOSTICS: -INVISIBLE_REFERENCE -INVISIBLE_MEMBER

import kotlin.contracts.*

fun isString(x: Any?): Boolean {
    contract {
        returns(true) implies (x is String)
    }
    return x is String
}

fun incorrectPartDoesntMatter(x: Any?) {
    if (isString(x) && <!CONSTANT_EXPECTED_TYPE_MISMATCH!>1<!>) {
        <!DEBUG_INFO_SMARTCAST!>x<!>.length
    }
    else {
        x.<!UNRESOLVED_REFERENCE!>length<!>
    }
}

/* GENERATED_FIR_TAGS: andExpression, contractConditionalEffect, contracts, functionDeclaration, ifExpression,
integerLiteral, isExpression, lambdaLiteral, nullableType, smartcast */
