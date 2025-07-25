// RUN_PIPELINE_TILL: BACKEND
// FULL_JDK

fun foo() {
    val y = listOf("Alpha", "Beta")
    val x = LinkedHashSet<String>().apply {
        addAll(y)
    }

    val z = ArrayList<String>()
    z.addAll(y)
    z.add("Omega")
}

/* GENERATED_FIR_TAGS: functionDeclaration, lambdaLiteral, localProperty, propertyDeclaration, stringLiteral */
