/*
 * Copyright 2010-2022 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.analysis.api.components

import org.jetbrains.kotlin.analysis.api.symbols.KaSymbol
import org.jetbrains.kotlin.idea.references.KtReference

public abstract class KaReferenceResolveProvider {
    public abstract fun resolveToSymbols(reference: KtReference): Collection<KaSymbol>

    public abstract fun isImplicitReferenceToCompanion(reference: KtReference): Boolean
}

public typealias KtReferenceResolveProvider = KaReferenceResolveProvider