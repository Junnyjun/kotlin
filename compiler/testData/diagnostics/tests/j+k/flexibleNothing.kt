// RUN_PIPELINE_TILL: BACKEND
// FIR_IDENTICAL

// FILE: TestClass.java
import org.jetbrains.annotations.Nullable;
public class TestClass {
    public <T> T set(@Nullable String key, @Nullable T t) {
        return t;
    }
}

// FILE: main.kt
fun run() {
    val testClass = TestClass()
    // inferred as `set<Nothing>()`, return type is Nothing!
    testClass.set("test", null)

    // Should not be unreachable
    run()
}

/* GENERATED_FIR_TAGS: flexibleType, functionDeclaration, javaFunction, javaType, localProperty, nullableType,
propertyDeclaration, stringLiteral */
