/*
 * Copyright 2010-2022 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.analysis.api.renderer.declarations.bodies

import org.jetbrains.kotlin.analysis.api.KaSession
import org.jetbrains.kotlin.analysis.api.symbols.KaFunctionLikeSymbol
import org.jetbrains.kotlin.analysis.utils.printer.PrettyPrinter

public interface KaFunctionLikeBodyRenderer {
    public fun renderBody(analysisSession: KaSession, symbol: KaFunctionLikeSymbol, printer: PrettyPrinter)

    public object NO_BODY : KaFunctionLikeBodyRenderer {
        override fun renderBody(analysisSession: KaSession, symbol: KaFunctionLikeSymbol, printer: PrettyPrinter) {}
    }
}

public typealias KtFunctionLikeBodyRenderer = KaFunctionLikeBodyRenderer