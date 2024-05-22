/*
 * Copyright 2010-2023 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.analysis.api.fir.symbols

import com.intellij.psi.PsiElement
import org.jetbrains.kotlin.analysis.api.KaInitializerValue
import org.jetbrains.kotlin.analysis.api.fir.KaFirSession
import org.jetbrains.kotlin.analysis.api.fir.annotations.KaFirAnnotationListForDeclaration
import org.jetbrains.kotlin.analysis.api.fir.findPsi
import org.jetbrains.kotlin.analysis.api.fir.symbols.pointers.KaFirJavaSyntheticPropertySymbolPointer
import org.jetbrains.kotlin.analysis.api.fir.symbols.pointers.createOwnerPointer
import org.jetbrains.kotlin.analysis.api.fir.utils.cached
import org.jetbrains.kotlin.analysis.api.lifetime.withValidityAssertion
import org.jetbrains.kotlin.analysis.api.symbols.*
import org.jetbrains.kotlin.analysis.api.symbols.pointers.KaSymbolPointer
import org.jetbrains.kotlin.analysis.api.types.KaType
import org.jetbrains.kotlin.descriptors.Modality
import org.jetbrains.kotlin.descriptors.Visibility
import org.jetbrains.kotlin.fir.declarations.synthetic.FirSyntheticProperty
import org.jetbrains.kotlin.fir.declarations.utils.isOverride
import org.jetbrains.kotlin.fir.declarations.utils.isStatic
import org.jetbrains.kotlin.fir.declarations.utils.modality
import org.jetbrains.kotlin.fir.declarations.utils.visibility
import org.jetbrains.kotlin.fir.symbols.SyntheticSymbol
import org.jetbrains.kotlin.fir.symbols.impl.FirSyntheticPropertySymbol
import org.jetbrains.kotlin.fir.symbols.impl.isExtension
import org.jetbrains.kotlin.name.CallableId
import org.jetbrains.kotlin.name.Name

internal class KaFirSyntheticJavaPropertySymbol(
    override val firSymbol: FirSyntheticPropertySymbol,
    override val analysisSession: KaFirSession,
) : KaSyntheticJavaPropertySymbol(), KaFirSymbol<FirSyntheticPropertySymbol> {
    override val psi: PsiElement? by cached { firSymbol.findPsi() }

    override val isVal: Boolean get() = withValidityAssertion { firSymbol.isVal }
    override val name: Name get() = withValidityAssertion { firSymbol.name }

    override val returnType: KaType get() = withValidityAssertion { firSymbol.returnType(builder) }
    override val receiverParameter: KaReceiverParameterSymbol? get() = withValidityAssertion { firSymbol.receiver(builder) }

    override val typeParameters: List<KaTypeParameterSymbol>
        get() = withValidityAssertion { firSymbol.createKtTypeParameters(builder) }


    override val isExtension: Boolean get() = withValidityAssertion { firSymbol.isExtension }

    override val initializer: KaInitializerValue? by cached { firSymbol.getKtConstantInitializer(builder) }

    override val modality: Modality get() = withValidityAssertion { firSymbol.modality }
    override val visibility: Visibility get() = withValidityAssertion { firSymbol.visibility }

    override val annotationsList by cached {
        KaFirAnnotationListForDeclaration.create(firSymbol, builder)
    }

    override val callableIdIfNonLocal: CallableId? get() = withValidityAssertion { firSymbol.getCallableIdIfNonLocal() }

    override val getter: KaPropertyGetterSymbol
        get() = withValidityAssertion {
            builder.callableBuilder.buildGetterSymbol(firSymbol.getterSymbol!!)
        }
    override val javaGetterSymbol: KaFunctionSymbol
        get() = withValidityAssertion {
            val fir = firSymbol.fir as FirSyntheticProperty
            return builder.functionLikeBuilder.buildFunctionSymbol(fir.getter.delegate.symbol)
        }
    override val javaSetterSymbol: KaFunctionSymbol?
        get() = withValidityAssertion {
            val fir = firSymbol.fir as FirSyntheticProperty
            return fir.setter?.delegate?.let { builder.functionLikeBuilder.buildFunctionSymbol(it.symbol) }
        }

    override val setter: KaPropertySetterSymbol?
        get() = withValidityAssertion {
            firSymbol.setterSymbol?.let { builder.callableBuilder.buildPropertyAccessorSymbol(it) } as? KaPropertySetterSymbol
        }

    override val backingFieldSymbol: KaBackingFieldSymbol?
        get() = null

    override val isFromPrimaryConstructor: Boolean get() = withValidityAssertion { false }
    override val isOverride: Boolean get() = withValidityAssertion { firSymbol.isOverride }
    override val isStatic: Boolean get() = withValidityAssertion { firSymbol.isStatic }

    override val hasSetter: Boolean get() = withValidityAssertion { firSymbol.setterSymbol != null }

    override val origin: KaSymbolOrigin get() = withValidityAssertion { KaSymbolOrigin.JAVA_SYNTHETIC_PROPERTY }

    override fun createPointer(): KaSymbolPointer<KaSyntheticJavaPropertySymbol> = withValidityAssertion {
        KaFirJavaSyntheticPropertySymbolPointer(
            ownerPointer = analysisSession.createOwnerPointer(this),
            propertyName = name,
            isSynthetic = firSymbol is SyntheticSymbol,
        )
    }

    override fun equals(other: Any?): Boolean = symbolEquals(other)
    override fun hashCode(): Int = symbolHashCode()
}
