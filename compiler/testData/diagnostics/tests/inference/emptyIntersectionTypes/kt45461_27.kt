// RUN_PIPELINE_TILL: BACKEND
// FIR_IDENTICAL
class Foo<T>

class Bar<T> {
    fun <S : T> takeFoo(foo: Foo<in S>) {}
}

class Inv<O>

fun <K : Inv<Inv<Number>>> main() {
    val foo = Foo<K>()
    Bar<Inv<in Inv<in Int>>>().takeFoo(foo) // error in 1.3.72, no error in 1.4.31
}

/* GENERATED_FIR_TAGS: classDeclaration, functionDeclaration, inProjection, intersectionType, localProperty,
nullableType, propertyDeclaration, typeConstraint, typeParameter */
