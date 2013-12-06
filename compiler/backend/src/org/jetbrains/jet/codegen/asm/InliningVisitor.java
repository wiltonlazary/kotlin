///*
// * Copyright 2010-2013 JetBrains s.r.o.
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// * http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
//package org.jetbrains.jet.codegen.asm;
//
//import org.jetbrains.asm4.Label;
//import org.jetbrains.asm4.MethodVisitor;
//import org.jetbrains.asm4.Opcodes;
//import org.jetbrains.asm4.Type;
//import org.jetbrains.asm4.commons.Method;
//import org.jetbrains.jet.codegen.ClosureCodegen;
//import org.jetbrains.jet.codegen.StackValue;
//
//import static org.jetbrains.jet.codegen.asm.InlineCodegenUtil.isFunctionConstructorCall;
//import static org.jetbrains.jet.codegen.asm.InlineCodegenUtil.isFunctionLiteralClass;
//import static org.jetbrains.jet.codegen.asm.InlineCodegenUtil.isInvokeOnInlinable;
//
//public class InliningVisitor extends InliningAdapter {
//
//    public InliningVisitor(
//            MethodVisitor mv,
//            int api,
//            String desc,
//            Label end,
//            int localsStart,
//            VarRemapper remapper
//    ) {
//        super(mv, api, desc, end, localsStart, remapper);
//    }
//
//    @Override
//    public void anew(Type type) {
//        if (isFunctionLiteralClass(type.getInternalName())) {
//            ConstructorInvocation invocation = constructorInvocation.get(0);
//            if (invocation.isInlinable()) {
//                CompiledFunctionTransformer transformer = new CompiledFunctionTransformer(state, invocation);
//                //transformer.doTransform(null);
//            } else {
//                super.anew(type);
//            }
//        } else {
//            super.anew(type);
//        }
//    }
//
//    @Override
//    public void visitMethodInsn(int opcode, String owner, String name, String desc) {
//        if (/*INLINE_RUNTIME.equals(owner) &&*/ isInvokeOnInlinable(owner, name)) { //TODO add method
//            assert !infos.isEmpty();
//            InlinableAccess inlinableAccess = infos.remove();
//            ClosureInfo info = expressionMap.get(inlinableAccess.index);
//
//            if (!inlinableAccess.isInlinable()) {
//                //noninlinable closure
//                super.visitMethodInsn(opcode, owner, name, desc);
//                return;
//            }
//
//            //TODO replace with codegen
//            int valueParamShift = getNextLocalIndex();
//            remapper.setNestedRemap(true);
//            putStackValuesIntoLocals(info.getParamsWithoutCapturedValOrVar(), valueParamShift, this, desc);
//            Label closureEnd = new Label();
//            InliningAdapter closureInliner = new InliningAdapter(mv, Opcodes.ASM4, desc, closureEnd, getNextLocalIndex(),
//                                                                 new VarRemapper.ClosureRemapper(info, valueParamShift, tempTypes));
//
//            info.getNode().instructions.resetLabels();
//            info.getNode().accept(closureInliner); //TODO
//
//            remapper.setNestedRemap(false);
//            mv.visitLabel(closureEnd);
//
//            Method bridge = typeMapper.mapSignature(ClosureCodegen.getInvokeFunction(info.getFunctionDescriptor())).getAsmMethod();
//            Method delegate = typeMapper.mapSignature(info.getFunctionDescriptor()).getAsmMethod();
//            StackValue.onStack(delegate.getReturnType()).put(bridge.getReturnType(), closureInliner);
//        }
//        else if (isFunctionConstructorCall(owner, name)) { //TODO add method
//            ConstructorInvocation invocation = constructorInvocation.remove(0);
//            super.visitMethodInsn(opcode, owner, name, desc);
//        }
//        else {
//            super.visitMethodInsn(opcode, owner, name, desc);
//        }
//    }
//}
