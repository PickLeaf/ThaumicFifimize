package com.pickleaf.thaumic_fifimize.core;
import net.minecraft.launchwrapper.IClassTransformer;

import org.objectweb.asm.*;

public class InventoryUtilsTransformer implements IClassTransformer {
    

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        if (transformedName.equals("thaumcraft.common.lib.utils.InventoryUtils")) {
            try {
                ClassReader cr = new ClassReader(basicClass);
                ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_MAXS);
                ClassVisitor cv = new InventoryUtilsClassVisitor(Opcodes.ASM5, cw);
                cr.accept(cv, 0);
                return cw.toByteArray();
            } catch (Exception e) {
                System.err.println("[ThaumicFifimize] Failed to transform class: " + transformedName);
                e.printStackTrace();
                return basicClass;
            }
        }
        return basicClass;
    }

    private static class InventoryUtilsClassVisitor extends ClassVisitor {
        public InventoryUtilsClassVisitor(int api, ClassVisitor cv) {
            super(api, cv);
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String desc,
                String signature, String[] exceptions) {
            MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);

            if (name.equals("findFirstMatchFromFilter") &&
                    desc.equals(
                            "([Lnet/minecraft/item/ItemStack;ZLnet/minecraft/inventory/IInventory;Lnet/minecraft/util/EnumFacing;ZZZZZ)Lnet/minecraft/item/ItemStack;")) {
                // public static ItemStack findFirstMatchFromFilter(ItemStack[] filters, boolean blacklist, IInventory inv, EnumFacing face, boolean ignoreDamage, boolean ignoreNBT, boolean useOre, boolean useMod, boolean leaveOne)
                return new HeadInjectMethodVisitor(Opcodes.ASM5, mv, 2);
            } else if (name.equals("inventoryContainsAmount")) {
                // public static int inventoryContainsAmount(IInventory inventory, ItemStack stack, EnumFacing side, boolean ignoreDamage, boolean ignoreNBT, boolean useOre, boolean useMod)
                return new HeadInjectMethodVisitor(Opcodes.ASM5, mv, 0);
            } else if (name.equals("extractStack")) {
                // public static ItemStack extractStack(IInventory inventory, ItemStack stack1, EnumFacing side, boolean ignoreDamage, boolean ignoreNBT, boolean useOre, boolean useMod, boolean doit)
                return new HeadInjectMethodVisitor(Opcodes.ASM5, mv, 0);
            } else if (name.equals("placeItemStackIntoInventory")) {
                // public static ItemStack placeItemStackIntoInventory(ItemStack stack, IInventory inventory, EnumFacing side, boolean doit)
                return new HeadInjectMethodVisitor(Opcodes.ASM5, mv, 1);
            }

            return mv;
        }
    }

    private static class HeadInjectMethodVisitor extends MethodVisitor {
        private int inventoryVarIndex;

        public HeadInjectMethodVisitor(int api, MethodVisitor mv, int inventoryVarIndex) {
            super(api, mv);
            this.inventoryVarIndex = inventoryVarIndex;
        }

        @Override
        public void visitCode() {
            super.visitCode();
            // 注入：inv = hookMethod(inv)
            // aload：加载局部变量（inv）到栈顶
            mv.visitVarInsn(Opcodes.ALOAD, inventoryVarIndex);
            // invokestatic：调用静态方法hookMethod
            mv.visitMethodInsn(
                    Opcodes.INVOKESTATIC,
                    "com/pickleaf/thaumic_fifimize/core/HookHandler", // 包名+类名
                    "hookMethod", // 方法名
                    "(Lnet/minecraft/inventory/IInventory;)Lnet/minecraft/inventory/IInventory;", // 方法描述符
                    false // 非接口方法
            );
            // astore：将栈顶的返回值存回局部变量（覆盖原inv）
            mv.visitVarInsn(Opcodes.ASTORE, inventoryVarIndex);
        }
    }
}