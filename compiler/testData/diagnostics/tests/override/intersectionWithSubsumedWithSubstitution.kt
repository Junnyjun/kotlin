// RUN_PIPELINE_TILL: BACKEND
// FIR_IDENTICAL

class KtFirPsiJavaClassSymbol : KtFirNamedClassOrObjectSymbolBase(), KtFirPsiSymbol<String, String>
open class KtFirNamedClassOrObjectSymbolBase : KtNamedClassOrObjectSymbol(), KtFirSymbol<String>
abstract class KtNamedClassOrObjectSymbol : KtLifetimeOwner

interface KtFirPsiSymbol<P, S> : KtFirSymbol<S>

interface KtLifetimeOwner {
    val token: String
}

interface KtSymbol : KtLifetimeOwner

interface KtFirSymbol<out S> : KtSymbol, KtLifetimeOwner {
    override val token: String get() = ""
}

/* GENERATED_FIR_TAGS: classDeclaration, getter, interfaceDeclaration, nullableType, out, override, propertyDeclaration,
stringLiteral, typeParameter */
