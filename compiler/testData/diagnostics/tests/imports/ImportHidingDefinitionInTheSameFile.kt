// RUN_PIPELINE_TILL: BACKEND
// FIR_IDENTICAL
// ResolveSession.resolveToDescriptor used to crash on such code, so we just check that it's ok now
import kotlin.Double
import kotlin.collections.List
import kotlin.arrayOfNulls

class List {}

fun arrayOfNulls(){}
val arrayOfNulls: Int = 0

/* GENERATED_FIR_TAGS: classDeclaration, functionDeclaration, integerLiteral, propertyDeclaration */
