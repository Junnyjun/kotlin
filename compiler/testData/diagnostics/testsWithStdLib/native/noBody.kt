// RUN_PIPELINE_TILL: BACKEND
// FIR_IDENTICAL
import kotlin.jvm.*

external fun foo()

class C {
    external fun foo()

    companion object {
        external fun foo()
    }
}

object O {
    external fun foo()
}

fun test() {
    class Local {
        external fun foo()
    }

    object {
        external fun foo()
    }
}

/* GENERATED_FIR_TAGS: anonymousObjectExpression, classDeclaration, companionObject, external, functionDeclaration,
localClass, objectDeclaration */
