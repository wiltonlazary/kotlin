/*
 * Copyright 2010-2022 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.analysis.api.renderer.types.renderers

import org.jetbrains.kotlin.analysis.api.KaSession
import org.jetbrains.kotlin.analysis.api.renderer.types.KaTypeRenderer
import org.jetbrains.kotlin.analysis.api.types.KaClassType
import org.jetbrains.kotlin.analysis.api.types.KaClassTypeQualifier
import org.jetbrains.kotlin.analysis.api.types.KaNonErrorClassType
import org.jetbrains.kotlin.analysis.api.types.KaType
import org.jetbrains.kotlin.analysis.utils.printer.PrettyPrinter
import org.jetbrains.kotlin.name.CallableId
import org.jetbrains.kotlin.renderer.render

public interface KaClassTypeQualifierRenderer {
    public fun renderClassTypeQualifier(
        analysisSession: KaSession,
        type: KaClassType,
        typeRenderer: KaTypeRenderer,
        printer: PrettyPrinter,
    )

    public object WITH_SHORT_NAMES : KaClassTypeQualifierRenderer {
        override fun renderClassTypeQualifier(
            analysisSession: KaSession,
            type: KaClassType,
            typeRenderer: KaTypeRenderer,
            printer: PrettyPrinter,
        ) {
            type.qualifiers.last().render(analysisSession, type, typeRenderer, printer)
        }
    }

    public object WITH_SHORT_NAMES_WITH_NESTED_CLASSIFIERS : KaClassTypeQualifierRenderer {
        override fun renderClassTypeQualifier(
            analysisSession: KaSession,
            type: KaClassType,
            typeRenderer: KaTypeRenderer,
            printer: PrettyPrinter,
        ) {
            printer {
                printCollection(type.qualifiers, separator = ".") { qualifier ->
                    qualifier.render(analysisSession, type, typeRenderer, printer)
                }
            }
        }
    }

    public object WITH_QUALIFIED_NAMES : KaClassTypeQualifierRenderer {
        override fun renderClassTypeQualifier(
            analysisSession: KaSession,
            type: KaClassType,
            typeRenderer: KaTypeRenderer,
            printer: PrettyPrinter,
        ) {
            printer {
                ".".separated(
                    {
                        if (type is KaNonErrorClassType && type.classId.packageFqName != CallableId.PACKAGE_FQ_NAME_FOR_LOCAL) {
                            append(type.classId.packageFqName.render())
                        }
                    },
                    { WITH_SHORT_NAMES_WITH_NESTED_CLASSIFIERS.renderClassTypeQualifier(analysisSession, type, typeRenderer, printer) },
                )
            }
        }
    }
}

public typealias KtClassTypeQualifierRenderer = KaClassTypeQualifierRenderer

private fun KaClassTypeQualifier.render(
    analysisSession: KaSession,
    type: KaType,
    typeRenderer: KaTypeRenderer,
    printer: PrettyPrinter,
) {
    printer {
        typeRenderer.typeNameRenderer.renderName(analysisSession, name, type, typeRenderer, printer)
        printCollectionIfNotEmpty(typeArguments, prefix = "<", postfix = ">") {
            typeRenderer.typeProjectionRenderer.renderTypeProjection(analysisSession, it, typeRenderer, this)
        }
    }
}
