// RUN_PIPELINE_TILL: BACKEND
// FIR_IDENTICAL
// NI_EXPECTED_FILE

interface In<in E>
class A : In<A>
class B : In<B>
fun <T> select(x: T, y: T) = x ?: y

// Return type should be In<*> nor In<out Any?>
fun foobar(a: A, b: B) = select(a, b)

/* GENERATED_FIR_TAGS: classDeclaration, elvisExpression, functionDeclaration, in, interfaceDeclaration,
intersectionType, nullableType, typeParameter */
