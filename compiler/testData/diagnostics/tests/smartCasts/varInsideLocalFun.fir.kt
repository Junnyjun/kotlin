// RUN_PIPELINE_TILL: BACKEND
// Based on KT-8643
public class MyClass
{
    fun main() {
        var str: String? = null

        if (str != null)
            callback {
                // Nodoby writes str, smart cast is possible
                method1(str)
            }
    }

    inline fun callback(foo: () ->Unit) = foo()

    fun method1(str: String) = str
}

/* GENERATED_FIR_TAGS: classDeclaration, equalityExpression, functionDeclaration, functionalType, ifExpression, inline,
lambdaLiteral, localProperty, nullableType, propertyDeclaration, smartcast */
