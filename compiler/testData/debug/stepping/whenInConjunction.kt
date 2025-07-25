
// FILE: test.kt

fun foo(n: Number) {
    if (n.toInt() > 1 && when (n) {
            is Float -> true
            else -> false
        }) {
    }
    if (when (n) {
            is Float -> true
            else -> false
        } && n.toInt() > 1) {
    }
}

fun box() {
    foo(2.0f)
}

// EXPECTATIONS JVM_IR
// test.kt:18 box
// test.kt:5 foo
// test.kt:6 foo
// test.kt:5 foo
// test.kt:10 foo
// test.kt:11 foo
// test.kt:10 foo
// test.kt:13 foo
// test.kt:15 foo
// test.kt:19 box

// EXPECTATIONS JS_IR
// test.kt:18 box
// test.kt:5 foo
// test.kt:6 foo
// test.kt:6 foo
// test.kt:11 foo
// test.kt:11 foo
// test.kt:10 foo
// test.kt:13 foo
// test.kt:15 foo
// test.kt:19 box

// EXPECTATIONS WASM
// test.kt:18 $box (8, 4)
// test.kt:5 $foo (8, 10, 20, 8, 31)
// test.kt:6 $foo (12, 24)
// test.kt:5 $foo (25)
// test.kt:10 $foo (14)
// test.kt:11 $foo (12, 24)
// test.kt:13 $foo (13, 15, 25, 13)
// test.kt:15 $foo (1)
// test.kt:19 $box (1)
