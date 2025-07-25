/*
 * Copyright 2010-2022 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.gradle.utils

import com.android.build.api.variant.AndroidComponentsExtension
import com.android.build.api.variant.Component
import com.android.build.gradle.*
import org.gradle.api.Project

/**
 * Uses the legacy Variant API
 */
internal fun Project.forAllAndroidVariants(
    @Suppress("TYPEALIAS_EXPANSION_DEPRECATION") action: (DeprecatedAndroidBaseVariant) -> Unit,
) {
    val androidExtension = this.extensions.getByName("android")
    when (androidExtension) {
        is AppExtension -> androidExtension.applicationVariants.all(action)
        is LibraryExtension -> {
            androidExtension.libraryVariants.all(action)
        }

        is TestExtension -> androidExtension.applicationVariants.all(action)
    }
    if (androidExtension is TestedExtension) {
        androidExtension.testVariants.all(action)
        androidExtension.unitTestVariants.all(action)
    }
}

/**
 * Uses the new Variant API
 */
internal fun Project.configureAndroidVariants(action: (Component) -> Unit) {
    val androidComponentsExtension = this.extensions.getByType(AndroidComponentsExtension::class.java)
    androidComponentsExtension.onVariants { variant ->
        action(variant)
        variant.nestedComponents.forEach { action(it) }
    }
}
