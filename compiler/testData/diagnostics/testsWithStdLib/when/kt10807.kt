// RUN_PIPELINE_TILL: BACKEND
// FIR_IDENTICAL
import java.util.*
import kotlin.comparisons.compareBy
import kotlin.comparisons.nullsLast

class Foo(val a: String, val b: Int)

fun getComp(): Comparator<Foo?> =
        when {
            else -> nullsLast(compareBy({ it.a }, { it.b }))
        }

fun getCompInverted(): Comparator<Foo?> =
        nullsLast(
                when {
                    else -> compareBy({ it.a }, { it.b })
                }
        )

/* GENERATED_FIR_TAGS: classDeclaration, functionDeclaration, lambdaLiteral, nullableType, primaryConstructor,
propertyDeclaration, starProjection, whenExpression */
