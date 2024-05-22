/*
 * Copyright 2010-2023 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.analysis.api.fir.types

import org.jetbrains.kotlin.analysis.api.annotations.KaAnnotationsList
import org.jetbrains.kotlin.analysis.api.fir.KaSymbolByFirBuilder
import org.jetbrains.kotlin.analysis.api.fir.annotations.KaFirAnnotationListForType
import org.jetbrains.kotlin.analysis.api.fir.utils.cached
import org.jetbrains.kotlin.analysis.api.lifetime.KaLifetimeToken
import org.jetbrains.kotlin.analysis.api.lifetime.withValidityAssertion
import org.jetbrains.kotlin.analysis.api.types.KaTypeErrorType
import org.jetbrains.kotlin.analysis.api.types.KaTypeNullability
import org.jetbrains.kotlin.analysis.api.types.KaUsualClassType
import org.jetbrains.kotlin.fir.diagnostics.ConeCannotInferTypeParameterType
import org.jetbrains.kotlin.fir.diagnostics.ConeTypeVariableTypeIsNotInferred
import org.jetbrains.kotlin.fir.types.ConeErrorType
import org.jetbrains.kotlin.fir.types.renderForDebugging

internal class KaFirTypeErrorType(
    override val coneType: ConeErrorType,
    private val builder: KaSymbolByFirBuilder,
) : KaTypeErrorType(), KaFirType {
    override val token: KaLifetimeToken get() = builder.token

    override val nullability: KaTypeNullability get() = withValidityAssertion { coneType.nullability.asKtNullability() }
    override val errorMessage: String get() = withValidityAssertion { coneType.diagnostic.reason }

    override fun tryRenderAsNonErrorType(): String? = withValidityAssertion {
        when (val diagnostic = coneType.diagnostic) {
            is ConeCannotInferTypeParameterType -> diagnostic.typeParameter.name.asString()
            is ConeTypeVariableTypeIsNotInferred -> diagnostic.typeVariableType.typeConstructor.debugName
            else -> null
        }
    }

    override val annotationsList: KaAnnotationsList by cached {
        KaFirAnnotationListForType.create(coneType, builder)
    }

    override val abbreviatedType: KaUsualClassType?
        get() = withValidityAssertion { null }

    override fun asStringForDebugging(): String = withValidityAssertion { coneType.renderForDebugging() }
    override fun equals(other: Any?) = typeEquals(other)
    override fun hashCode() = typeHashcode()
}
