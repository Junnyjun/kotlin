// RUN_PIPELINE_TILL: FRONTEND
package test

import test.TopLevelClass.NestedClass
import test.TopLevelClass.NestedClass.InNested
import test.TopLevelEnum.NestedEnum.NestedEntry
import test.TopLevelObject.inObject
import test.TopLevelEnum.E1

private class TopLevelClass {
    private class NestedClass {
        class InNested
    }

    fun test() {
        InNested()
    }
}

private enum class TopLevelEnum(private val e: NestedEnum) {
    E1(NestedEntry);

    private enum class NestedEnum {
        NestedEntry;
    }
}

private object TopLevelObject {
    fun inObject() {}
}

fun testAccess() {
    <!INVISIBLE_REFERENCE!>NestedClass<!>()
    E1
    <!INVISIBLE_REFERENCE!>InNested<!>()
    <!INVISIBLE_REFERENCE!>NestedEntry<!>
    inObject()
}

/* GENERATED_FIR_TAGS: classDeclaration, enumDeclaration, enumEntry, functionDeclaration, nestedClass, objectDeclaration,
primaryConstructor, propertyDeclaration */
