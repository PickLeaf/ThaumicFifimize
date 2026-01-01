package com.pickleaf.thaumic_fifimize.core;

import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.*;

public class SilverLeavesDropChanceTransformer implements IClassTransformer {

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        if (transformedName.equals("thaumcraft.common.blocks.world.plants.BlockLeavesTC")) {
            try {
                ClassReader cr = new ClassReader(basicClass);
                ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
                ClassVisitor cv = new DropChanceClassVisitor(Opcodes.ASM5, cw);
                cr.accept(cv, 0);
                return cw.toByteArray();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return basicClass;
    }

    private static class DropChanceClassVisitor extends ClassVisitor {

        public DropChanceClassVisitor(int api, ClassVisitor cv) {
            super(api, cv);
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String desc,
                String signature, String[] exceptions) {
            MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
            if (name.equals("func_176232_d")) {
                return new DropChanceMethodVisitor(Opcodes.ASM5, mv);
            }
            return mv;
        }
    }

    private static class DropChanceMethodVisitor extends MethodVisitor {

        public DropChanceMethodVisitor(int api, MethodVisitor mv) {
            super(api, mv);
        }

        @Override
        public void visitIntInsn(int opcode, int operand) {
            if (opcode == Opcodes.SIPUSH && operand == 200) {
                super.visitFieldInsn(Opcodes.GETSTATIC,
                        "com/pickleaf/thaumic_fifimize/Config", // 类名
                        "SILVERSAPLING_DROPCHANCE", // 字段名
                        "I"); // 字段类型描述符(I表示int)
            } else {
                super.visitIntInsn(opcode, operand);
            }
        }
    }
}