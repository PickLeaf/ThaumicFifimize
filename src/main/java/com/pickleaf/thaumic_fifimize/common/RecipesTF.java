package com.pickleaf.thaumic_fifimize.common;

import com.pickleaf.thaumic_fifimize.Config;

import net.minecraft.item.ItemStack;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.crafting.CrucibleRecipe;
import thaumcraft.api.crafting.InfusionRecipe;
import thaumcraft.api.items.ItemsTC;

public class RecipesTF {
    public static InfusionRecipe sealCopier;
    public static CrucibleRecipe primordialMote;
    public static InfusionRecipe primordialPearl;

    public static void init() {
        initializeInfusionRecipes();
        initializeAlchemyRecipes();
    }

    private static void initializeAlchemyRecipes() {

    }

    private static void initializeInfusionRecipes() {
        if (Config.ITEM_SEAL_COPIER)
            sealCopier = ThaumcraftApi.addInfusionCraftingRecipe("SEAL_COPIER",
                    new ItemStack(ItemsTF.sealCopier), 1,
                    (new AspectList())
                            .add(Aspect.MIND, 25)
                            .add(Aspect.MECHANISM, 10),
                    new ItemStack(ItemsTC.golemBell),
                    new Object[] {
                            new ItemStack(ItemsTC.seals, 1, 0),
                            new ItemStack(ItemsTC.brain),
                            new ItemStack(ItemsTC.brain) });
        if (Config.PRIMORDIAL_PEARL_DUP) {
            primordialMote = ThaumcraftApi.addCrucibleRecipe("PRIMORDIAL_PEARL_DUP",
                    new ItemStack(ItemsTF.primordialMote, 8),
                    new ItemStack(ItemsTC.primordialPearl),
                    (new AspectList())
                            .add(Aspect.FLUX, 40)
                            .add(Aspect.EXCHANGE, 40)
                            .add(Aspect.ENERGY, 40)
                            .add(Aspect.UNDEAD, 40));
            primordialPearl = ThaumcraftApi.addInfusionCraftingRecipe("PRIMORDIAL_PEARL_DUP",
                    new ItemStack(ItemsTC.primordialPearl), 6,
                    (new AspectList())
                            .add(Aspect.AIR, 64)
                            .add(Aspect.EARTH, 64)
                            .add(Aspect.FIRE, 64)
                            .add(Aspect.WATER, 64)
                            .add(Aspect.ORDER, 64)
                            .add(Aspect.ENTROPY, 64)
                            .add(Aspect.PLANT, 64)
                            .add(Aspect.FLUX, 64),
                    new ItemStack(ItemsTF.primordialMote),
                    new Object[] {
                            new ItemStack(ItemsTC.shard, 1, 0),
                            new ItemStack(ItemsTC.shard, 1, 1),
                            new ItemStack(ItemsTC.shard, 1, 2),
                            new ItemStack(ItemsTC.shard, 1, 3),
                            new ItemStack(ItemsTC.shard, 1, 4),
                            new ItemStack(ItemsTC.shard, 1, 5),
                            new ItemStack(ItemsTC.shard, 1, 6),
                            new ItemStack(ItemsTC.shard, 1, 7), });
        }
    }
}
