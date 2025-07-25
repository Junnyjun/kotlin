// RUN_PIPELINE_TILL: FRONTEND

// It's relevant only for Java constructor calls

// FILE: J.java

public class J<T extends Integer>  {}

// FILE: main.kt

import java.util.ArrayList

class Foo(val attributes: Map<String, String>)

class A<R>

class Bar<T, K: Any> {
    val foos1 = ArrayList<Foo>()
    val foos2 = ArrayList<Foo?>()
    val foos3 = ArrayList<A<Foo>>()
    val foos4 = ArrayList<A<Foo>?>()
    val foos5 = ArrayList<A<Foo?>?>()
    val foos6 = ArrayList<A<Foo?>>()
    val foos7 = ArrayList<T>()
    val foos8 = ArrayList<T?>()
    val foos9 = ArrayList<K>()
    val foos10 = ArrayList<K?>()
    val foos11 = ArrayList<A<K?>>()
    val foos12 = ArrayList<A<K>>()
    val foos13 = ArrayList<A<T>>()
    val foos14 = ArrayList<A<T>?>()
    val foos15 = ArrayList<A<T?>>()

    val foos16 = J<<!UPPER_BOUND_VIOLATED!>Foo<!>>()
    val foos17 = J<<!UPPER_BOUND_VIOLATED!>Foo?<!>>()
    val foos18 = J<<!UPPER_BOUND_VIOLATED!>T<!>>()
    val foos19 = J<<!UPPER_BOUND_VIOLATED!>T?<!>>()
}

/* GENERATED_FIR_TAGS: classDeclaration, flexibleType, javaFunction, javaType, nullableType, primaryConstructor,
propertyDeclaration, typeConstraint, typeParameter */
