package com.pickleaf.thaumic_fifimize.core;

import java.util.HashMap;
import java.util.UUID;

import com.mojang.authlib.GameProfile;
import com.pickleaf.thaumic_fifimize.ThaumicFifimize;

import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IEntityOwnable;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import thaumcraft.api.golems.IGolemAPI;

public class HookHandler {
    public static IInventory getDoubleChestInv(IInventory inv) {
        if (inv instanceof TileEntityChest) {
            BlockPos pos = ((TileEntityChest) inv).getPos();
            World world = ((TileEntityChest) inv).getWorld();
            Block block = world.getBlockState(pos).getBlock();
            if (block instanceof BlockChest)
                inv = ((BlockChest) block).getLockableContainer(world, pos);
        }
        return inv;
    }

    public static String getOwnerPlayerName(IGolemAPI golem) {
        Entity entity = golem.getGolemEntity();
        if (entity instanceof IEntityOwnable) {
            GameProfile profile = MinecraftServer.getServer().getPlayerProfileCache()
                    .getProfileByUUID(UUID.fromString(((IEntityOwnable) entity).getOwnerId()));
            if (profile != null) {
                ThaumicFifimize.printErr(profile.getName());
                return profile.getName();
            }
        }
        return "FakeThaumcraftGolem";
    }

    public static ItemStack tickSealEmpty(ItemStack stack, HashMap<Integer, ItemStack> cache) {
        if (cache.containsValue(stack)){
            return null;
        }
        return stack;
    }
}
