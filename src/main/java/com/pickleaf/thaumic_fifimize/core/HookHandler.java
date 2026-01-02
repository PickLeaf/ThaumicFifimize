package com.pickleaf.thaumic_fifimize.core;

import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class HookHandler {
    public static IInventory hookMethod(IInventory inv) {
        if (inv instanceof TileEntityChest) {
            BlockPos pos = ((TileEntityChest) inv).getPos();
            World world = ((TileEntityChest) inv).getWorld();
            Block block = world.getBlockState(pos).getBlock();
            if (block instanceof BlockChest)
                inv = ((BlockChest) block).getLockableContainer(world, pos);
        }
        return inv;
    }
}
