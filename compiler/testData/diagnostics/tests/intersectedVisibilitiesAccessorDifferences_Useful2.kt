// RUN_PIPELINE_TILL: FRONTEND
// ISSUE: KT-66717

interface IVar {
    var z: Int
}

abstract class WithVarPrivateSet {
    final var z: Int = 42
        private set
}

class G2 : WithVarPrivateSet(), IVar {
    fun foo() {
        z = 5
    }
}

fun main() {
    G2().foo()
}

/* GENERATED_FIR_TAGS: assignment, classDeclaration, functionDeclaration, integerLiteral, interfaceDeclaration,
propertyDeclaration */
