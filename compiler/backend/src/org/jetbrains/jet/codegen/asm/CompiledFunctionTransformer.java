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
import org.jetbrains.asm4.*;
import org.jetbrains.asm4.tree.*;
import org.jetbrains.asm4.tree.analysis.Frame;
import org.jetbrains.asm4.tree.analysis.SourceValue;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CompiledFunctionTransformer extends InlineTransformer {

    private ConstructorInvocation invocation;

    private Map<String, Integer> paramMapping = new HashMap<String, Integer>();

    private MethodNode transformedConstructor;

    public CompiledFunctionTransformer(GenerationState state, ConstructorInvocation invocation) {
        super(state);
        this.invocation = invocation;

        VirtualFile file = InlineCodegenUtil.findVirtualFile(state.getProject(), new FqName(invocation.getTypeDesc()), false);
        if (file == null) {
            throw new RuntimeException("Couldn't find virtual file for " + invocation.getTypeDesc());
        }


        ClassReader reader;
        try {
            reader = new ClassReader(file.getInputStream());
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        MethodNode constructor = getClassConstructor(reader);
        extractParametersMapping(constructor);
    }

    public void doTransform(ClassBuilder builder) throws IOException {


    }

    private void extractParametersMapping(MethodNode constructor) {
        AbstractInsnNode cur = constructor.instructions.getFirst();
        cur = cur.getNext(); //skip super call
        int index = 0;
        while (cur != null) {
            if (cur.getType() == AbstractInsnNode.FIELD_INSN) {
                FieldInsnNode fieldNode = (FieldInsnNode) cur;
                VarInsnNode previous = (VarInsnNode) fieldNode.getPrevious();
                int varIndex = previous.var;
                paramMapping.put(fieldNode.name, varIndex);
                System.out.println(fieldNode.name + " "  + varIndex);
            }
            cur = cur.getNext();
            index++;
        }
    }

    public MethodNode getClassConstructor(ClassReader reader) {
        final MethodNode[] methodNode = new MethodNode[1];
        reader.accept(new ClassVisitor(InlineCodegenUtil.API) {

            @Override
            public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
                if ("<init>".equals(name)) {
                    assert methodNode[0] == null;
                    return methodNode[0] = new MethodNode(access, name, desc, signature, exceptions);
                }
                return null;
            }
        }, ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);

        if (methodNode[0] == null) {
            throw new RuntimeException("Coudn't find constructor of lambda class " + invocation.getTypeDesc());
        }

        return methodNode[0];
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
