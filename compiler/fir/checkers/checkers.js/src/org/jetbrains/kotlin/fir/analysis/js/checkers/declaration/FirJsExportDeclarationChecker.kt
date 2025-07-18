/*
 * Copyright 2010-2023 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.fir.analysis.js.checkers.declaration

import org.jetbrains.kotlin.KtFakeSourceElementKind
import org.jetbrains.kotlin.config.LanguageFeature
import org.jetbrains.kotlin.descriptors.ClassKind
import org.jetbrains.kotlin.diagnostics.DiagnosticReporter
import org.jetbrains.kotlin.diagnostics.reportOn
import org.jetbrains.kotlin.fir.FirSession
import org.jetbrains.kotlin.fir.analysis.checkers.MppCheckerKind
import org.jetbrains.kotlin.fir.analysis.checkers.context.CheckerContext
import org.jetbrains.kotlin.fir.analysis.checkers.declaration.FirBasicDeclarationChecker
import org.jetbrains.kotlin.fir.analysis.checkers.getAnnotationFirstArgument
import org.jetbrains.kotlin.fir.analysis.checkers.getContainingClassSymbol
import org.jetbrains.kotlin.fir.analysis.checkers.isTopLevel
import org.jetbrains.kotlin.fir.analysis.diagnostics.js.FirJsErrors
import org.jetbrains.kotlin.fir.analysis.js.checkers.isExportedObject
import org.jetbrains.kotlin.fir.analysis.js.checkers.sanitizeName
import org.jetbrains.kotlin.fir.declarations.*
import org.jetbrains.kotlin.fir.declarations.utils.*
import org.jetbrains.kotlin.fir.expressions.FirLiteralExpression
import org.jetbrains.kotlin.fir.isEnumEntries
import org.jetbrains.kotlin.fir.resolve.fullyExpandedType
import org.jetbrains.kotlin.fir.resolve.toRegularClassSymbol
import org.jetbrains.kotlin.fir.resolve.toSymbol
import org.jetbrains.kotlin.fir.symbols.impl.FirCallableSymbol
import org.jetbrains.kotlin.fir.symbols.impl.FirClassSymbol
import org.jetbrains.kotlin.fir.symbols.impl.FirPropertyAccessorSymbol
import org.jetbrains.kotlin.fir.symbols.impl.FirTypeParameterSymbol
import org.jetbrains.kotlin.fir.types.*
import org.jetbrains.kotlin.js.common.RESERVED_KEYWORDS
import org.jetbrains.kotlin.js.common.SPECIAL_KEYWORDS
import org.jetbrains.kotlin.name.JsStandardClassIds
import org.jetbrains.kotlin.name.SpecialNames.DEFAULT_NAME_FOR_COMPANION_OBJECT
import org.jetbrains.kotlin.types.Variance

object FirJsExportDeclarationChecker : FirBasicDeclarationChecker(MppCheckerKind.Platform) {
    context(context: CheckerContext, reporter: DiagnosticReporter)
    override fun check(declaration: FirDeclaration) {
        if (!declaration.symbol.isExportedObject() || declaration !is FirMemberDeclaration) {
            return
        }

        fun checkTypeParameter(typeParameter: FirTypeParameterRef) {
            if (typeParameter is FirConstructedClassTypeParameterRef) {
                return
            }
            for (upperBound in typeParameter.symbol.resolvedBounds) {
                if (!upperBound.coneType.isExportable(context.session)) {
                    val source = upperBound.source ?: typeParameter.source ?: declaration.source
                    reporter.reportOn(source, FirJsErrors.NON_EXPORTABLE_TYPE, "upper bound", upperBound.coneType)
                }
            }
        }

        fun checkValueParameter(valueParameter: FirValueParameter) {
            val type = valueParameter.returnTypeRef.coneType
            if (!type.isExportable(context.session)) {
                val source = valueParameter.source ?: declaration.source
                reporter.reportOn(source, FirJsErrors.NON_EXPORTABLE_TYPE, "parameter", type)
            }
        }

        val hasJsName = declaration.hasAnnotation(JsStandardClassIds.Annotations.JsName, context.session)

        fun reportWrongExportedDeclaration(kind: String) {
            reporter.reportOn(declaration.source, FirJsErrors.WRONG_EXPORTED_DECLARATION, kind)
        }

        if (!context.languageVersionSettings.supportsFeature(LanguageFeature.AllowExpectDeclarationsInJsExport) && declaration.isExpect) {
            reportWrongExportedDeclaration("expect")
        }

        validateDeclarationOnConsumableName(declaration)

        when (declaration) {
            is FirFunction -> {
                if (declaration.isExternal && context.isTopLevel) {
                    reportWrongExportedDeclaration("external function")
                    return
                }
                for (typeParameter in declaration.typeParameters) {
                    checkTypeParameter(typeParameter)
                }

                if (declaration.symbol.isInlineWithReified) {
                    reportWrongExportedDeclaration("inline function with reified type parameters")
                    return
                }

                if (declaration.isSuspend) {
                    reportWrongExportedDeclaration("suspend function")
                    return
                }

                if (declaration is FirConstructor && !declaration.isPrimary && !hasJsName) {
                    reportWrongExportedDeclaration("secondary constructor without @JsName")
                }

                // Properties are checked instead of property accessors
                if (declaration is FirPropertyAccessor) {
                    return
                }

                val allCheckedParameters = declaration.contextParameters + declaration.valueParameters
                for (parameter in allCheckedParameters) {
                    checkValueParameter(parameter)
                }

                val returnType = declaration.returnTypeRef.coneType

                if (declaration !is FirConstructor && !returnType.isExportableReturn(context.session)) {
                    reporter.reportOn(declaration.source, FirJsErrors.NON_EXPORTABLE_TYPE, "return", returnType)
                }
            }

            is FirProperty -> {
                if (declaration.source?.kind == KtFakeSourceElementKind.PropertyFromParameter) {
                    return
                }

                if (declaration.isExternal && context.isTopLevel) {
                    reportWrongExportedDeclaration("external property")
                    return
                }

                if (declaration.isExtension) {
                    reportWrongExportedDeclaration("extension property")
                    return
                }

                val containingClass = declaration.getContainingClassSymbol() as? FirClassSymbol<*>
                val enumEntriesProperty = containingClass?.let(declaration::isEnumEntries) ?: false
                val returnType = declaration.returnTypeRef.coneType
                if (!enumEntriesProperty && !returnType.isExportable(context.session)) {
                    reporter.reportOn(declaration.source, FirJsErrors.NON_EXPORTABLE_TYPE, "property", returnType)
                }
            }

            is FirClass -> {
                if (declaration.isExternal && context.isTopLevel) {
                    val wrongDeclaration = when (declaration.classKind) {
                        ClassKind.CLASS -> "external class"
                        ClassKind.INTERFACE -> null // Exporting external interfaces is allowed. They are used to generate TypeScript definitions.
                        ClassKind.ENUM_CLASS -> "external enum class"
                        ClassKind.ENUM_ENTRY -> "external enum entry"
                        ClassKind.ANNOTATION_CLASS -> "external annotation class"
                        ClassKind.OBJECT -> "external object"
                    }
                    if (wrongDeclaration != null) {
                        reportWrongExportedDeclaration(wrongDeclaration)
                        return
                    }
                }

                for (typeParameter in declaration.typeParameters) {
                    checkTypeParameter(typeParameter)
                }

                val wrongDeclaration: String? = when (declaration.classKind) {
                    ClassKind.ANNOTATION_CLASS -> "annotation class"
                    ClassKind.CLASS -> when {
                        context.isInsideInterface -> "nested class inside exported interface"
                        declaration.isInlineOrValue -> "value class"
                        else -> null
                    }
                    else -> if (context.isInsideInterface && !declaration.status.isCompanion) {
                        "nested/inner declaration inside exported interface"
                    } else null
                }

                if (context.isInsideInterface && declaration.status.isCompanion && declaration.nameOrSpecialName != DEFAULT_NAME_FOR_COMPANION_OBJECT) {
                    reporter.reportOn(declaration.source, FirJsErrors.NAMED_COMPANION_IN_EXPORTED_INTERFACE)
                }

                if (wrongDeclaration != null) {
                    reportWrongExportedDeclaration(wrongDeclaration)
                }
            }

            is FirTypeAlias -> {
                reportWrongExportedDeclaration("typealias")
            }

            else -> {
                if (declaration.isExternal) {
                    reportWrongExportedDeclaration("external declaration")
                }
            }
        }
    }

    private val CheckerContext.isInsideInterface
        get(): Boolean {
            val parent = containingDeclarations.lastOrNull() as? FirClassSymbol<*>
            return parent != null && parent.isInterface
        }

    private val FirCallableSymbol<*>.isInlineWithReified: Boolean
        get() = when (this) {
            is FirPropertyAccessorSymbol -> {
                this.propertySymbol.isInlineWithReified
            }
            else -> typeParameterSymbols.any { it.isReified }
        }

    private fun ConeKotlinType.isExportableReturn(session: FirSession, currentlyProcessed: MutableSet<ConeKotlinType> = hashSetOf()) =
        isUnit || isExportable(session, currentlyProcessed)

    private fun ConeKotlinType.isExportableTypeArguments(
        session: FirSession,
        currentlyProcessed: MutableSet<ConeKotlinType>
    ): Boolean {
        if (typeArguments.isEmpty()) {
            return true
        }
        val symbol = toRegularClassSymbol(session) ?: return false
        for (i in 0 until typeArguments.size) {
            val parameter = symbol.typeParameterSymbols.getOrNull(i) ?: return false
            if (!typeArguments[i].isExportable(session, parameter, currentlyProcessed)) {
                return false
            }
        }
        return true
    }

    private fun ConeTypeProjection.isExportable(
        session: FirSession,
        declarationSite: FirTypeParameterSymbol,
        currentlyProcessed: MutableSet<ConeKotlinType> = hashSetOf(),
    ): Boolean {
        val typeFromProjection = when (kind) {
            ProjectionKind.INVARIANT, ProjectionKind.OUT, ProjectionKind.IN -> type
            ProjectionKind.STAR -> when (declarationSite.variance) {
                Variance.INVARIANT -> null
                else -> declarationSite.getProjectionForRawType(session, makeNullable = false)
            }
        } ?: return false

        return declarationSite.variance == Variance.OUT_VARIANCE && typeFromProjection.isUnit
                || typeFromProjection.isExportable(session, currentlyProcessed)
    }

    private fun ConeKotlinType.isExportable(
        session: FirSession,
        currentlyProcessed: MutableSet<ConeKotlinType> = hashSetOf(),
    ): Boolean {
        if (!currentlyProcessed.add(this)) {
            return true
        }

        val expandedType = fullyExpandedType(session)

        val isFunctionType = expandedType.isBasicFunctionType(session)
        val isExportableArgs = expandedType.isExportableTypeArguments(session, currentlyProcessed)
        currentlyProcessed.remove(this)
        if (isFunctionType || !isExportableArgs) {
            return isExportableArgs
        }

        val nonNullable = expandedType.withNullability(nullable = false, session.typeContext)
        val isPrimitiveExportableType = nonNullable.isAny || nonNullable.isNullableAny
                || nonNullable is ConeDynamicType || nonNullable.isPrimitiveExportableConeKotlinType

        val symbol = expandedType.toSymbol(session)

        return when {
            isPrimitiveExportableType -> true
            symbol?.isMemberDeclaration != true -> false
            expandedType.isEnum -> true
            else -> symbol.isEffectivelyExternal(session) || symbol.isExportedObject(session)
        }
    }

    private val ConeKotlinType.isPrimitiveExportableConeKotlinType: Boolean
        get() = this is ConeTypeParameterType
                || isBoolean
                || isThrowableOrNullableThrowable
                || isString
                || isPrimitiveNumberOrNullableType && !isLong
                || isNothingOrNullableNothing
                || isPrimitiveArray
                || isNonPrimitiveArray
                || isList
                || isMutableList
                || isSet
                || isMutableSet
                || isMap
                || isMutableMap

    context(context: CheckerContext, reporter: DiagnosticReporter)
    private fun validateDeclarationOnConsumableName(
        declaration: FirMemberDeclaration,
    ) {
        if (!context.isTopLevel || declaration.nameOrSpecialName.isSpecial) {
            return
        }

        val jsNameArgument = declaration.symbol.getAnnotationFirstArgument(JsStandardClassIds.Annotations.JsName, context.session)
        val reportTarget = jsNameArgument?.source ?: declaration.source
        val name = (jsNameArgument as? FirLiteralExpression)?.value as? String ?: declaration.nameOrSpecialName.asString()

        if (name in SPECIAL_KEYWORDS || (name !in RESERVED_KEYWORDS && sanitizeName(name) == name)) {
            return
        }

        reporter.reportOn(reportTarget, FirJsErrors.NON_CONSUMABLE_EXPORTED_IDENTIFIER, name)
    }
}
