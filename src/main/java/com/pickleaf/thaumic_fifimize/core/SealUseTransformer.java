package com.pickleaf.thaumic_fifimize.core;

import net.minecraft.launchwrapper.IClassTransformer;

import org.objectweb.asm.*;

import com.pickleaf.thaumic_fifimize.ThaumicFifimize;

public class SealUseTransformer implements IClassTransformer {

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        if (transformedName.equals("thaumcraft.common.entities.construct.golem.seals.SealUse")) {
            try {
                ClassReader cr = new ClassReader(basicClass);
                ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_MAXS);
                ClassVisitor cv = new SealUseClassVisitor(Opcodes.ASM5, cw);
                cr.accept(cv, 0);
                return cw.toByteArray();
            } catch (Exception e) {
                ThaumicFifimize.printErr("Failed to transform class: " + transformedName);
                e.printStackTrace();
                return basicClass;
            }
        }
        return basicClass;
    }

    private static class SealUseClassVisitor extends ClassVisitor {
        public SealUseClassVisitor(int api, ClassVisitor cv) {
            super(api, cv);
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String desc,
                String signature, String[] exceptions) {
            MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);

            if (name.equals("onTaskCompletion") &&
                    desc.equals(
                            "(Lnet/minecraft/world/World;Lthaumcraft/api/golems/IGolemAPI;Lthaumcraft/api/golems/tasks/Task;)Z")) {
                // public boolean onTaskCompletion(World world, IGolemAPI golem, Task task)
                return new onTaskCompletionMethodVisitor(Opcodes.ASM5, mv);
            }

            return mv;
        }
    }

    private static class onTaskCompletionMethodVisitor extends MethodVisitor {
        public onTaskCompletionMethodVisitor(int api, MethodVisitor mv) {
            super(api, mv);
        }
        // 拦截常量加载指令，替换目标字符串
        @Override
        public void visitLdcInsn(Object cst) {
            // 匹配到目标字符串时，替换为方法调用
            if ("FakeThaumcraftGolem".equals(cst)) {
                // 加载golem变量(（)局部变量索引2)→ 栈：[golem]
                super.visitVarInsn(Opcodes.ALOAD, 2);
                // 调用静态方法getOwnerPlayerName(golem) → 栈：[返回的字符串]
                super.visitMethodInsn(
                        Opcodes.INVOKESTATIC, // 静态方法调用
                        "com/pickleaf/thaumic_fifimize/core/HookHandler", // Hook类路径
                        "getOwnerPlayerName", // 方法名
                        "(Lthaumcraft/api/golems/IGolemAPI;)Ljava/lang/String;", // 方法描述符
                        false // 非接口方法
                );
                return;
            }
            // 非目标字符串，执行原指令
            super.visitLdcInsn(cst);
        }

        // 手动维护栈最大值(必须！！！)
        @Override
        public void visitMaxs(int maxStack, int maxLocals) {
            // 调用hook方法需要1个操作数栈空间，原LDC也是1个，所以maxStack保持不变或设为1即可
            super.visitMaxs(Math.max(maxStack, 1), maxLocals);
        }
    }
}