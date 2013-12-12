/*
 * Copyright 2010-2013 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jetbrains.k2js.translate.declaration;

import com.google.dart.compiler.backend.js.ast.*;
import com.intellij.util.SmartList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.jet.lang.descriptors.NamespaceDescriptor;
import org.jetbrains.jet.lang.psi.JetDeclaration;
import org.jetbrains.jet.lang.psi.JetFile;
import org.jetbrains.k2js.translate.context.DefinitionPlace;
import org.jetbrains.k2js.translate.context.TranslationContext;
import org.jetbrains.k2js.translate.general.AbstractTranslator;
import org.jetbrains.k2js.translate.utils.AnnotationsUtils;
import org.jetbrains.k2js.translate.utils.BindingUtils;

import java.util.List;
import java.util.Map;

final class NamespaceTranslator extends AbstractTranslator {

    static NamespaceTranslator create(
            @NotNull NamespaceDescriptor descriptor,
            @NotNull TranslationContext context
    ) {
        SmartList<JsPropertyInitializer> initializers = new SmartList<JsPropertyInitializer>();
        DefinitionPlace definitionPlace = new DefinitionPlace(initializers, context.getQualifiedReference(descriptor));

        TranslationContext newContext = context.newDeclaration(descriptor, definitionPlace);
        FileDeclarationVisitor visitor = new FileDeclarationVisitor(newContext, initializers);
        return new NamespaceTranslator(descriptor, newContext, visitor);
    }

    @NotNull
    private final NamespaceDescriptor descriptor;

    private final FileDeclarationVisitor visitor;

    private NamespaceTranslator(
            @NotNull NamespaceDescriptor descriptor,
            @NotNull TranslationContext context,
            @NotNull FileDeclarationVisitor visitor
    ) {
        super(context);
        this.descriptor = descriptor;
        this.visitor = visitor;
    }

    public void translate(JetFile file) {
        for (JetDeclaration declaration : file.getDeclarations()) {
            if (!AnnotationsUtils.isPredefinedObject(BindingUtils.getDescriptorForElement(bindingContext(), declaration))) {
                declaration.accept(visitor, context());
            }
        }
    }

    private void createDefinitionPlace(
            @Nullable JsExpression initializer,
            Map<NamespaceDescriptor, DefineInvocation> descriptorToDefineInvocation
    ) {
        DefineInvocation place = DefineInvocation.create(descriptor, initializer, new JsObjectLiteral(visitor.getResult(), true), context());
        descriptorToDefineInvocation.put(descriptor, place);
        addToParent((NamespaceDescriptor) descriptor.getContainingDeclaration(), getEntry(descriptor, place), descriptorToDefineInvocation);
    }

    public void add(@NotNull Map<NamespaceDescriptor, DefineInvocation> descriptorToDefineInvocation) {
        JsExpression initializer = visitor.computeInitializer();

        DefineInvocation defineInvocation = descriptorToDefineInvocation.get(descriptor);
        if (defineInvocation == null) {
            if (initializer != null || !visitor.getResult().isEmpty()) {
                createDefinitionPlace(initializer, descriptorToDefineInvocation);
            }
        }
        else {
            if (initializer != null) {
                assert defineInvocation.getInitializer() == JsLiteral.NULL;
                defineInvocation.setInitializer(initializer);
            }

            List<JsPropertyInitializer> listFromPlace = defineInvocation.getMembers();
            // if equals, so, inner functions was added
            if (listFromPlace != visitor.getResult()) {
                listFromPlace.addAll(visitor.getResult());
            }
        }
    }

    private JsPropertyInitializer getEntry(@NotNull NamespaceDescriptor descriptor, DefineInvocation defineInvocation) {
        return new JsPropertyInitializer(context().getNameForDescriptor(descriptor).makeRef(),
                                         new JsInvocation(context().namer().packageDefinitionMethodReference(), defineInvocation.asList()));
    }

    private static boolean addEntryIfParentExists(
            NamespaceDescriptor parentDescriptor,
            JsPropertyInitializer entry,
            Map<NamespaceDescriptor, DefineInvocation> descriptorToDeclarationPlace
    ) {
        DefineInvocation parentDefineInvocation = descriptorToDeclarationPlace.get(parentDescriptor);
        if (parentDefineInvocation != null) {
            parentDefineInvocation.getMembers().add(entry);
            return true;
        }
        return false;
    }

    private void addToParent(
            NamespaceDescriptor parentDescriptor,
            JsPropertyInitializer entry,
            Map<NamespaceDescriptor, DefineInvocation> descriptorToDefineInvocation
    ) {
        while (!addEntryIfParentExists(parentDescriptor, entry, descriptorToDefineInvocation)) {
            JsObjectLiteral members = new JsObjectLiteral(new SmartList<JsPropertyInitializer>(entry), true);
            DefineInvocation defineInvocation = DefineInvocation.create(parentDescriptor, null, members, context());
            entry = getEntry(parentDescriptor, defineInvocation);

            descriptorToDefineInvocation.put(parentDescriptor, defineInvocation);
            parentDescriptor = (NamespaceDescriptor) parentDescriptor.getContainingDeclaration();
        }
    }

}
