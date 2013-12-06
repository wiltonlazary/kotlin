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

import com.google.common.collect.Lists;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.asm4.Label;
import org.jetbrains.asm4.MethodVisitor;
import org.jetbrains.asm4.Opcodes;
import org.jetbrains.asm4.Type;
import org.jetbrains.asm4.commons.InstructionAdapter;
import org.jetbrains.asm4.commons.Method;
import org.jetbrains.asm4.tree.AbstractInsnNode;
import org.jetbrains.asm4.tree.MethodInsnNode;
import org.jetbrains.asm4.tree.MethodNode;
import org.jetbrains.asm4.tree.VarInsnNode;
import org.jetbrains.asm4.tree.analysis.*;
import org.jetbrains.jet.codegen.ClosureCodegen;
import org.jetbrains.jet.codegen.StackValue;
import org.jetbrains.jet.codegen.state.GenerationState;
import org.jetbrains.jet.codegen.state.JetTypeMapper;

import java.util.*;

public abstract class InlineTransformer implements Inliner {

    public final static String INVOKE = "invoke";

    protected final List<InlinableAccess> inlinableInvocation = new ArrayList<InlinableAccess>();

    protected final List<ConstructorInvocation> constructorInvocation = new ArrayList<ConstructorInvocation>();

    protected final JetTypeMapper typeMapper;

    protected final List<ParameterInfo> tempTypes = new ArrayList<ParameterInfo>();

    protected final Map<Integer, ClosureInfo> expressionMap = new HashMap<Integer, ClosureInfo>();

    protected final GenerationState state;

    public InlineTransformer(@NotNull GenerationState state) {
        this.state = state;
        this.typeMapper = state.getTypeMapper();
    }

    public static void putStackValuesIntoLocals(List<Type> directOrder, int shift, InstructionAdapter mv, String descriptor) {
        Type[] actualParams = Type.getArgumentTypes(descriptor); //last param is closure itself
        assert actualParams.length == directOrder.size() : "Number of expected and actual params should be equals!";

        int size = 0;
        for (Type next : directOrder) {
            size += next.getSize();
        }

        shift += size;
        int index = directOrder.size();

        for (Type next : Lists.reverse(directOrder)) {
            shift -= next.getSize();
            Type typeOnStack = actualParams[--index];
            if (!typeOnStack.equals(next)) {
                StackValue.onStack(typeOnStack).put(next, mv);
            }
            mv.visitVarInsn(next.getOpcode(Opcodes.ISTORE), shift);
        }
    }

    protected void doInline(
            int access,
            String desc,
            MethodVisitor mv,
            MethodNode methodNode,
            int frameSize,
            final boolean inlineClosures,
            @NotNull VarRemapper remapper
    ) {

        Label end = new Label();

        final LinkedList<InlinableAccess> infos = new LinkedList<InlinableAccess>(inlinableInvocation);
        methodNode.instructions.resetLabels();

        InliningAdapter inliner = new InliningAdapter(mv, Opcodes.ASM4, end, frameSize, remapper) {

            @Override
            public void anew(Type type) {
                if (inlineClosures && isFunctionLiteralClass(type.getInternalName())) {
                    ConstructorInvocation invocation = constructorInvocation.get(0);
                    if (invocation.isInlinable()) {
                        CompiledFunctionTransformer transformer = new CompiledFunctionTransformer(state, invocation);
                        //transformer.doTransform(null);
                    } else {
                        super.anew(type);
                    }
                } else {
                    super.anew(type);
                }
            }

            @Override
            public void visitMethodInsn(int opcode, String owner, String name, String desc) {
                if (inlineClosures && /*INLINE_RUNTIME.equals(owner) &&*/ isInvokeOnInlinable(owner, name)) { //TODO add method
                    assert !infos.isEmpty();
                    InlinableAccess inlinableAccess = infos.remove();
                    ClosureInfo info = expressionMap.get(inlinableAccess.index);

                    if (!inlinableAccess.isInlinable()) {
                        //noninlinable closure
                        super.visitMethodInsn(opcode, owner, name, desc);
                        return;
                    }

                    //TODO replace with codegen
                    int valueParamShift = getNextLocalIndex();
                    remapper.setNestedRemap(true);
                    putStackValuesIntoLocals(info.getParamsWithoutCapturedValOrVar(), valueParamShift, this, desc);
                    Label closureEnd = new Label();
                    InliningAdapter closureInliner = new InliningAdapter(mv, Opcodes.ASM4, closureEnd, getNextLocalIndex(),
                                                                         new VarRemapper.ClosureRemapper(info, valueParamShift, tempTypes));

                    info.getNode().instructions.resetLabels();
                    info.getNode().accept(closureInliner); //TODO

                    remapper.setNestedRemap(false);
                    mv.visitLabel(closureEnd);

                    Method bridge = typeMapper.mapSignature(ClosureCodegen.getInvokeFunction(info.getFunctionDescriptor())).getAsmMethod();
                    Method delegate = typeMapper.mapSignature(info.getFunctionDescriptor()).getAsmMethod();
                    StackValue.onStack(delegate.getReturnType()).put(bridge.getReturnType(), closureInliner);
                }
                else if (inlineClosures && isFunctionConstructorCall(owner, name)) { //TODO add method
                    ConstructorInvocation invocation = constructorInvocation.remove(0);
                    super.visitMethodInsn(opcode, owner, name, desc);
                }
                else {
                    super.visitMethodInsn(opcode, owner, name, desc);
                }
            }
        };

        methodNode.accept(inliner);

        mv.visitLabel(end);
    }

    protected boolean isInvokeOnInlinable(String owner, String name) {
        return INVOKE.equals(name) && /*TODO: check type*/owner.contains("Function");
    }

    protected boolean isFunctionConstructorCall(@NotNull String internalName, @NotNull String name) {
        if (!"<init>".equals(name)) {
            return false;
        }

        return isFunctionLiteralClass(internalName);
    }

    private boolean isFunctionLiteralClass(String internalName) {
        String shortName = getLastNamePart(internalName);
        int index = shortName.lastIndexOf("$");

        if (index < 0) {
            return false;
        }

        String suffix = shortName.substring(index + 1);
        for (char c : suffix.toCharArray()) {
            if (!Character.isDigit(c)) return false;
        }
        return true;
    }

    @NotNull
    private String getLastNamePart(@NotNull String internalName) {
        int index = internalName.lastIndexOf("/");
        return index < 0 ? internalName : internalName.substring(index + 1);
    }

    private boolean isInitCallOfFunction(String owner, String name) {
        return "<init>".equals(name);
    }

    protected void markPlacesForInlineAndRemoveInlinable(@NotNull MethodNode node) throws AnalyzerException {
        Analyzer<SourceValue> analyzer = new Analyzer<SourceValue>(new SourceInterpreter());
        Frame<SourceValue>[] sources = analyzer.analyze("fake", node);

        AbstractInsnNode cur = node.instructions.getFirst();
        int index = 0;
        while (cur != null) {
            if (cur.getType() == AbstractInsnNode.METHOD_INSN) {
                MethodInsnNode methodInsnNode = (MethodInsnNode) cur;
                String owner = methodInsnNode.owner;
                String desc = methodInsnNode.desc;
                String name = methodInsnNode.name;
                //TODO check closure
                int paramLength = Type.getArgumentTypes(desc).length + 1;//non static
                if (isInvokeOnInlinable(owner, name) /*&& methodInsnNode.owner.equals(INLINE_RUNTIME)*/) {
                    Frame<SourceValue> frame = sources[index];
                    SourceValue sourceValue = frame.getStack(frame.getStackSize() - paramLength);
                    assert sourceValue.insns.size() == 1;

                    AbstractInsnNode insnNode = sourceValue.insns.iterator().next();
                    assert insnNode.getType() == AbstractInsnNode.VAR_INSN && insnNode.getOpcode() == Opcodes.ALOAD;

                    int varIndex = ((VarInsnNode) insnNode).var;
                    ClosureInfo closureInfo = expressionMap.get(varIndex);
                    boolean isInlinable = closureInfo != null;
                    if (isInlinable) { //TODO: maybe add separate map for noninlinable inlinableInvocation
                        //remove inlinable access
                        node.instructions.remove(insnNode);
                    }
                    inlinableInvocation.add(new InlinableAccess(varIndex, isInlinable, null));
                }
                else if (isFunctionConstructorCall(owner, name)) {
                    Frame<SourceValue> frame = sources[index];
                    ArrayList<InlinableAccess> infos = new ArrayList<InlinableAccess>();
                    int paramStart = frame.getStackSize() - paramLength;

                    for (int i = 0; i < paramLength; i++) {
                        SourceValue sourceValue = frame.getStack(paramStart + i);
                        if (sourceValue.insns.size() == 1) {
                            AbstractInsnNode insnNode = sourceValue.insns.iterator().next();
                            if (insnNode.getType() == AbstractInsnNode.VAR_INSN && insnNode.getOpcode() == Opcodes.ALOAD) {
                                int varIndex = ((VarInsnNode) insnNode).var;
                                ClosureInfo closureInfo = expressionMap.get(varIndex);
                                if (closureInfo != null) {
                                    InlinableAccess inlinableAccess = new InlinableAccess(varIndex, true, null);
                                    inlinableAccess.setInfo(closureInfo);
                                    infos.add(inlinableAccess);

                                    //remove inlinable access
                                    node.instructions.remove(insnNode);
                                }
                            }
                        }
                    }

                    ConstructorInvocation invocation = new ConstructorInvocation(owner, infos);
                    constructorInvocation.add(invocation);
                }
            }
            cur = cur.getNext();
            index++;
        }
    }
}
