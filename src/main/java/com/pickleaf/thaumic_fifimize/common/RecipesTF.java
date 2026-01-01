package com.pickleaf.thaumic_fifimize.common;

import java.util.HashMap;

import net.minecraft.item.ItemStack;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.crafting.InfusionRecipe;
import thaumcraft.api.items.ItemsTC;

public class RecipesTF {
    public static InfusionRecipe sealCopier;

    public static void init() {
        initializeInfusionRecipes();
        initializeAlchemyRecipes();
    }

    private static void initializeAlchemyRecipes() {

    }

    private static void initializeInfusionRecipes() {
        sealCopier = ThaumcraftApi.addInfusionCraftingRecipe("SEAL_COPIER", new ItemStack(ItemsTF.sealCopier), 1,
                (new AspectList()).add(Aspect.MIND, 25).add(Aspect.MECHANISM, 10),
                new ItemStack(ItemsTC.golemBell),
                new Object[] { new ItemStack(ItemsTC.seals, 1, 0),
                        new ItemStack(ItemsTC.brain), new ItemStack(ItemsTC.brain) });
    }
}
