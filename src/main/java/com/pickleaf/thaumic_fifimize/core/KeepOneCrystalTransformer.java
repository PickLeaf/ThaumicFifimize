package com.pickleaf.thaumic_fifimize.core;

import net.minecraft.launchwrapper.IClassTransformer;

import org.objectweb.asm.*;

import com.pickleaf.thaumic_fifimize.ThaumicFifimize;

public class KeepOneCrystalTransformer implements IClassTransformer {

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        if (transformedName.equals("thaumcraft.common.blocks.world.ore.BlockCrystal")) {
            try {
                ClassReader cr = new ClassReader(basicClass);
                ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_MAXS);
                ClassVisitor cv = new BlockCrystalClassVisitor(Opcodes.ASM5, cw);
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

    private static class BlockCrystalClassVisitor extends ClassVisitor {
        public BlockCrystalClassVisitor(int api, ClassVisitor cv) {
            super(api, cv);
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String desc,
                String signature, String[] exceptions) {
            MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
            if (name.equals("onWandRightClick") &&
                    desc.equals(
                            "(Lnet/minecraft/world/World;Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/util/BlockPos;Lnet/minecraft/util/EnumFacing;)Z")) {
                // public boolean onWandRightClick
                // (World world, ItemStack wandstack, EntityPlayer player, BlockPos pos,
                // EnumFacing side)
                return new onWandRightClickMethodVisitor(Opcodes.ASM5, mv);
            }

            return mv;
        }
    }

    private static class onWandRightClickMethodVisitor extends MethodVisitor {
        public onWandRightClickMethodVisitor(int api, MethodVisitor mv) {
            super(api, mv);
        }

        @Override
        public void visitMethodInsn(int opcode, String owner, String name,
                String desc, boolean itf) {
            // net/minecraft/world/World/setBlockToAir
            if (name.equals("func_175698_g")) {
                mv.visitInsn(Opcodes.ICONST_1);
                mv.visitInsn(Opcodes.IRETURN);
            } else {
                super.visitMethodInsn(opcode, owner, name, desc, itf);
            }
        }
    }
}