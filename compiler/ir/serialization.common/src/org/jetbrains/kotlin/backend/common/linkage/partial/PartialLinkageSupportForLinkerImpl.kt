/*
 * Copyright 2010-2022 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.backend.common.linkage.partial

import org.jetbrains.kotlin.backend.common.linkage.issues.PartialLinkageErrorsLogged
import org.jetbrains.kotlin.backend.common.overrides.IrLinkerFakeOverrideProvider
import org.jetbrains.kotlin.cli.common.messages.MessageCollector
import org.jetbrains.kotlin.ir.IrBuiltIns
import org.jetbrains.kotlin.ir.declarations.IrDeclaration
import org.jetbrains.kotlin.ir.declarations.IrFile
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.symbols.IrClassifierSymbol
import org.jetbrains.kotlin.ir.symbols.IrSymbol
import org.jetbrains.kotlin.ir.symbols.IrTypeAliasSymbol
import org.jetbrains.kotlin.ir.util.SymbolTable
import org.jetbrains.kotlin.platform.TargetPlatform

fun createPartialLinkageSupportForLinker(
    partialLinkageConfig: PartialLinkageConfig,
    builtIns: IrBuiltIns,
    messageCollector: MessageCollector,
    platform: TargetPlatform,
): PartialLinkageSupportForLinker = if (partialLinkageConfig.isEnabled)
    PartialLinkageSupportForLinkerImpl(
        builtIns = builtIns,
        platform = platform,
        logger = PartialLinkageLogger(messageCollector, partialLinkageConfig.logLevel),
    )
else
    PartialLinkageSupportForLinker.DISABLED

internal class PartialLinkageSupportForLinkerImpl(
    builtIns: IrBuiltIns,
    private val logger: PartialLinkageLogger,
    platform: TargetPlatform
) : PartialLinkageSupportForLinker {
    private val stubGenerator = MissingDeclarationStubGenerator(builtIns)
    private val classifierExplorer = ClassifierExplorer(builtIns, stubGenerator, platform)
    private val patcher = PartiallyLinkedIrTreePatcher(builtIns, classifierExplorer, stubGenerator, logger)

    /**
     * The queue of IR files to remove unusable annotations.
     *
     * Note: The fact that an IR file is in this queue does not automatically mean that
     * the declarations of this file are going to be processed/patched by the PL engine.
     * To process the declarations, they need to be explicitly added to the appropriate queue: [declarationsEnqueuedForProcessing].
     */
    private val filesEnqueuedForProcessing = hashSetOf<IrFile>()

    /** The queue of IR declarations to be processed/patched by the PL engine. */
    private val declarationsEnqueuedForProcessing = hashSetOf<IrDeclaration>()

    override val isEnabled get() = true

    override fun shouldBeSkipped(declaration: IrDeclaration) = patcher.shouldBeSkipped(declaration)

    override fun enqueueFile(file: IrFile) {
        filesEnqueuedForProcessing += file
    }

    override fun enqueueDeclaration(declaration: IrDeclaration) {
        declarationsEnqueuedForProcessing += declaration
    }

    override fun exploreClassifiers(fakeOverrideBuilder: IrLinkerFakeOverrideProvider) {
        val entries = fakeOverrideBuilder.fakeOverrideCandidates
        if (entries.isEmpty()) return

        val toExclude = buildSet {
            for (clazz in entries.keys) {
                if (classifierExplorer.exploreSymbol(clazz.symbol) != null) {
                    this += clazz
                }
            }
        }

        entries -= toExclude
    }

    override fun exploreClassifiersInInlineLazyIrFunction(function: IrFunction) {
        classifierExplorer.exploreIrElement(function)
    }

    override fun generateStubsAndPatchUsages(symbolTable: SymbolTable) {
        // Generate stubs.
        for (symbol in symbolTable.descriptorExtension.allUnboundSymbols) {
            stubGenerator.getDeclaration(symbol)
        }

        // Patch IR files (without visiting contained declarations).
        patcher.removeUnusableAnnotationsFromFiles(filesEnqueuedForProcessing.getCopyAndClear())

        // Patch all IR declarations scheduled so far.
        patcher.patchDeclarations(declarationsEnqueuedForProcessing.getCopyAndClear())

        // Make sure that there are no linkage issues that have been reported with the 'error' severity.
        // If there are, abort the current compilation.
        if (logger.logLevel == PartialLinkageLogLevel.ERROR && patcher.linkageIssuesLogged > 0)
            PartialLinkageErrorsLogged.raiseIssue(logger.messageCollector)
    }

    override fun generateStubsForClassifiers(symbolTable: SymbolTable) {
        symbolTable.descriptorExtension.allUnboundSymbols
            .filter { it is IrClassifierSymbol || it is IrTypeAliasSymbol }
            .forEach { stubGenerator.getDeclaration(it) }
    }

    override fun collectAllStubbedSymbols(): Set<IrSymbol> {
        return stubGenerator.allStubbedSymbols
    }
}
