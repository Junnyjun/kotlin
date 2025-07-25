// IGNORE_FIR_DIAGNOSTICS
// RUN_PIPELINE_TILL: BACKEND
// WITH_STDLIB
// MODULE: m1-common
// FILE: common.kt

public expect abstract class AbstractMutableMap<K, V> : MutableMap<K, V> {
    override val values: MutableCollection<V>
}

// MODULE: m2-jvm()()(m1-common)
// FILE: jvm.kt

import java.util.AbstractMap

public actual abstract class <!EXPECT_ACTUAL_INCOMPATIBLE_CLASS_SCOPE, EXPECT_ACTUAL_INCOMPATIBLE_CLASS_SCOPE, EXPECT_ACTUAL_INCOMPATIBLE_CLASS_SCOPE, EXPECT_ACTUAL_INCOMPATIBLE_CLASS_SCOPE, EXPECT_ACTUAL_INCOMPATIBLE_CLASS_SCOPE, EXPECT_ACTUAL_INCOMPATIBLE_CLASS_SCOPE, EXPECT_ACTUAL_INCOMPATIBLE_CLASS_SCOPE, EXPECT_ACTUAL_INCOMPATIBLE_CLASS_SCOPE, EXPECT_ACTUAL_INCOMPATIBLE_CLASS_SCOPE, EXPECT_ACTUAL_INCOMPATIBLE_CLASS_SCOPE!>AbstractMutableMap<!><K, V>() : MutableMap<K, V>, AbstractMap<K, V>()

/* GENERATED_FIR_TAGS: actual, classDeclaration, expect, nullableType, override, primaryConstructor, propertyDeclaration,
typeParameter */
