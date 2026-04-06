package com.pickleaf.thaumic_fifimize.common;

import net.minecraft.block.Block;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.common.blocks.devices.BlockLamp;

import com.pickleaf.thaumic_fifimize.ThaumicFifimize;
import com.pickleaf.thaumic_fifimize.common.tile.TileLampCrystal;
import com.pickleaf.thaumic_fifimize.Config;

public class BlocksTF {
    public static Block lamp_crystal;

    public static void init() {
        if (Config.LAMP_CRYSTAL) {
            lamp_crystal = registerBlock(new BlockLamp(TileLampCrystal.class), "lamp_crystal");
            GameRegistry.registerTileEntity(TileLampCrystal.class,
                    ThaumicFifimize.MODID.toLowerCase() + ":lamp_crystal");
        }
    }

    @SideOnly(Side.CLIENT)
    public static void initRenders() {
        if (Config.LAMP_CRYSTAL) {
            registerRender(lamp_crystal, "lamp_crystal");
        }
    }

    @SideOnly(Side.CLIENT)
    public static void registerRender(Block block, String name) {
        ModelResourceLocation model = new ModelResourceLocation(
                ThaumicFifimize.MODID.toLowerCase() + ":" + name,
                "inventory");
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0, model);
    }

    public static Block registerBlock(Block block, String name) {
        block.setUnlocalizedName(ThaumicFifimize.MODID.toLowerCase() + "." + name);
        block.setCreativeTab(ItemsTF.CREATIVE_TAB);
        GameRegistry.registerBlock(block, name);
        return block;
    }
}
