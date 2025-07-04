// RUN_PIPELINE_TILL: FRONTEND
fun test_1() {
    val list = buildList {
        add("")
    }
    takeList(list)
}

fun test_2() {
    val list = myBuildList {
        add("")
    }
    takeList(list)
}

fun <E> myBuildList(@<!OPT_IN_USAGE_ERROR!>BuilderInference<!> builderAction: MutableList<E>.() -> Unit): List<E> {
    return ArrayList<E>().apply(builderAction)
}

fun takeList(list: List<String>) {}

/* GENERATED_FIR_TAGS: functionDeclaration, functionalType, lambdaLiteral, localProperty, nullableType,
propertyDeclaration, stringLiteral, typeParameter, typeWithExtension */
