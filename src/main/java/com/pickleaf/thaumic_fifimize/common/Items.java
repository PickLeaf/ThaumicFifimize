package com.pickleaf.thaumic_fifimize.common;

import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.pickleaf.thaumic_fifimize.ThaumicFifimize;
import com.pickleaf.thaumic_fifimize.common.item.*;

public class Items {
    public static Item sealCopier;

    public static void init() {
        sealCopier = initializeItem(new ItemSealCopier());
    }

    @SideOnly(Side.CLIENT)
    public static void initRenders() {
        registerRender(sealCopier);
        registerRender(sealCopier, 1, "_holding");
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
            return sealCopier;
        }
    };
}
