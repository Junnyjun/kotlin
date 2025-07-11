// RUN_PIPELINE_TILL: BACKEND
// FULL_JDK
// FILE: MyIterable.java
public interface MyIterable<T> extends Iterable<T> {}

// FILE: test.kt
interface UseIterable : MyIterable<String> {
    fun test() {
        val it = iterator()
        val split = spliterator()
    }
}

fun test(some: Iterable<String>) {
    val it = some.iterator()
    val split = some.spliterator()
}

/* GENERATED_FIR_TAGS: flexibleType, functionDeclaration, interfaceDeclaration, javaType, localProperty,
propertyDeclaration */
