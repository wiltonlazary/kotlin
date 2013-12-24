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

package org.jetbrains.jet.lang.resolve.scopes;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.TestOnly;
import org.jetbrains.jet.lang.descriptors.*;
import org.jetbrains.jet.lang.resolve.name.LabelName;
import org.jetbrains.jet.lang.resolve.name.Name;
import org.jetbrains.jet.utils.Printer;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class DelegatingScope implements JetScope {
    private final DeclarationDescriptor containingDeclaration;
    private final String debugName;
    private final JetScope[] delegatedScopes;
    private Collection<DeclarationDescriptor> allDescriptors;
    private List<ReceiverParameterDescriptor> implicitReceiverHierarchy;

    public DelegatingScope(DeclarationDescriptor containingDeclaration, String debugName, JetScope... scopes) {
        this.containingDeclaration = containingDeclaration;
        delegatedScopes = scopes.clone();

        this.debugName = debugName;
    }

    @Override
    public ClassifierDescriptor getClassifier(@NotNull Name name) {
        return getFirstCheckAllScopes(name, JetScopeSelectorUtil.CLASSIFIER_DESCRIPTOR_SCOPE_SELECTOR);
    }

    @Override
    public PackageViewDescriptor getPackage(@NotNull Name name) {
        return getFirstCheckAllScopes(name, JetScopeSelectorUtil.PACKAGE_SCOPE_SELECTOR);
    }

    @NotNull
    @Override
    public Set<VariableDescriptor> getProperties(@NotNull Name name) {
        return getFromAllScopes(name, JetScopeSelectorUtil.NAMED_PROPERTIES_SCOPE_SELECTOR);
    }

    @Override
    public VariableDescriptor getLocalVariable(@NotNull Name name) {
        return getFirstCheckAllScopes(name, JetScopeSelectorUtil.VARIABLE_DESCRIPTOR_SCOPE_SELECTOR);
    }

    @NotNull
    @Override
    public Set<FunctionDescriptor> getFunctions(@NotNull Name name) {
        return getFromAllScopes(name, JetScopeSelectorUtil.NAMED_FUNCTION_SCOPE_SELECTOR);
    }

    @NotNull
    @Override
    public List<ReceiverParameterDescriptor> getImplicitReceiversHierarchy() {
        if (implicitReceiverHierarchy == null) {
            implicitReceiverHierarchy = Lists.newArrayList();
            for (JetScope jetScope : delegatedScopes) {
                implicitReceiverHierarchy.addAll(jetScope.getImplicitReceiversHierarchy());
            }
        }
        return implicitReceiverHierarchy;
    }

    @NotNull
    @Override
    public DeclarationDescriptor getContainingDeclaration() {
        return containingDeclaration;
    }

    @NotNull
    @Override
    public Collection<DeclarationDescriptor> getDeclarationsByLabel(@NotNull LabelName labelName) {
        for (JetScope jetScope : delegatedScopes) {
            Collection<DeclarationDescriptor> declarationsByLabel = jetScope.getDeclarationsByLabel(labelName);
            if (!declarationsByLabel.isEmpty()) return declarationsByLabel; // TODO : merge?
        }
        return Collections.emptyList();
    }

    @NotNull
    @Override
    public Collection<DeclarationDescriptor> getAllDescriptors() {
        if (allDescriptors == null) {
            allDescriptors = Sets.newHashSet();
            for (JetScope scope : delegatedScopes) {
                allDescriptors.addAll(scope.getAllDescriptors());
            }
        }
        return allDescriptors;
    }

    @NotNull
    @Override
    public Collection<DeclarationDescriptor> getOwnDeclaredDescriptors() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String toString() {
        return debugName;
    }

    @TestOnly
    @Override
    public void printScopeStructure(@NotNull Printer p) {
        p.println(getClass().getSimpleName(), ": ", debugName, " {");
        p.pushIndent();

        for (JetScope scope : delegatedScopes) {
            scope.printScopeStructure(p);
        }

        p.popIndent();
        p.println("}");
    }

    @Nullable
    private <D extends DeclarationDescriptor> D getFirstCheckAllScopes(
            Name name, JetScopeSelectorUtil.ScopeByNameSelector<D> descriptorSelector
    ) {
        D result = null;

        for (JetScope scope : delegatedScopes) {
            D descriptor = descriptorSelector.get(scope, name);
            if (result == null && descriptor != null) {
                result = descriptor;
            }
        }

        return result;
    }

    @NotNull
    private <D extends DeclarationDescriptor> Set<D> getFromAllScopes(
            Name name, JetScopeSelectorUtil.ScopeByNameMultiSelector<D> descriptorsSelector
    ) {
        if (delegatedScopes.length == 0) {
            return Collections.emptySet();
        }

        Set<D> descriptors = Sets.newLinkedHashSet();

        for (JetScope jetScope : delegatedScopes) {
            descriptors.addAll(descriptorsSelector.get(jetScope, name));
        }

        return descriptors;
    }
}
