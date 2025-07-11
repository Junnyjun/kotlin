/*
 * Copyright 2010-2025 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.benchmarks

import com.intellij.openapi.Disposable
import com.intellij.openapi.util.io.FileUtil
import com.intellij.openapi.vfs.StandardFileSystems
import com.intellij.openapi.vfs.VfsUtilCore
import com.intellij.openapi.vfs.VirtualFileManager
import com.intellij.psi.PsiManager
import org.jetbrains.kotlin.analyzer.AnalysisResult
import org.jetbrains.kotlin.backend.common.linkage.issues.checkNoUnboundSymbols
import org.jetbrains.kotlin.backend.common.linkage.partial.PartialLinkageSupportForLinker
import org.jetbrains.kotlin.backend.common.phaser.then
import org.jetbrains.kotlin.backend.common.serialization.IrSerializationSettings
import org.jetbrains.kotlin.backend.common.serialization.signature.IdSignatureDescriptor
import org.jetbrains.kotlin.build.report.DoNothingBuildReporter
import org.jetbrains.kotlin.cli.common.arguments.K2JSCompilerArguments
import org.jetbrains.kotlin.cli.common.messages.MessageCollector
import org.jetbrains.kotlin.cli.js.klib.TopDownAnalyzerFacadeForJSIR
import org.jetbrains.kotlin.cli.jvm.compiler.EnvironmentConfigFiles
import org.jetbrains.kotlin.cli.jvm.compiler.KotlinCoreEnvironment
import org.jetbrains.kotlin.config.*
import org.jetbrains.kotlin.config.phaser.CompilerPhase
import org.jetbrains.kotlin.config.phaser.invokeToplevel
import org.jetbrains.kotlin.descriptors.impl.ModuleDescriptorImpl
import org.jetbrains.kotlin.diagnostics.DiagnosticReporterFactory
import org.jetbrains.kotlin.incremental.ChangedFiles
import org.jetbrains.kotlin.incremental.IncrementalJsCompilerRunner
import org.jetbrains.kotlin.incremental.multiproject.EmptyModulesApiHistory
import org.jetbrains.kotlin.incremental.withJsIC
import org.jetbrains.kotlin.ir.IrBuiltIns
import org.jetbrains.kotlin.ir.KtDiagnosticReporterWithImplicitIrBasedContext
import org.jetbrains.kotlin.ir.ObsoleteDescriptorBasedAPI
import org.jetbrains.kotlin.ir.backend.js.*
import org.jetbrains.kotlin.ir.backend.js.lower.serialization.ir.JsIrLinker
import org.jetbrains.kotlin.ir.backend.js.lower.serialization.ir.JsIrModuleSerializer
import org.jetbrains.kotlin.ir.backend.js.lower.serialization.ir.JsManglerDesc
import org.jetbrains.kotlin.ir.backend.js.transformers.irToJs.IrModuleToJsTransformer
import org.jetbrains.kotlin.ir.backend.js.transformers.irToJs.TranslationMode
import org.jetbrains.kotlin.ir.declarations.IrModuleFragment
import org.jetbrains.kotlin.ir.declarations.impl.IrFactoryImpl
import org.jetbrains.kotlin.ir.util.ExternalDependenciesGenerator
import org.jetbrains.kotlin.ir.util.SymbolTable
import org.jetbrains.kotlin.ir.util.patchDeclarationParents
import org.jetbrains.kotlin.js.config.JSConfigurationKeys
import org.jetbrains.kotlin.library.*
import org.jetbrains.kotlin.library.impl.BuiltInsPlatform
import org.jetbrains.kotlin.library.impl.KotlinLibraryOnlyIrWriter
import org.jetbrains.kotlin.library.loader.KlibPlatformChecker
import org.jetbrains.kotlin.library.metadata.kotlinLibrary
import org.jetbrains.kotlin.metadata.deserialization.MetadataVersion
import org.jetbrains.kotlin.progress.ProgressIndicatorAndCompilationCanceledStatus
import org.jetbrains.kotlin.psi.KtFile
import org.jetbrains.kotlin.psi2ir.Psi2IrConfiguration
import org.jetbrains.kotlin.psi2ir.Psi2IrTranslator
import org.jetbrains.kotlin.psi2ir.descriptors.IrBuiltInsOverDescriptors
import org.jetbrains.kotlin.psi2ir.generators.TypeTranslatorImpl
import org.jetbrains.kotlin.resolve.BindingContext
import org.jetbrains.kotlin.resolve.CompilerEnvironment
import org.jetbrains.kotlin.resolve.multiplatform.isCommonSource
import org.jetbrains.kotlin.serialization.js.ModuleKind
import org.junit.After
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import org.junit.jupiter.api.Tag
import java.io.File
import kotlin.io.path.createTempDirectory
import kotlin.io.path.createTempFile

@Tag("legacy-frontend")
@Ignore
class GenerateIrRuntime {
    fun loadKlib(klibPath: String, isPacked: Boolean): KotlinLibrary {
        return loadWebKlibsInTestPipeline(
            configuration = environment.configuration,
            libraryPaths = listOf("$klibPath${if (isPacked) ".klib" else ""}"),
            platformChecker = KlibPlatformChecker.JS
        ).all.single()
    }

    private fun buildConfiguration(environment: KotlinCoreEnvironment): CompilerConfiguration {
        val runtimeConfiguration = environment.configuration.copy()
        runtimeConfiguration.put(CommonConfigurationKeys.MODULE_NAME, "JS_IR_RUNTIME")
        runtimeConfiguration.put(JSConfigurationKeys.MODULE_KIND, ModuleKind.UMD)

        runtimeConfiguration.languageVersionSettings = LanguageVersionSettingsImpl(
            LanguageVersion.LATEST_STABLE, ApiVersion.LATEST_STABLE,
            specificFeatures = mapOf(
                LanguageFeature.AllowContractsForCustomFunctions to LanguageFeature.State.ENABLED,
                LanguageFeature.MultiPlatformProjects to LanguageFeature.State.ENABLED
            ),
            analysisFlags = mapOf(
                AnalysisFlags.optIn to listOf(
                    "kotlin.contracts.ExperimentalContracts",
                    "kotlin.Experimental",
                    "kotlin.ExperimentalMultiplatform"
                ),
            )
        )

        return runtimeConfiguration
    }

    private val environment =
        KotlinCoreEnvironment.createForTests(Disposable { }, CompilerConfiguration(), EnvironmentConfigFiles.JS_CONFIG_FILES)
    private val configuration = buildConfiguration(environment)
    private val project = environment.project
    private val jsPhases = getJsLowerings(configuration).toCompilerPhase()

    private val languageVersionSettings = configuration.languageVersionSettings
    private val moduleName = configuration[CommonConfigurationKeys.MODULE_NAME]!!

    fun createPsiFile(fileName: String, isCommon: Boolean): KtFile? {
        val psiManager = PsiManager.getInstance(environment.project)
        val fileSystem = VirtualFileManager.getInstance().getFileSystem(StandardFileSystems.FILE_PROTOCOL)

        val file = fileSystem.findFileByPath(fileName) ?: error("File not found: $fileName")

        val psiFile = psiManager.findFile(file)

        return (psiFile as? KtFile)?.apply { isCommonSource = isCommon }
    }

    private fun File.listAllFiles(): List<File> {
        return if (isDirectory) listFiles().flatMap { it.listAllFiles() }
        else listOf(this)
    }

    private fun List<CompilerPhase<JsIrBackendContext, IrModuleFragment, IrModuleFragment>>.toCompilerPhase() =
        reduce { acc, lowering -> acc.then(lowering) }

    private fun createPsiFileFromDir(path: String, vararg extraDirs: String): List<KtFile> {
        val dir = File(path)
        val buildPath = File(dir, "build")
        val commonPath = File(buildPath, "commonMainSources")
        val extraPaths = extraDirs.map { File(dir, it) }
        val jsPaths = listOf(File(buildPath, "jsMainSources")) + extraPaths
        val commonPsis = commonPath.listAllFiles().mapNotNull { createPsiFile(it.path, true) }
        val jsPsis = jsPaths.flatMap { d -> d.listAllFiles().mapNotNull { createPsiFile(it.path, false) } }
        return commonPsis + jsPsis
    }

    private val fullRuntimeSourceSet = createPsiFileFromDir("libraries/stdlib/js-ir", "builtins", "runtime", "src")
    private val reducedRuntimeSourceSet = createPsiFileFromDir("libraries/stdlib/js-ir-minimal-for-test", "src")

    private lateinit var workingDir: File

    @Before
    fun setUp() {
        workingDir = FileUtil.createTempDirectory("irTest", null, false)
    }

    @After
    fun tearDown() {
        workingDir.deleteRecursively()
    }

    @Test
    fun runFullPipeline() {
        runBenchWithWarmup("Full pipeline", 5, 2, MeasureUnits.MICROSECONDS, pre = System::gc) {
            compile(fullRuntimeSourceSet)
        }
    }

    @Test
    fun runWithoutFrontEnd() {
        val files = fullRuntimeSourceSet
        val analysisResult = doFrontEnd(files)

        runBenchWithWarmup("Pipeline without FrontEnd", 40, 10, MeasureUnits.MICROSECONDS, pre = System::gc) {
            val (rawModuleFragment, irBuiltInsForSerialization) = doPsi2Ir(files, analysisResult)

            val modulePath = doSerializeModule(rawModuleFragment, irBuiltInsForSerialization, analysisResult.bindingContext, files)

            val (module, symbolTable, irBuiltIns, linker) = doDeserializeModule(modulePath)

            doBackEnd(module, symbolTable, irBuiltIns, linker)
        }
    }

    @Test
    fun runPsi2Ir() {
        val files = fullRuntimeSourceSet
        val analysisResult = doFrontEnd(files)

        runBenchWithWarmup("Psi2Ir phase", 40, 10, MeasureUnits.MICROSECONDS, pre = System::gc) {
            doPsi2Ir(files, analysisResult)
        }
    }

    @Test
    fun runSerialization() {
        val files = fullRuntimeSourceSet
        val analysisResult = doFrontEnd(files)
        val (rawModuleFragment, irBuiltIns) = doPsi2Ir(files, analysisResult)

        runBenchWithWarmup("Ir Serialization", 40, 10, MeasureUnits.MILLISECONDS, pre = System::gc) {
            doSerializeModule(rawModuleFragment, irBuiltIns, analysisResult.bindingContext, files)
        }
    }

    enum class MeasureUnits(val delimeter: Long, private val suffix: String) {
        NANOSECONDS(1L, "ns"),
        MICROSECONDS(1000L, "mcs"),
        MILLISECONDS(1000L * 1000L, "ms");

        fun convert(nanos: Long): String = "${(nanos / delimeter)}$suffix"
    }

    @Test
    fun runIrDeserializationMonolithic() {
        val files = fullRuntimeSourceSet
        val analysisResult = doFrontEnd(files)
        val (rawModuleFragment, irBuiltIns) = doPsi2Ir(files, analysisResult)
        val modulePath = doSerializeModule(rawModuleFragment, irBuiltIns, analysisResult.bindingContext, files)
        val moduleRef = loadKlib(modulePath, isPacked = false)
        val moduleDescriptor = doDeserializeModuleMetadata(moduleRef)

        runBenchWithWarmup("Ir Deserialization Monolithic", 40, 10, MeasureUnits.MILLISECONDS, pre = System::gc) {
            doDeserializeIrModule(moduleDescriptor)
        }
    }

    @Test
    fun runIrSerialization() {
        val files = fullRuntimeSourceSet
        val analysisResult = doFrontEnd(files)
        val (rawModuleFragment, irBuiltIns) = doPsi2Ir(files, analysisResult)

        runBenchWithWarmup("Ir Serialization", 40, 10, MeasureUnits.MILLISECONDS, pre = System::gc) {
            doSerializeIrModule(rawModuleFragment, irBuiltIns)
        }
    }

    @Test
    fun runMonolithicDiskWriting() {
        val compilerVersion = KotlinCompilerVersion.getVersion()
        val abiVersion = KotlinAbiVersion.CURRENT // does not matter
        val metadataVersion = MetadataVersion.INSTANCE // does not matter

        val versions = KotlinLibraryVersioning(compilerVersion, abiVersion, metadataVersion)
        val file = createTempFile(directory = workingDir.toPath()).toFile()
        val writer = KotlinLibraryOnlyIrWriter(file.absolutePath, "", versions, BuiltInsPlatform.JS, emptyList())
        val files = fullRuntimeSourceSet
        val analysisResult = doFrontEnd(files)
        val (rawModuleFragment, irBuiltIns) = doPsi2Ir(files, analysisResult)
        val fileCount = rawModuleFragment.files.size
        val serializedIr = doSerializeIrModule(rawModuleFragment, irBuiltIns)

        runBenchWithWarmup("Monolithic Disk Writing of $fileCount files", 10, 30, MeasureUnits.MILLISECONDS, pre = writer::invalidate) {
            doWriteIrModuleToStorage(serializedIr, writer)
        }
    }

    @Test
    fun runIncrementalKlibGeneration() {

        val klibDirectory = workingDir.resolve("output/klib")

        val filesToCompile = fullRuntimeSourceSet
//        val filesToCompile = reducedRuntimeSourceSet

        val args = K2JSCompilerArguments().apply {
            libraries = ""
            outputDir = klibDirectory.path
            sourceMap = false
            irProduceKlibDir = true
            irModuleName = "kotlin"
            allowKotlinPackage = true
            optIn = arrayOf("kotlin.contracts.ExperimentalContracts", "kotlin.Experimental", "kotlin.ExperimentalMultiplatform")
            multiPlatform = true
            languageVersion = "1.4"
            commonSources = filesToCompile.filter { it.isCommonSource == true }.map { it.virtualFilePath }.toTypedArray()
        }

        val cachesDir = workingDir.resolve("caches")
        val allFiles = filesToCompile.map { VfsUtilCore.virtualToIoFile(it.virtualFile) }
        val dirtyFiles = allFiles.filter { it.name.contains("coreRuntime") }

        val cleanBuildStart = System.nanoTime()

        withJsIC(args) {
            val buildHistoryFile = File(cachesDir, "build-history.bin")
            val compiler = IncrementalJsCompilerRunner(
                cachesDir, DoNothingBuildReporter,
                buildHistoryFile = buildHistoryFile,
                modulesApiHistory = EmptyModulesApiHistory
            )
            compiler.compile(allFiles, args, MessageCollector.NONE, changedFiles = ChangedFiles.DeterminableFiles.ToBeComputed)
        }

        val cleanBuildTime = System.nanoTime() - cleanBuildStart

        println("[Cold] Clean build of ${allFiles.size} takes ${MeasureUnits.MILLISECONDS.convert(cleanBuildTime)}")

        var index = -1
        val wmpDone = { index = 0 }

        val elist = emptyList<File>()
        var changedFiles = ChangedFiles.DeterminableFiles.Known(dirtyFiles, elist)

        val update = {
            changedFiles = if (index < 0) changedFiles else ChangedFiles.DeterminableFiles.Known(listOf(allFiles[index++]), elist)
            System.gc()
        }

        class CompileTimeResult(val file: String, val time: Long)

        var maxResult = CompileTimeResult("", -1)
        var minResult = CompileTimeResult("", Long.MAX_VALUE)

        val done = { t: Long ->
            if (maxResult.time < t) maxResult = CompileTimeResult(changedFiles.modified[0].path, t)
            if (minResult.time > t) minResult = CompileTimeResult(changedFiles.modified[0].path, t)
        }

        runBenchWithWarmup(
            "Incremental recompilation of ${dirtyFiles.count()} files",
            200,
            allFiles.size,
            MeasureUnits.MILLISECONDS,
            wmpDone,
            done,
            update
        ) {
            withJsIC(args) {
                val buildHistoryFile = File(cachesDir, "build-history.bin")
                val compiler = IncrementalJsCompilerRunner(
                    cachesDir, DoNothingBuildReporter,
                    buildHistoryFile = buildHistoryFile,
                    modulesApiHistory = EmptyModulesApiHistory
                )
                compiler.compile(allFiles, args, MessageCollector.NONE, changedFiles)
            }
        }

        println("Longest re-compilation takes ${MeasureUnits.MILLISECONDS.convert(maxResult.time)} (${maxResult.file})")
        println("Fastest re-compilation takes ${MeasureUnits.MILLISECONDS.convert(minResult.time)} (${minResult.file})")
    }

    private fun runBenchWithWarmup(
        name: String,
        W: Int,
        N: Int,
        measurer: MeasureUnits,
        wmpDone: () -> Unit = {},
        bnhDone: (Long) -> Unit = {},
        pre: () -> Unit = {},
        bench: () -> Unit
    ) {

        println("Run $name benchmark")

        println("Warmup: $W times...")

        repeat(W) {
            println("W: ${it + 1} out of $W")
            pre()
            bench()
        }

        var total = 0L

        wmpDone()

        println("Run bench: $N times...")

        repeat(N) {
            print("R: ${it + 1} out of $N ")
            pre()
            val start = System.nanoTime()
            bench()
            val iter = System.nanoTime() - start
            println("takes ${measurer.convert(iter)}")
            bnhDone(iter)
            total += iter
        }

        println("$name takes ${measurer.convert(total / N)}")
    }

    @Test
    fun runDeserialization() {
        val files = fullRuntimeSourceSet
        val analysisResult = doFrontEnd(files)
        val (rawModuleFragment, irBuiltIns) = doPsi2Ir(files, analysisResult)
        val modulePath = doSerializeModule(rawModuleFragment, irBuiltIns, analysisResult.bindingContext, files)

        repeat(20) {
            doDeserializeModule(modulePath)
        }
    }

    @Test
    fun runDeserializationAndBackend() {
        val files = fullRuntimeSourceSet
        val analysisResult = doFrontEnd(files)
        val (rawModuleFragment, irBuiltIns) = doPsi2Ir(files, analysisResult)
        val modulePath = doSerializeModule(rawModuleFragment, irBuiltIns, analysisResult.bindingContext, files)

        runBenchWithWarmup("Deserialization and Backend", 40, 10, MeasureUnits.MICROSECONDS, pre = System::gc) {
            val (module, symbolTable, irBuiltIns, linker) = doDeserializeModule(modulePath)
            doBackEnd(module, symbolTable, irBuiltIns, linker)
        }
    }

    private fun doFrontEnd(files: List<KtFile>): AnalysisResult {
        @Suppress("DEPRECATION")
        val analysisResult =
            TopDownAnalyzerFacadeForJSIR.analyzeFiles(
                files,
                project,
                configuration,
                emptyList(),
                friendModuleDescriptors = emptyList(),
                CompilerEnvironment,
                thisIsBuiltInsModule = true,
                customBuiltInsModule = null
            )

        ProgressIndicatorAndCompilationCanceledStatus.checkCanceled()

        @Suppress("DEPRECATION")
        TopDownAnalyzerFacadeForJSIR.checkForErrors(files, analysisResult.bindingContext)

        return analysisResult
    }

    private fun doPsi2Ir(files: List<KtFile>, analysisResult: AnalysisResult): Pair<IrModuleFragment, IrBuiltIns> {
        val messageCollector = MessageCollector.NONE
        val psi2Ir = Psi2IrTranslator(languageVersionSettings, Psi2IrConfiguration(), messageCollector::checkNoUnboundSymbols)
        val symbolTable = SymbolTable(IdSignatureDescriptor(JsManglerDesc), IrFactoryImpl)
        val psi2IrContext = psi2Ir.createGeneratorContext(analysisResult.moduleDescriptor, analysisResult.bindingContext, symbolTable)

        val irLinker = JsIrLinker(
            psi2IrContext.moduleDescriptor,
            messageCollector,
            psi2IrContext.irBuiltIns,
            psi2IrContext.symbolTable,
            PartialLinkageSupportForLinker.DISABLED,
        )

        val psi2IrTranslator =
            Psi2IrTranslator(languageVersionSettings, psi2IrContext.configuration, messageCollector::checkNoUnboundSymbols)
        return psi2IrTranslator.generateModuleFragment(psi2IrContext, files, listOf(irLinker)) to psi2IrContext.irBuiltIns
    }

    private fun doSerializeModule(
        moduleFragment: IrModuleFragment,
        irBuiltIns: IrBuiltIns,
        bindingContext: BindingContext,
        files: List<KtFile>,
    ): String {
        val messageCollector = configuration.getNotNull(CommonConfigurationKeys.MESSAGE_COLLECTOR_KEY)
        val diagnosticReporter = DiagnosticReporterFactory.createPendingReporter(messageCollector)
        val tmpKlibDir = createTempDirectory().also { it.toFile().deleteOnExit() }.toString()
        val metadataSerializer = KlibMetadataIncrementalSerializer(
            files,
            configuration,
            project,
            bindingContext,
            moduleFragment.descriptor,
        )
        serializeModuleIntoKlib(
            moduleName,
            configuration,
            diagnosticReporter,
            metadataSerializer,
            tmpKlibDir,
            emptyList(),
            moduleFragment,
            irBuiltIns,
            emptyList(),
            true,
            jsOutputName = null,
        )

        return tmpKlibDir
    }

    private fun doDeserializeModuleMetadata(moduleRef: KotlinLibrary): ModuleDescriptorImpl {
        return getModuleDescriptorByLibrary(moduleRef, emptyMap())
    }

    private data class DeserializedModuleInfo(
        val module: IrModuleFragment,
        val symbolTable: SymbolTable,
        val irBuiltIns: IrBuiltIns,
        val linker: JsIrLinker
    )


    private fun doSerializeIrModule(module: IrModuleFragment, irBuiltIns: IrBuiltIns): SerializedIrModule {
        val messageCollector = configuration.getNotNull(CommonConfigurationKeys.MESSAGE_COLLECTOR_KEY)

        return JsIrModuleSerializer(
            settings = IrSerializationSettings(configuration),
            KtDiagnosticReporterWithImplicitIrBasedContext(
                DiagnosticReporterFactory.createPendingReporter(messageCollector),
                configuration.languageVersionSettings,
            ),
            irBuiltIns,
        ).serializedIrModule(module)
    }

    private fun doWriteIrModuleToStorage(serializedIrModule: SerializedIrModule, writer: KotlinLibraryOnlyIrWriter) {
        writer.writeIr(serializedIrModule)
    }

    @OptIn(ObsoleteDescriptorBasedAPI::class)
    private fun doDeserializeIrModule(moduleDescriptor: ModuleDescriptorImpl): DeserializedModuleInfo {
        val mangler = JsManglerDesc
        val signaturer = IdSignatureDescriptor(mangler)
        val symbolTable = SymbolTable(signaturer, IrFactoryImpl)
        val typeTranslator = TypeTranslatorImpl(symbolTable, languageVersionSettings, moduleDescriptor)
        val irBuiltIns = IrBuiltInsOverDescriptors(moduleDescriptor.builtIns, typeTranslator, symbolTable)

        val jsLinker = JsIrLinker(moduleDescriptor, MessageCollector.NONE, irBuiltIns, symbolTable, PartialLinkageSupportForLinker.DISABLED)

        val moduleFragment = jsLinker.deserializeFullModule(moduleDescriptor, moduleDescriptor.kotlinLibrary)
        jsLinker.init(null)
        // Create stubs
        ExternalDependenciesGenerator(symbolTable, listOf(jsLinker))
            .generateUnboundSymbolsAsDependencies()

        jsLinker.postProcess(inOrAfterLinkageStep = true)
        jsLinker.clear()

        moduleFragment.patchDeclarationParents()

        return DeserializedModuleInfo(moduleFragment, symbolTable, irBuiltIns, jsLinker)
    }

    @OptIn(ObsoleteDescriptorBasedAPI::class)
    private fun doDeserializeModule(modulePath: String): DeserializedModuleInfo {
        val moduleRef = loadKlib(modulePath, false)
        val moduleDescriptor = doDeserializeModuleMetadata(moduleRef)
        val mangler = JsManglerDesc
        val signaturer = IdSignatureDescriptor(mangler)
        val symbolTable = SymbolTable(signaturer, IrFactoryImpl)
        val typeTranslator = TypeTranslatorImpl(symbolTable, languageVersionSettings, moduleDescriptor)
        val irBuiltIns = IrBuiltInsOverDescriptors(moduleDescriptor.builtIns, typeTranslator, symbolTable)

        val jsLinker = JsIrLinker(moduleDescriptor, MessageCollector.NONE, irBuiltIns, symbolTable, PartialLinkageSupportForLinker.DISABLED)

        val moduleFragment = jsLinker.deserializeFullModule(moduleDescriptor, moduleDescriptor.kotlinLibrary)
        jsLinker.init(null)
        // Create stubs
        ExternalDependenciesGenerator(symbolTable, listOf(jsLinker))
            .generateUnboundSymbolsAsDependencies()

        jsLinker.postProcess(inOrAfterLinkageStep = true)
        jsLinker.clear()

        moduleFragment.patchDeclarationParents()

        return DeserializedModuleInfo(moduleFragment, symbolTable, irBuiltIns, jsLinker)
    }


    private fun doBackEnd(
        module: IrModuleFragment, symbolTable: SymbolTable, irBuiltIns: IrBuiltIns, jsLinker: JsIrLinker
    ): CompilerResult {
        val context = JsIrBackendContext(
            module.descriptor,
            irBuiltIns,
            symbolTable,
            additionalExportedDeclarationNames = emptySet(),
            keep = emptySet(),
            configuration,
            null
        )

        ExternalDependenciesGenerator(symbolTable, listOf(jsLinker)).generateUnboundSymbolsAsDependencies()

        jsPhases.invokeToplevel(context.phaseConfig, context, module)

        val transformer = IrModuleToJsTransformer(context, shouldReferMainFunction = false)

        return transformer.generateModule(listOf(module), setOf(TranslationMode.PER_MODULE_DEV), false)
    }

    fun compile(files: List<KtFile>): String {
        val analysisResult = doFrontEnd(files)

        val (rawModuleFragment, irBuiltInsForSerialization) = doPsi2Ir(files, analysisResult)

        val modulePath = doSerializeModule(rawModuleFragment, irBuiltInsForSerialization, analysisResult.bindingContext, files)

        val (module, symbolTable, irBuiltIns, linker) = doDeserializeModule(modulePath)

        val jsProgram = doBackEnd(module, symbolTable, irBuiltIns, linker)

        return jsProgram.toString()
    }
}
