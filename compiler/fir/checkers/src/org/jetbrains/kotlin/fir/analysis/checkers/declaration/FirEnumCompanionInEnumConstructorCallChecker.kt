/*
 * Copyright 2010-2021 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.fir.analysis.checkers.declaration

import org.jetbrains.kotlin.descriptors.ClassKind
import org.jetbrains.kotlin.descriptors.isEnumEntry
import org.jetbrains.kotlin.diagnostics.DiagnosticReporter
import org.jetbrains.kotlin.diagnostics.reportOn
import org.jetbrains.kotlin.fir.FirSession
import org.jetbrains.kotlin.fir.analysis.checkers.MppCheckerKind
import org.jetbrains.kotlin.fir.analysis.checkers.context.CheckerContext
import org.jetbrains.kotlin.fir.analysis.diagnostics.FirErrors
import org.jetbrains.kotlin.fir.declarations.FirAnonymousObject
import org.jetbrains.kotlin.fir.declarations.FirClass
import org.jetbrains.kotlin.fir.declarations.FirRegularClass
import org.jetbrains.kotlin.fir.declarations.primaryConstructorIfAny
import org.jetbrains.kotlin.fir.expressions.FirExpression
import org.jetbrains.kotlin.fir.expressions.FirResolvedQualifier
import org.jetbrains.kotlin.fir.expressions.allReceiverExpressions
import org.jetbrains.kotlin.fir.expressions.toReference
import org.jetbrains.kotlin.fir.expressions.unwrapSmartcastExpression
import org.jetbrains.kotlin.fir.references.FirThisReference
import org.jetbrains.kotlin.fir.resolve.dfa.cfg.CFGNodeWithSubgraphs
import org.jetbrains.kotlin.fir.resolve.dfa.cfg.ControlFlowGraph
import org.jetbrains.kotlin.fir.resolve.dfa.cfg.FunctionCallExitNode
import org.jetbrains.kotlin.fir.resolve.dfa.cfg.QualifiedAccessNode
import org.jetbrains.kotlin.fir.resolve.dfa.controlFlowGraph
import org.jetbrains.kotlin.fir.symbols.impl.FirRegularClassSymbol
import org.jetbrains.kotlin.fir.types.resolvedType
import org.jetbrains.kotlin.fir.resolve.toRegularClassSymbol
import org.jetbrains.kotlin.utils.addToStdlib.lastIsInstanceOrNull

object FirEnumCompanionInEnumConstructorCallChecker : FirClassChecker(MppCheckerKind.Common) {
    context(context: CheckerContext, reporter: DiagnosticReporter)
    override fun check(declaration: FirClass) {
        val enumClass = when (declaration.classKind) {
            ClassKind.ENUM_CLASS -> (declaration as FirRegularClass).symbol
            ClassKind.ENUM_ENTRY -> context.containingDeclarations.lastIsInstanceOrNull()
            else -> null
        } ?: return
        val companionOfEnum = enumClass.resolvedCompanionObjectSymbol ?: return
        val graph = declaration.controlFlowGraphReference?.controlFlowGraph ?: return
        analyzeGraph(graph, companionOfEnum, enumClass)
        if (declaration.classKind.isEnumEntry) {
            val constructor = declaration.primaryConstructorIfAny(context.session)
            val constructorGraph = constructor?.resolvedControlFlowGraphReference?.controlFlowGraph
            if (constructorGraph != null) {
                analyzeGraph(constructorGraph, companionOfEnum, enumClass)
            }
        }
    }

    context(context: CheckerContext, reporter: DiagnosticReporter)
    private fun analyzeGraph(
        graph: ControlFlowGraph,
        companionSymbol: FirRegularClassSymbol,
        enumClass: FirRegularClassSymbol,
    ) {
        for (node in graph.nodes) {
            if (node is CFGNodeWithSubgraphs) {
                for (subGraph in node.subGraphs) {
                    when (subGraph.kind) {
                        ControlFlowGraph.Kind.AnonymousFunctionCalledInPlace,
                        ControlFlowGraph.Kind.PropertyInitializer,
                        ControlFlowGraph.Kind.ClassInitializer
                            -> analyzeGraph(subGraph, companionSymbol, enumClass)
                        ControlFlowGraph.Kind.Class -> {
                            if (subGraph.declaration is FirAnonymousObject) {
                                analyzeGraph(subGraph, companionSymbol, enumClass)
                            }
                        }
                        else -> {}
                    }
                }
            }
            val qualifiedAccess = when (node) {
                is QualifiedAccessNode -> node.fir
                is FunctionCallExitNode -> node.fir
                else -> continue
            }
            val matchingReceiver = qualifiedAccess.allReceiverExpressions
                .firstOrNull { it.unwrapSmartcastExpression().getClassSymbol(context.session) == companionSymbol }
            if (matchingReceiver != null) {
                reporter.reportOn(
                    matchingReceiver.source ?: qualifiedAccess.source,
                    FirErrors.UNINITIALIZED_ENUM_COMPANION,
                    enumClass
                )
            }
        }
    }

    private fun FirExpression.getClassSymbol(session: FirSession): FirRegularClassSymbol? {
        return when (this) {
            is FirResolvedQualifier -> {
                this.resolvedType.toRegularClassSymbol(session)
            }
            else -> (this.toReference(session) as? FirThisReference)?.boundSymbol
        } as? FirRegularClassSymbol
    }
}
