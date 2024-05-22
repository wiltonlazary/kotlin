/*
 * Copyright 2010-2022 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.analysis.api.fir.components

import org.jetbrains.kotlin.analysis.api.fir.KaFirSession
import org.jetbrains.kotlin.analysis.api.fir.signatures.KaFirFunctionLikeDummySignature
import org.jetbrains.kotlin.analysis.api.fir.signatures.KaFirVariableLikeDummySignature
import org.jetbrains.kotlin.analysis.api.fir.symbols.KaFirSymbol
import org.jetbrains.kotlin.analysis.api.impl.base.components.KaAbstractSignatureSubstitutor
import org.jetbrains.kotlin.analysis.api.signatures.KaFunctionLikeSignature
import org.jetbrains.kotlin.analysis.api.signatures.KaVariableLikeSignature
import org.jetbrains.kotlin.analysis.api.symbols.KaFunctionLikeSymbol
import org.jetbrains.kotlin.analysis.api.symbols.KaVariableLikeSymbol
import org.jetbrains.kotlin.fir.symbols.impl.FirFunctionSymbol
import org.jetbrains.kotlin.fir.symbols.impl.FirVariableSymbol

internal class KaFirSignatureSubstitutor(
    override val analysisSession: KaFirSession
) : KaAbstractSignatureSubstitutor(), KaFirSessionComponent {
    override fun <S : KaFunctionLikeSymbol> asSignature(symbol: S): KaFunctionLikeSignature<S> {
        val firSymbol = (symbol as KaFirSymbol<*>).firSymbol as FirFunctionSymbol<*>
        return KaFirFunctionLikeDummySignature<S>(analysisSession.token, firSymbol, analysisSession.firSymbolBuilder)
    }

    override fun <S : KaVariableLikeSymbol> asSignature(symbol: S): KaVariableLikeSignature<S> {
        val firSymbol = (symbol as KaFirSymbol<*>).firSymbol as FirVariableSymbol<*>
        return KaFirVariableLikeDummySignature<S>(analysisSession.token, firSymbol, analysisSession.firSymbolBuilder)
    }
}