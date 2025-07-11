// RUN_PIPELINE_TILL: FRONTEND
// CHECK_TYPE

fun simpleWhile(x: Int?, y0: Int) {
    var y = y0
    while (x!! == y) {
        checkSubtype<Int>(x)
        y++
    }
    checkSubtype<Int>(x)
}

fun whileWithBreak(x: Int?, y0: Int) {
    var y = y0
    while (x!! == y) {
        checkSubtype<Int>(x)
        break
    }
    checkSubtype<Int>(x)
}

fun whileWithNoCondition(x: Int?) {
    while (<!SYNTAX!><!>) {
        x!!
    }
    checkSubtype<Int>(<!ARGUMENT_TYPE_MISMATCH!>x<!>)
}

/* GENERATED_FIR_TAGS: assignment, break, checkNotNullCall, classDeclaration, equalityExpression,
funWithExtensionReceiver, functionDeclaration, functionalType, incrementDecrementExpression, infix, localProperty,
nullableType, propertyDeclaration, smartcast, typeParameter, typeWithExtension, whileLoop */
