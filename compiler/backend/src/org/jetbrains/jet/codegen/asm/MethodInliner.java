package org.jetbrains.jet.codegen.asm;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.asm4.Label;
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
import org.jetbrains.jet.codegen.state.JetTypeMapper;
import org.jetbrains.jet.lang.types.lang.InlineStrategy;

import java.util.*;

import static org.jetbrains.jet.codegen.asm.InlineCodegenUtil.isFunctionConstructorCall;
import static org.jetbrains.jet.codegen.asm.InlineCodegenUtil.isInvokeOnInlinable;
import static org.jetbrains.jet.codegen.asm.InlineTransformer.putStackValuesIntoLocals;

public class MethodInliner {

    private MethodNode node;
    private List<ParameterInfo> parameters;

    private InliningInfo parent;

    private final JetTypeMapper typeMapper;

    private final List<InlinableAccess> inlinableInvocation = new ArrayList<InlinableAccess>();
    private final List<ConstructorInvocation> constructorInvocation = new ArrayList<ConstructorInvocation>();

    private Map<Integer, Integer> transformed = new HashMap<Integer, Integer>();

    public MethodInliner(@NotNull MethodNode node, List<ParameterInfo> parameters, @NotNull InliningInfo parent) {
        this.node = node;
        this.parameters = parameters;
        this.parent = parent;
        this.typeMapper = parent.state.getTypeMapper();
    }

    public Label doTransformAndMergeWith(InstructionAdapter adapter) {
        //analyze body
        try {
            markPlacesForInlineAndRemoveInlinable(node);
        }
        catch (AnalyzerException e) {
            throw new RuntimeException(e);
        }
        Label end = new Label();

        MethodNode transformedNode = doInline(node, end, null);
        VarRemapper.ParamRemapper remapper = new VarRemapper.ParamRemapper(0, parameters.size(), 0, parameters);
        RemapVisitor visitor = new RemapVisitor(adapter, end, remapper);
        transformedNode.accept(visitor);
        return end;
    }

    private MethodNode doInline(MethodNode node, Label end, final Map<Integer, ClosureInfo> expressionMap) {

        final LinkedList<InlinableAccess> infos = new LinkedList<InlinableAccess>(inlinableInvocation);

        MethodNode resultNode = new MethodNode(node.access, node.name, node.desc, node.signature, null);

        CounterAdapter inliner = new CounterAdapter(resultNode, parameters.size()) {

            @Override
            public void visitMethodInsn(int opcode, String owner, String name, String desc) {
                if (/*INLINE_RUNTIME.equals(owner) &&*/ isInvokeOnInlinable(owner, name)) { //TODO add method
                    assert !infos.isEmpty();
                    InlinableAccess inlinableAccess = infos.remove();

                    if (!inlinableAccess.isInlinable()) {
                        //noninlinable closure
                        super.visitMethodInsn(opcode, owner, name, desc);
                        return;
                    }
                    ClosureInfo info = getLambda(inlinableAccess.index);

                    int valueParamShift = getNextLocalIndex();

                    putStackValuesIntoLocals(info.getParamsWithoutCapturedValOrVar(), valueParamShift, this, desc);

                    MethodInliner inliner = new MethodInliner(info.getNode(), inlinableAccess.getParameters(), new InliningInfo(null, null, null, null, parent.state));

                    Label lambdaEnd = inliner.doTransformAndMergeWith(new ShiftAdapter(this, valueParamShift));

                    mv.visitLabel(lambdaEnd);

                    Method bridge = typeMapper.mapSignature(ClosureCodegen.getInvokeFunction(info.getFunctionDescriptor())).getAsmMethod();
                    Method delegate = typeMapper.mapSignature(info.getFunctionDescriptor()).getAsmMethod();
                    StackValue.onStack(delegate.getReturnType()).put(bridge.getReturnType(), this);
                }
                //else if (inlineClosures && isFunctionConstructorCall(owner, name)) { //TODO add method
                //    ConstructorInvocation invocation = constructorInvocation.remove(0);
                //    super.visitMethodInsn(opcode, owner, name, desc);
                //}
                else {
                    super.visitMethodInsn(opcode, owner, name, desc);
                }
            }
        };

        node.accept(inliner);

        return resultNode;
    }

    public void merge() {

    }

    public MethodNode prepareNode(@NotNull MethodNode node, final int capturedVarSize) {
        //shift all variable to captured var size
        //MethodNode transformedNode = new MethodNode(node.access, node.name, node.desc, node.signature, null) {
        //    @Override
        //    public void visitVarInsn(int opcode, int var) {
        //        super.visitVarInsn(opcode, var + capturedVarSize);
        //    }
        //};
        //node.accept(transformedNode);
        return node;
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
                    ClosureInfo closureInfo =  getLambda(varIndex);
                    boolean isInlinable = closureInfo != null;
                    if (isInlinable) { //TODO: maybe add separate map for noninlinable inlinableInvocation
                        //remove inlinable access
                        node.instructions.remove(insnNode);
                    }

                    inlinableInvocation.add(new InlinableAccess(varIndex, isInlinable, getParametersInfo(desc)));
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
                                ClosureInfo closureInfo = getLambda(varIndex);
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

    public List<ParameterInfo> getParametersInfo(String desc) {
        List<ParameterInfo> result = new ArrayList<ParameterInfo>();

        //add skipped this cause closure doesn't have it
        result.add(ParameterInfo.STUB);
        int index = 1;

        Type[] types = Type.getArgumentTypes(desc);
        for (int i = 0; i < types.length; i++) {
            Type type = types[i];
            int paramIndex = index++;
            result.add(new ParameterInfo(type, false, -1, paramIndex));
            if (type.getSize() == 2) {
                result.add(ParameterInfo.STUB);
            }
        }
        return result;
    }

    public ClosureInfo getLambda(int index) {
        if (index < parameters.size()) {
            return parameters.get(index).getLambda();
        }
        return null;
    }
}
