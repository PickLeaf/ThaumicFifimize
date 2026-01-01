package com.pickleaf.thaumic_fifimize.core;

import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import thaumcraft.api.golems.seals.ISealEntity;

import org.objectweb.asm.*;

public class SealsInteractDoubleChestTransformer implements IClassTransformer {
    public static IInventory hookMethod(IInventory inv, World world, ISealEntity seal) {
        BlockPos pos = seal.getSealPos().pos;
        Block block = world.getBlockState(pos).getBlock();
        if (inv instanceof TileEntityChest && block instanceof BlockChest) {
            inv = ((BlockChest) block).getLockableContainer(world, pos);
        }
        return inv;
    }

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        if (transformedName.equals("thaumcraft.common.entities.construct.golem.seals.SealEmpty")) {
            System.out.println("[ThaumicFifimize] Transforming: " + transformedName);

            try {
                ClassReader cr = new ClassReader(basicClass);
                ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_MAXS);
                ClassVisitor cv = new SealEmptyClassVisitor(Opcodes.ASM5, cw);
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

    private static class SealEmptyClassVisitor extends ClassVisitor {
        public SealEmptyClassVisitor(int api, ClassVisitor cv) {
            super(api, cv);
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String desc,
                String signature, String[] exceptions) {
            MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);

            if (name.equals("tickSeal") &&
                    desc.equals("(Lnet/minecraft/world/World;Lthaumcraft/api/golems/seals/ISealEntity;)V")) {
                System.out.println("[ThaumicFifimize] Found target method: " + name);
                return new TickSealMethodVisitor(Opcodes.ASM5, mv);
            }

            return mv;
        }
    }

    private static class TickSealMethodVisitor extends MethodVisitor {
        private boolean inserted = false;

        public TickSealMethodVisitor(int api, MethodVisitor mv) {
            super(api, mv);
        }

        @Override
        public void visitTypeInsn(int opcode, String type) {
            if (!inserted && opcode == Opcodes.CHECKCAST &&
                    "net/minecraft/inventory/IInventory".equals(type)) {
                inserted = true;

                // 执行 CHECKCAST
                super.visitTypeInsn(opcode, type);

                // 现在栈顶是 (IInventory)te
                // 栈上还有之前的参数：ItemStack[] 和 boolean

                // 我们需要在不破坏栈的情况下调用 hook 方法
                // 思路：将栈顶的 IInventory 存储到局部变量，
                // 然后重新加载并调用 hook 方法，
                // 最后将结果放回栈顶

                // 存储栈顶的 IInventory 到临时变量（索引 10）
                super.visitVarInsn(Opcodes.ASTORE, 10);

                // 现在栈顶是 boolean，下面是 ItemStack[]
                // 存储 boolean 到临时变量（索引 11）
                super.visitVarInsn(Opcodes.ISTORE, 11);

                // 现在栈顶是 ItemStack[]，存储到临时变量（索引 12）
                super.visitVarInsn(Opcodes.ASTORE, 12);

                // 栈现在是空的
                // 加载 IInventory、world、seal 调用 hook 方法
                super.visitVarInsn(Opcodes.ALOAD, 10); // IInventory
                super.visitVarInsn(Opcodes.ALOAD, 1); // world
                super.visitVarInsn(Opcodes.ALOAD, 2); // seal
                super.visitMethodInsn(
                        Opcodes.INVOKESTATIC,
                        "com/pickleaf/thaumic_fifimize/core/SealsInteractDoubleChestTransformer",
                        "hookMethod",
                        "(Lnet/minecraft/inventory/IInventory;Lnet/minecraft/world/World;Lthaumcraft/api/golems/seals/ISealEntity;)Lnet/minecraft/inventory/IInventory;",
                        false);

                // 存储 hook 方法的返回值到临时变量 10
                super.visitVarInsn(Opcodes.ASTORE, 10);

                // 重新加载参数到栈上（保持原始顺序）
                super.visitVarInsn(Opcodes.ALOAD, 12); // ItemStack[]
                super.visitVarInsn(Opcodes.ILOAD, 11); // boolean
                super.visitVarInsn(Opcodes.ALOAD, 10); // hook 后的 IInventory

                return;
            }

            super.visitTypeInsn(opcode, type);
        }

        @Override
        public void visitMaxs(int maxStack, int maxLocals) {
            super.visitMaxs(Math.max(maxStack, 3), Math.max(maxLocals, 13));
        }
    }
}