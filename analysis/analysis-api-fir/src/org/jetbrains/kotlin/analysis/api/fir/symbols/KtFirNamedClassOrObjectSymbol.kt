/*
 * Copyright 2010-2023 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.analysis.api.fir.symbols

import com.intellij.psi.PsiElement
import org.jetbrains.kotlin.analysis.api.KaAnalysisApiInternals
import org.jetbrains.kotlin.analysis.api.base.KaContextReceiver
import org.jetbrains.kotlin.analysis.api.fir.KaFirSession
import org.jetbrains.kotlin.analysis.api.fir.annotations.KaFirAnnotationListForDeclaration
import org.jetbrains.kotlin.analysis.api.fir.findPsi
import org.jetbrains.kotlin.analysis.api.fir.utils.cached
import org.jetbrains.kotlin.analysis.api.impl.base.symbols.toKtClassKind
import org.jetbrains.kotlin.analysis.api.lifetime.KaLifetimeToken
import org.jetbrains.kotlin.analysis.api.lifetime.withValidityAssertion
import org.jetbrains.kotlin.analysis.api.symbols.KaClassKind
import org.jetbrains.kotlin.analysis.api.symbols.markers.KaSymbolKind
import org.jetbrains.kotlin.descriptors.Modality
import org.jetbrains.kotlin.descriptors.Visibilities
import org.jetbrains.kotlin.descriptors.Visibility
import org.jetbrains.kotlin.fir.declarations.FirDeclarationStatus
import org.jetbrains.kotlin.fir.declarations.utils.*
import org.jetbrains.kotlin.fir.extensions.extensionService
import org.jetbrains.kotlin.fir.extensions.statusTransformerExtensions
import org.jetbrains.kotlin.fir.symbols.impl.FirRegularClassSymbol
import org.jetbrains.kotlin.name.ClassId
import org.jetbrains.kotlin.name.Name

internal class KaFirNamedClassOrObjectSymbol(
    override val firSymbol: FirRegularClassSymbol,
    override val analysisSession: KaFirSession,
) : KaFirNamedClassOrObjectSymbolBase() {
    override val token: KaLifetimeToken get() = builder.token
    override val psi: PsiElement? by cached { firSymbol.findPsi() }

    override val name: Name get() = withValidityAssertion { firSymbol.name }

    override val classIdIfNonLocal: ClassId?
        get() = withValidityAssertion { firSymbol.getClassIdIfNonLocal() }

    override val modality: Modality
        get() = withValidityAssertion {
            firSymbol.optionallyResolvedStatus.modality
                ?: when (classKind) { // default modality
                    KaClassKind.INTERFACE -> Modality.ABSTRACT
                    else -> Modality.FINAL
                }
        }

    override val visibility: Visibility
        get() = withValidityAssertion {
            // TODO: We should use resolvedStatus, because it can be altered by status-transforming compiler plugins. See KT-58572
            when (val possiblyRawVisibility = firSymbol.fir.visibility) {
                Visibilities.Unknown -> if (firSymbol.fir.isLocal) Visibilities.Local else Visibilities.Public
                else -> possiblyRawVisibility
            }
        }

    override val annotationsList by cached {
        KaFirAnnotationListForDeclaration.create(firSymbol, builder)
    }

    override val isInner: Boolean get() = withValidityAssertion { firSymbol.isInner }
    override val isData: Boolean get() = withValidityAssertion { firSymbol.isData }
    override val isInline: Boolean get() = withValidityAssertion { firSymbol.isInline }
    override val isFun: Boolean get() = withValidityAssertion { firSymbol.isFun }
    override val isExternal: Boolean get() = withValidityAssertion { firSymbol.isExternal }
    override val isActual: Boolean get() = withValidityAssertion { firSymbol.isActual }
    override val isExpect: Boolean get() = withValidityAssertion { firSymbol.isExpect }

    override val contextReceivers: List<KaContextReceiver> get() = withValidityAssertion { firSymbol.createContextReceivers(builder) }

    override val companionObject: KaFirNamedClassOrObjectSymbol? by cached {
        firSymbol.companionObjectSymbol?.let {
            builder.classifierBuilder.buildNamedClassOrObjectSymbol(it)
        }
    }

    override val typeParameters = withValidityAssertion {
        firSymbol.createRegularKtTypeParameters(builder)
    }

    @OptIn(KaAnalysisApiInternals::class)
    override val classKind: KaClassKind
        get() = withValidityAssertion {
            firSymbol.classKind.toKtClassKind(isCompanionObject = firSymbol.isCompanion)
        }

    override val symbolKind: KaSymbolKind get() = withValidityAssertion { getSymbolKind() }

    /**
     * We can use [FirRegularClassSymbol.rawStatus] to avoid unnecessary resolve unless there are status transformers present.
     * If they are present, we have to resort to [FirRegularClassSymbol.resolvedStatus] instead - otherwise we can observe incorrect status
     * properties.
     *
     * TODO This optimization should become obsolete after KT-56551 is fixed.
     */
    private val FirRegularClassSymbol.optionallyResolvedStatus: FirDeclarationStatus
        get() = if (statusTransformersPresent) {
            resolvedStatus
        } else {
            rawStatus
        }

    private val statusTransformersPresent: Boolean
        get() = analysisSession.useSiteSession.extensionService.statusTransformerExtensions.isNotEmpty()
}
