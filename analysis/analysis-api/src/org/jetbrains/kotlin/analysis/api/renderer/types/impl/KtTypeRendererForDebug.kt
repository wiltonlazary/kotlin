/*
 * Copyright 2010-2022 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.analysis.api.renderer.types.impl

import org.jetbrains.kotlin.analysis.api.renderer.types.KaTypeRenderer
import org.jetbrains.kotlin.analysis.api.renderer.types.renderers.KaCapturedTypeRenderer
import org.jetbrains.kotlin.analysis.api.renderer.types.renderers.KaFlexibleTypeRenderer
import org.jetbrains.kotlin.analysis.api.renderer.types.renderers.KaTypeErrorTypeRenderer
import org.jetbrains.kotlin.analysis.api.renderer.types.renderers.KaUnresolvedClassErrorTypeRenderer

public object KaTypeRendererForDebug {
    public val WITH_QUALIFIED_NAMES: KaTypeRenderer = KaTypeRendererForSource.WITH_QUALIFIED_NAMES.with {
        capturedTypeRenderer = KaCapturedTypeRenderer.AS_CAPTURED_TYPE_WITH_PROJECTION
        flexibleTypeRenderer = KaFlexibleTypeRenderer.AS_SHORT
        typeErrorTypeRenderer = KaTypeErrorTypeRenderer.WITH_ERROR_MESSAGE
        unresolvedClassErrorTypeRenderer = KaUnresolvedClassErrorTypeRenderer.WITH_ERROR_MESSAGE
    }
}

public typealias KtTypeRendererForDebug = KaTypeRendererForDebug