// This file was generated automatically. See  generateTestDataForTypeScriptWithFileExport.kt
// DO NOT MODIFY IT MANUALLY.

// CHECK_TYPESCRIPT_DECLARATIONS
// RUN_PLAIN_BOX_FUNCTION
// WITH_STDLIB
// SKIP_NODE_JS
// INFER_MAIN_MODULE

// TODO fix statics export in DCE-driven mode
// SKIP_DCE_DRIVEN

// MODULE: JS_TESTS
// FILE: f1.kt

@file:JsExport

import kotlin.js.Promise


fun provideList(): List<Int> = listOf(1, 2, 3)


fun provideMutableList(): MutableList<Int> = mutableListOf(4, 5, 6)


fun provideSet(): Set<Int> = setOf(1, 2, 3)


fun provideMutableSet(): MutableSet<Int> = mutableSetOf(4, 5, 6)


fun provideMap(): Map<String, Int> = mapOf("a" to 1, "b" to 2, "c" to 3)


fun provideMutableMap(): MutableMap<String, Int> = mutableMapOf("d" to 4, "e" to 5, "f" to 6)


fun consumeList(list: List<Int>) = list.toString() == "[1, 2, 3]"


fun consumeMutableList(list: MutableList<Int>): Boolean {
    list.add(7)
    return list.toString() == "[4, 5, 6, 7]"
}


fun consumeSet(list: Set<Int>) = list.toString() == "[1, 2, 3]"


fun consumeMutableSet(list: MutableSet<Int>): Boolean {
    list.add(7)
    return list.toString() == "[4, 5, 6, 7]"
}


fun consumeMap(map: Map<String, Int>) = map.toString() == "{a=1, b=2, c=3}"


fun consumeMutableMap(map: MutableMap<String, Int>): Boolean {
    map["g"] = 7
    return map.toString() == "{d=4, e=5, f=6, g=7}"
}


fun provideListAsync(): Promise<List<Int>> = Promise.resolve(listOf(1, 2, 3))