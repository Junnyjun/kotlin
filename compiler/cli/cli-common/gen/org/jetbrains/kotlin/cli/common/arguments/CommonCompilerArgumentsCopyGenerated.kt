@file:Suppress("unused", "DuplicatedCode")

// DO NOT EDIT MANUALLY!
// Generated by generators/tests/org/jetbrains/kotlin/generators/arguments/GenerateCompilerArgumentsCopy.kt
// To regenerate run 'generateCompilerArgumentsCopy' task

package org.jetbrains.kotlin.cli.common.arguments

@OptIn(org.jetbrains.kotlin.utils.IDEAPluginsCompatibilityAPI::class)
fun copyCommonCompilerArguments(from: CommonCompilerArguments, to: CommonCompilerArguments): CommonCompilerArguments {
    copyCommonToolArguments(from, to)

    to.allowAnyScriptsInSourceRoots = from.allowAnyScriptsInSourceRoots
    to.allowConditionImpliesReturnsContracts = from.allowConditionImpliesReturnsContracts
    to.allowContractsOnMoreFunctions = from.allowContractsOnMoreFunctions
    to.allowHoldsinContract = from.allowHoldsinContract
    to.allowKotlinPackage = from.allowKotlinPackage
    to.allowReifiedTypeInCatch = from.allowReifiedTypeInCatch
    to.annotationDefaultTarget = from.annotationDefaultTarget
    to.annotationTargetAll = from.annotationTargetAll
    to.apiVersion = from.apiVersion
    to.autoAdvanceApiVersion = from.autoAdvanceApiVersion
    to.autoAdvanceLanguageVersion = from.autoAdvanceLanguageVersion
    to.checkPhaseConditions = from.checkPhaseConditions
    to.commonSources = from.commonSources?.copyOf()
    to.consistentDataClassCopyVisibility = from.consistentDataClassCopyVisibility
    to.contextParameters = from.contextParameters
    to.contextReceivers = from.contextReceivers
    to.contextSensitiveResolution = from.contextSensitiveResolution
    to.debugLevelCompilerChecks = from.debugLevelCompilerChecks
    to.directJavaActualization = from.directJavaActualization
    to.disableDefaultScriptingPlugin = from.disableDefaultScriptingPlugin
    to.disablePhases = from.disablePhases?.copyOf()
    to.dontWarnOnErrorSuppression = from.dontWarnOnErrorSuppression
    to.dumpDirectory = from.dumpDirectory
    to.dumpOnlyFqName = from.dumpOnlyFqName
    to.dumpPerf = from.dumpPerf
    to.expectActualClasses = from.expectActualClasses
    to.explicitApi = from.explicitApi
    to.explicitReturnTypes = from.explicitReturnTypes
    to.fragmentDependencies = from.fragmentDependencies?.copyOf()
    to.fragmentRefines = from.fragmentRefines?.copyOf()
    to.fragmentSources = from.fragmentSources?.copyOf()
    to.fragments = from.fragments?.copyOf()
    to.ignoreConstOptimizationErrors = from.ignoreConstOptimizationErrors
    to.incrementalCompilation = from.incrementalCompilation
    to.inlineClasses = from.inlineClasses
    to.intellijPluginRoot = from.intellijPluginRoot
    to.kotlinHome = from.kotlinHome
    to.languageVersion = from.languageVersion
    to.lenientMode = from.lenientMode
    to.listPhases = from.listPhases
    to.metadataKlib = from.metadataKlib
    to.metadataVersion = from.metadataVersion
    to.multiDollarInterpolation = from.multiDollarInterpolation
    to.multiPlatform = from.multiPlatform
    to.nameBasedDestructuring = from.nameBasedDestructuring
    to.nestedTypeAliases = from.nestedTypeAliases
    to.newInference = from.newInference
    to.noCheckActual = from.noCheckActual
    to.noInline = from.noInline
    to.nonLocalBreakContinue = from.nonLocalBreakContinue
    to.optIn = from.optIn?.copyOf()
    to.phasesToDump = from.phasesToDump?.copyOf()
    to.phasesToDumpAfter = from.phasesToDumpAfter?.copyOf()
    to.phasesToDumpBefore = from.phasesToDumpBefore?.copyOf()
    to.phasesToValidate = from.phasesToValidate?.copyOf()
    to.phasesToValidateAfter = from.phasesToValidateAfter?.copyOf()
    to.phasesToValidateBefore = from.phasesToValidateBefore?.copyOf()
    to.pluginClasspaths = from.pluginClasspaths?.copyOf()
    to.pluginConfigurations = from.pluginConfigurations?.copyOf()
    to.pluginOptions = from.pluginOptions?.copyOf()
    to.profilePhases = from.profilePhases
    to.progressiveMode = from.progressiveMode
    to.renderInternalDiagnosticNames = from.renderInternalDiagnosticNames
    to.repl = from.repl
    to.reportAllWarnings = from.reportAllWarnings
    to.reportOutputFiles = from.reportOutputFiles
    to.reportPerf = from.reportPerf
    to.returnValueChecker = from.returnValueChecker
    to.script = from.script
    to.separateKmpCompilationScheme = from.separateKmpCompilationScheme
    to.skipMetadataVersionCheck = from.skipMetadataVersionCheck
    to.skipPrereleaseCheck = from.skipPrereleaseCheck
    to.stdlibCompilation = from.stdlibCompilation
    to.suppressApiVersionGreaterThanLanguageVersionError = from.suppressApiVersionGreaterThanLanguageVersionError
    to.suppressVersionWarnings = from.suppressVersionWarnings
    to.suppressedDiagnostics = from.suppressedDiagnostics?.copyOf()
    to.unrestrictedBuilderInference = from.unrestrictedBuilderInference
    @Suppress("DEPRECATION")
    to.useFirExperimentalCheckers = from.useFirExperimentalCheckers
    to.useFirIC = from.useFirIC
    to.useFirLT = from.useFirLT
    to.useK2 = from.useK2
    to.verbosePhases = from.verbosePhases?.copyOf()
    to.verifyIr = from.verifyIr
    to.verifyIrVisibility = from.verifyIrVisibility
    to.warningLevels = from.warningLevels?.copyOf()
    to.whenGuards = from.whenGuards
    to.xdataFlowBasedExhaustiveness = from.xdataFlowBasedExhaustiveness

    return to
}
