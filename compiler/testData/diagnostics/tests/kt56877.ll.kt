// LL_FIR_DIVERGENCE
// See KT-73392
// LL_FIR_DIVERGENCE
// RUN_PIPELINE_TILL: FRONTEND
@file:OptIn(ExperimentalContracts::class)

import kotlin.contracts.*

class Success : Result()

open class Result {
    val someProperty = run { 10 }

    fun isSuccess1(): Boolean {
        contract {
            returns(true) implies (this@Result is Success)
        }
        return this@Result is Success
    }

    fun isSuccess2(): Boolean {
        contract {
            // implicit type ref
            // ERROR CLASS: Cannot calculate return type during full-body resolution (local class/object?)
            <!ERROR_IN_CONTRACT_DESCRIPTION!>returns(true) implies (this@Result.someProperty == 10)<!>
        }
        return this@Result.someProperty == 10
    }
}

fun Result.isSuccess3(): Boolean {
    contract {
        returns(true) implies (this@isSuccess3 is Success)
    }
    return this@isSuccess3 is Success
}

fun Result.isSuccess4(): Boolean {
    contract {
        // implicit type ref
        // ERROR CLASS: Cannot calculate return type during full-body resolution (local class/object?)
        <!ERROR_IN_CONTRACT_DESCRIPTION!>returns(true) implies (this@isSuccess4.someProperty == 10)<!>
    }
    return this@isSuccess4.someProperty == 10
}

/* GENERATED_FIR_TAGS: annotationUseSiteTargetFile, classDeclaration, classReference, contractConditionalEffect,
contracts, equalityExpression, funWithExtensionReceiver, functionDeclaration, integerLiteral, isExpression,
lambdaLiteral, propertyDeclaration, thisExpression */
