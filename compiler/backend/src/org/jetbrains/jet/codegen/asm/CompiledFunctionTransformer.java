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

package org.jetbrains.jet.codegen.asm;

import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.asm4.ClassReader;
import org.jetbrains.asm4.ClassVisitor;
import org.jetbrains.asm4.Type;
import org.jetbrains.jet.codegen.CallableMethod;
import org.jetbrains.jet.codegen.ClassBuilder;
import org.jetbrains.jet.codegen.StackValue;
import org.jetbrains.jet.codegen.context.MethodContext;
import org.jetbrains.jet.codegen.state.GenerationState;
import org.jetbrains.jet.lang.descriptors.ValueParameterDescriptor;
import org.jetbrains.jet.lang.psi.JetExpression;
import org.jetbrains.jet.lang.psi.JetFunctionLiteralExpression;
import org.jetbrains.jet.lang.resolve.name.FqName;

import java.io.IOException;

public class CompiledFunctionTransformer extends InlineTransformer {

    private ConstructorInvocation invocation;

    public CompiledFunctionTransformer(GenerationState state, ConstructorInvocation invocation) {
        super(state);
        this.invocation = invocation;
    }

    public void doTransform(ClassBuilder builder) throws IOException {
        VirtualFile file = InlineCodegenUtil.findVirtualFile(state.getProject(), new FqName(invocation.getTypeDesc()));
        //if (file == null) {
        //    throw new RuntimeException("Couldn't find virtual file for " + invocation.getTypeDesc());
        //}
        ////builder.defineClass();
        //
        //ClassReader reader = new ClassReader(file.getInputStream());
    }

    public void calculatedDataForTransformation() {

    }

    @Override
    public void inlineCall(CallableMethod callableMethod, ClassVisitor visitor) {

    }

    @Override
    public void putInLocal(
            Type type, StackValue stackValue, ValueParameterDescriptor valueParameterDescriptor
    ) {

    }

    @Override
    public boolean shouldPutValue(
            Type type, StackValue stackValue, MethodContext context, ValueParameterDescriptor descriptor
    ) {
        assert false;
        return false;
    }

    @Override
    public void putHiddenParams() {
        assert false;
    }

    @Override
    public void leaveTemps() {
        assert false;
    }

    @Override
    public boolean isInliningClosure(
            JetExpression expression, ValueParameterDescriptor valueParameterDescriptora
    ) {
        assert false;
        return false;
    }

    @Override
    public void rememberClosure(JetFunctionLiteralExpression expression, Type type) {

    }
}
