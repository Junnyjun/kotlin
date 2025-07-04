// RUN_PIPELINE_TILL: FRONTEND
// FIR_IDENTICAL
//KT-1248 Control visibility of overrides needed
package kt1248

interface ParseResult<out T> {
    public val success : Boolean
    public val value : T
}

class Success<T>(<!CANNOT_WEAKEN_ACCESS_PRIVILEGE!>internal<!> override val value : T) : ParseResult<T> {
    <!CANNOT_WEAKEN_ACCESS_PRIVILEGE!>internal<!> override val success : Boolean = true
}

/* GENERATED_FIR_TAGS: classDeclaration, interfaceDeclaration, nullableType, out, override, primaryConstructor,
propertyDeclaration, typeParameter */
