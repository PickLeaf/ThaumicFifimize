package com.pickleaf.thaumic_fifimize.common;

import com.pickleaf.thaumic_fifimize.Config;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.crafting.CrucibleRecipe;
import thaumcraft.api.crafting.InfusionRecipe;
import thaumcraft.api.crafting.ShapelessArcaneRecipe;
import thaumcraft.api.crafting.ShapedArcaneRecipe;
import thaumcraft.api.items.ItemsTC;
import thaumcraft.api.golems.GolemHelper;
import thaumcraft.api.blocks.BlocksTC;

public class RecipesTF {
    public static InfusionRecipe sealCopier;
    public static CrucibleRecipe primordialMote;
    public static InfusionRecipe primordialPearl;
    public static InfusionRecipe sealArcaneCraft;
    // 石头复制配方
    public static CrucibleRecipe stoneDuplication_1;
    public static CrucibleRecipe stoneDuplication_2;
    public static CrucibleRecipe stoneDuplication_3;
    // 世界基质制造配方
    public static ShapelessArcaneRecipe gravel;
    public static ShapelessArcaneRecipe sand;
    public static ShapedArcaneRecipe netherrack;
    public static ShapedArcaneRecipe soulSand;
    public static ShapedArcaneRecipe endStone;
    // 魔法植物增生配方
    public static InfusionRecipe shimmerleaf;
    public static InfusionRecipe cinderpearl;
    public static InfusionRecipe vishroom;
    // 银树炼水银配方
    public static CrucibleRecipe quicksilver;
    // 矿物质复制配方
    public static CrucibleRecipe redstone;
    public static CrucibleRecipe lapisLazuli;
    public static CrucibleRecipe quartz;

    public static void init() {
        initializeArcaneRecipes();
        initializeInfusionRecipes();
        initializeAlchemyRecipes();
    }

    private static void initializeArcaneRecipes() {
        // 世界基质制造配方
        if (Config.STONE_MANUFACTURE) {
            recipeStoneManuFfacture();
        }
    }

    private static void initializeAlchemyRecipes() {
        // 石头复制配方
        if (Config.STONE_DUP) {
            recipeStoneDuplication();
        }
        // 银树炼水银配方
        if (Config.QUICKSILVER) {
            quicksilver = ThaumcraftApi.addCrucibleRecipe("PUREMETAL",
                    new ItemStack(ItemsTC.quicksilver),
                    new ItemStack(Item.getItemFromBlock(BlocksTC.log), 1, 3),
                    (new AspectList())
                            .add(Aspect.FIRE, 3)
                            .add(Aspect.ORDER, 1));
        }
        // 矿物质复制配方
        if (Config.MINERAL_COPY) {
            redstone = ThaumcraftApi.addCrucibleRecipe("ALCHEMICALDUPLICATION",
                    new ItemStack(Items.redstone, 2),
                    new ItemStack(Items.redstone),
                    (new AspectList())
                            .add(Aspect.ENERGY, 2));
            lapisLazuli = ThaumcraftApi.addCrucibleRecipe("ALCHEMICALDUPLICATION",
                    new ItemStack(Items.dye, 2, 4),
                    new ItemStack(Items.dye, 1, 4),
                    (new AspectList())
                            .add(Aspect.SENSES, 1));
            quartz = ThaumcraftApi.addCrucibleRecipe("ALCHEMICALDUPLICATION",
                    new ItemStack(Items.quartz, 2),
                    new ItemStack(Items.quartz),
                    (new AspectList())
                            .add(Aspect.ENERGY, 1)
                            .add(Aspect.CRYSTAL, 1));
        }
    }

    private static void initializeInfusionRecipes() {
        if (Config.ITEM_SEAL_COPIER) {
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
        }
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
        if (Config.SEAL_ARCANE_CRAFT) {
            sealArcaneCraft = ThaumcraftApi.addInfusionCraftingRecipe("SEAL_ARCANE_CRAFT",
                    GolemHelper.getSealStack(SealsTF.sealArcaneCraft.getKey()), 3,
                    (new AspectList())
                            .add(Aspect.MIND, 16)
                            .add(Aspect.CRAFT, 16)
                            .add(Aspect.DESIRE, 8)
                            .add(Aspect.EXCHANGE, 4)
                            .add(Aspect.SENSES, 4),
                    GolemHelper.getSealStack("Thaumcraft:use"),
                    new Object[] {
                            new ItemStack(BlocksTC.arcaneWorkbench),
                            new ItemStack(Item.getItemFromBlock(Blocks.crafting_table)),
                            new ItemStack(ItemsTC.wand),
                            new ItemStack(ItemsTC.salisMundus),
                            new ItemStack(ItemsTC.salisMundus),
                            new ItemStack(ItemsTC.salisMundus) });
        }
        // 魔法植物增生配方
        if (Config.MAGIC_PLANT) {
            recipePlantHyperplasia();
        }
    }

    private static void recipeStoneDuplication() {
        stoneDuplication_1 = ThaumcraftApi.addCrucibleRecipe("STONE_DUP",
                new ItemStack(Blocks.stone, 2, 1),
                new ItemStack(Blocks.stone, 1, 1),
                (new AspectList()).add(Aspect.EARTH, 2));
        stoneDuplication_2 = ThaumcraftApi.addCrucibleRecipe("STONE_DUP",
                new ItemStack(Blocks.stone, 2, 3),
                new ItemStack(Blocks.stone, 1, 3),
                (new AspectList()).add(Aspect.EARTH, 2));
        stoneDuplication_3 = ThaumcraftApi.addCrucibleRecipe("STONE_DUP",
                new ItemStack(Blocks.stone, 2, 5),
                new ItemStack(Blocks.stone, 1, 5),
                (new AspectList()).add(Aspect.EARTH, 2));
    }

    private static void recipeStoneManuFfacture() {
        gravel = ThaumcraftApi.addShapelessArcaneCraftingRecipe("STONE_MANUFACTURE",
                new ItemStack(Blocks.gravel, 5),
                (new AspectList())
                        .add(Aspect.ENTROPY, 1),
                new Object[] {
                        new ItemStack(Blocks.cobblestone),
                        new ItemStack(Blocks.cobblestone),
                        new ItemStack(Blocks.gravel),
                        new ItemStack(Blocks.gravel),
                        new ItemStack(Blocks.gravel) });
        sand = ThaumcraftApi.addShapelessArcaneCraftingRecipe("STONE_MANUFACTURE",
                new ItemStack(Blocks.sand, 5),
                (new AspectList())
                        .add(Aspect.ENTROPY, 1),
                new Object[] {
                        new ItemStack(Blocks.gravel),
                        new ItemStack(Blocks.gravel),
                        new ItemStack(Blocks.sand),
                        new ItemStack(Blocks.sand),
                        new ItemStack(Blocks.sand) });
        netherrack = ThaumcraftApi.addArcaneCraftingRecipe("STONE_MANUFACTURE",
                new ItemStack(Blocks.netherrack, 8),
                (new AspectList())
                        .add(Aspect.FIRE, 1)
                        .add(Aspect.EARTH, 6),
                new Object[] { "CCC", "CNC", "CCC",
                        'C', Item.getItemFromBlock(Blocks.cobblestone),
                        'N', Items.nether_wart });
        soulSand = ThaumcraftApi.addArcaneCraftingRecipe("STONE_MANUFACTURE",
                new ItemStack(Blocks.soul_sand, 8),
                (new AspectList())
                        .add(Aspect.ENTROPY, 2)
                        .add(Aspect.WATER, 2)
                        .add(Aspect.AIR, 2),
                new Object[] { "SSS", "SNS", "SSS",
                        'S', Item.getItemFromBlock(Blocks.sand),
                        'N', Items.nether_wart });
        endStone = ThaumcraftApi.addArcaneCraftingRecipe("STONE_MANUFACTURE",
                new ItemStack(Blocks.end_stone, 8),
                (new AspectList())
                        .add(Aspect.AIR, 2)
                        .add(Aspect.FIRE, 2)
                        .add(Aspect.ENTROPY, 2),
                new Object[] { "CCC", "CEC", "CCC",
                        'C', Item.getItemFromBlock(Blocks.cobblestone),
                        'E', Items.ender_pearl });
    }

    private static void recipePlantHyperplasia() {
        shimmerleaf = ThaumcraftApi.addInfusionCraftingRecipe("MAGIC_PLANT",
                new ItemStack(BlocksTC.shimmerleaf), 1,
                (new AspectList())
                        .add(Aspect.PLANT, 10)
                        .add(Aspect.ENERGY, 8)
                        .add(Aspect.AURA, 2),
                new ItemStack(ItemsTC.quicksilver),
                new Object[] {
                        new ItemStack(ItemsTC.tallow),
                        new ItemStack(ItemsTC.wandRods, 1, 0) });
        cinderpearl = ThaumcraftApi.addInfusionCraftingRecipe("MAGIC_PLANT",
                new ItemStack(BlocksTC.cinderpearl), 1,
                (new AspectList())
                        .add(Aspect.PLANT, 10)
                        .add(Aspect.FIRE, 8)
                        .add(Aspect.AURA, 2),
                new ItemStack(Items.blaze_powder),
                new Object[] {
                        new ItemStack(ItemsTC.tallow),
                        new ItemStack(ItemsTC.wandRods, 1, 0) });
        vishroom = ThaumcraftApi.addInfusionCraftingRecipe("MAGIC_PLANT",
                new ItemStack(BlocksTC.vishroom), 1,
                (new AspectList())
                        .add(Aspect.PLANT, 10)
                        .add(Aspect.DEATH, 4)
                        .add(Aspect.ENTROPY, 4)
                        .add(Aspect.AURA, 2),
                new ItemStack(Items.poisonous_potato),
                new Object[] {
                        new ItemStack(Items.dye, 1, 13),
                        new ItemStack(ItemsTC.wandRods, 1, 0) });
    }
}
