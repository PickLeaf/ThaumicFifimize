package com.pickleaf.thaumic_fifimize.core;

import net.minecraft.launchwrapper.IClassTransformer;

import org.objectweb.asm.*;

import com.pickleaf.thaumic_fifimize.ThaumicFifimize;

public class SealEmptyTransformer implements IClassTransformer {

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        if (transformedName.equals("thaumcraft.common.entities.construct.golem.seals.SealEmpty")) {
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
        private String className;

        public SealUseClassVisitor(int api, ClassVisitor cv) {
            super(api, cv);
        }

        @Override
        public void visit(int version, int access, String name, String signature, String superName,
                String[] interfaces) {
            this.className = name;
            super.visit(version, access, name, signature, superName, interfaces);
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String desc,
                String signature, String[] exceptions) {
            MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
            if (name.equals("tickSeal") &&
                    desc.equals(
                            "(Lnet/minecraft/world/World;Lthaumcraft/api/golems/seals/ISealEntity;)V")) {
                // public void tickSeal(World world, ISealEntity seal)
                return new tickSealMethodVisitor(Opcodes.ASM5, mv, className);
            }

            return mv;
        }
    }

    private static class tickSealMethodVisitor extends MethodVisitor {
        private final String sealEmptyClassName;
        private boolean needInject;

        public tickSealMethodVisitor(int api, MethodVisitor mv, String className) {
            super(api, mv);
            this.sealEmptyClassName = className;
            this.needInject = false;
        }

        @Override
        public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
            super.visitMethodInsn(opcode, owner, name, desc, itf);

            // 检测是否是L14标签内的InventoryUtils.findFirstMatchFromFilter调用
            if (opcode == Opcodes.INVOKESTATIC
                    && owner.equals("thaumcraft/common/lib/utils/InventoryUtils")
                    && name.equals("findFirstMatchFromFilter")
                    && desc.equals(
                            "([Lnet/minecraft/item/ItemStack;ZLnet/minecraft/inventory/IInventory;Lnet/minecraft/util/EnumFacing;ZZZZZ)Lnet/minecraft/item/ItemStack;")) {
                // 标记：需要在该指令后注入
                needInject = true;
            }
        }

        @Override
        public void visitVarInsn(int opcode, int var) {
            if (opcode == Opcodes.ASTORE && var == 4 && needInject) {
                // 此时栈顶：ItemStack（findFirstMatchFromFilter的返回值）
                // 步骤1：aload 0 → 加载this（SealEmpty实例）
                mv.visitVarInsn(Opcodes.ALOAD, 0);
                // 步骤2：getfield → 获取SealEmpty的cache字段（HashMap）
                mv.visitFieldInsn(Opcodes.GETFIELD, sealEmptyClassName, "cache", "Ljava/util/HashMap;");
                // 步骤4：invokestatic → 调用tickSealEmpty静态方法
                mv.visitMethodInsn(
                        Opcodes.INVOKESTATIC,
                        "com/pickleaf/thaumic_fifimize/core/HookHandler", // 包名
                        "tickSealEmpty", // 方法名
                        "(Lnet/minecraft/item/ItemStack;Ljava/util/HashMap;)Lnet/minecraft/item/ItemStack;", // 方法描述符
                        false // 非接口方法
                );
                needInject = false;
            }
            super.visitVarInsn(opcode, var);
        }
    }
}