package com.pickleaf.thaumic_fifimize.common;

import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.pickleaf.thaumic_fifimize.ThaumicFifimize;
import com.pickleaf.thaumic_fifimize.common.item.*;
import com.pickleaf.thaumic_fifimize.Config;

public class ItemsTF {
    public static Item sealCopier;
    public static Item primordialMote;

    public static void init() {
        if (Config.ITEM_SEAL_COPIER)
            sealCopier = initializeItem(new ItemSealCopier());
        if (Config.PRIMORDIAL_PEARL_DUP)
            primordialMote = initializeItem(new ItemPrimordialMote());
    }

    @SideOnly(Side.CLIENT)
    public static void initRenders() {
        if (Config.ITEM_SEAL_COPIER) {
            registerRender(sealCopier);
            registerRender(sealCopier, 1, "_holding");
        }
        if (Config.PRIMORDIAL_PEARL_DUP)
            registerRender(primordialMote);
    }

    public static Item initializeItem(Item item) {
        String name = ((IHasName) item).getName();
        item.setUnlocalizedName(ThaumicFifimize.MODID.toLowerCase() + "." + name);
        item.setCreativeTab(CREATIVE_TAB);
        GameRegistry.registerItem(item, name);
        return item;
    }

    @SideOnly(Side.CLIENT)
    public static void registerRender(Item item) {
        String name = ((IHasName) item).getName();
        ModelResourceLocation model = new ModelResourceLocation(
                ThaumicFifimize.MODID.toLowerCase() + ":" + name,
                "inventory");
        ModelLoader.setCustomModelResourceLocation(item, 0, model);
    }

    @SideOnly(Side.CLIENT)
    public static void registerRender(Item item, int meat, String variant) {
        String name = ((IHasName) item).getName();
        ModelResourceLocation model = new ModelResourceLocation(
                ThaumicFifimize.MODID.toLowerCase() + ":" + name + variant,
                "inventory");
        ModelLoader.setCustomModelResourceLocation(item, meat, model);
    }

    public static CreativeTabs CREATIVE_TAB = new CreativeTabs(ThaumicFifimize.MODID) {
        @Override
        public Item getTabIconItem() {
            if (Config.ITEM_SEAL_COPIER)
                return sealCopier;
            else
                return Item.getItemFromBlock(Blocks.barrier);
        }
    };
}
