/*
 * Copyright 2010-2020 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

@file:Suppress("LeakingThis", "PackageDirectoryMismatch") // All tasks should be inherited only by Gradle, Old package for compatibility

package org.jetbrains.kotlin.gradle.targets.native.tasks

import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.*
import org.gradle.work.DisableCachingByDefault
import org.jetbrains.kotlin.gradle.utils.CommandFallback
import org.jetbrains.kotlin.gradle.utils.RunProcessResult
import org.jetbrains.kotlin.gradle.utils.onlyIfCompat
import org.jetbrains.kotlin.gradle.utils.runCommandWithFallback
import java.io.File

/**
 * The task takes the path to the Podfile and calls `pod install`
 * to obtain sources or artifacts for the declared dependencies.
 * This task is a part of CocoaPods integration infrastructure.
 */
@DisableCachingByDefault(because = "Abstract super-class, not to be instantiated directly")
abstract class AbstractPodInstallTask : CocoapodsTask() {
    init {
        onlyIfCompat("Podfile location is set") { podfile.isPresent }
    }

    @get:Optional
    @get:PathSensitive(PathSensitivity.RELATIVE)
    @get:InputFile
    abstract val podfile: Property<File?>

    @get:Internal
    protected val workingDir: Provider<File> = podfile.map { file: File? ->
        requireNotNull(file) { "Task outputs shouldn't be queried if it's skipped" }.parentFile
    }

    @get:OutputDirectory
    internal val podsDir: Provider<File> = workingDir.map { it.resolve("Pods") }

    @get:Internal
    internal val podsXcodeProjDirProvider: Provider<File> = podsDir.map { it.resolve("Pods.xcodeproj") }

    @TaskAction
    open fun doPodInstall() {
        runPodInstall(false)

        with(podsXcodeProjDirProvider.get()) {
            check(exists() && isDirectory) {
                "The directory 'Pods/Pods.xcodeproj' was not created as a result of the `pod install` call."
            }
        }
    }

    private fun runPodInstall(updateRepo: Boolean): String {
        // env is used here to work around the JVM PATH caching when spawning a child process with custom environment, i.e. LC_ALL
        // The caching causes the ProcessBuilder to ignore changes in the PATH that may occur on incremental runs of the Gradle daemon
        // KT-60394
        val podInstallCommand = listOfNotNull("env", "pod", "install", if (updateRepo) "--repo-update" else null)

        return runCommandWithFallback(podInstallCommand,
                                      logger,
                                      fallback = { result ->
                                          val output = result.stdErr.ifBlank { result.stdOut }
                                          if (output.contains("out-of-date source repos which you can update with `pod repo update` or with `pod install --repo-update`") && updateRepo.not()) {
                                              CommandFallback.Action(runPodInstall(true))
                                          } else {
                                              CommandFallback.Error(sharedHandleError(podInstallCommand, result))
                                          }
                                      },
                                      processConfiguration = {
                                          directory(workingDir.get())
                                          // CocoaPods requires to be run with Unicode external encoding
                                          environment().putIfAbsent("LC_ALL", "en_US.UTF-8")
                                      })
    }

    private fun sharedHandleError(podInstallCommand: List<String>, result: RunProcessResult): String? {
        val command = podInstallCommand.joinToString(" ")
        val output = result.stdErr.ifBlank { result.stdOut }

        var message = """
            |'$command' command failed with an exception:
            | stdErr: ${result.stdErr}
            | stdOut: ${result.stdOut}
            | exitCode: ${result.retCode}
            |        
        """.trimMargin()

        if (output.contains("No such file or directory")) {
            message += """ 
               |        Full command: $command
               |        
               |        Possible reason: CocoaPods is not installed
               |        Please check that CocoaPods v1.14 or above is installed.
               |        
               |        To check CocoaPods version type 'pod --version' in the terminal
               |        
               |        To install CocoaPods execute 'sudo gem install cocoapods'
               |        For more information, refer to the documentation: https://kotl.in/fx2sde
               |
            """.trimMargin()
            return message
        } else if (output.contains("[Xcodeproj] Unknown object version")) {
            message += """
               |        Your CocoaPods installation may be outdated or corrupted
               |
               |        To update CocoaPods execute 'sudo gem install cocoapods'
               |        For more information, refer to the documentation: https://kotl.in/0xfxux
               |
            """.trimMargin()
            return message
        } else {
            return handleError(result)
        }
    }

    abstract fun handleError(result: RunProcessResult): String?
}