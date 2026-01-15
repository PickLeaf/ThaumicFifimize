package com.pickleaf.thaumic_fifimize.common;

import com.pickleaf.thaumic_fifimize.Config;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.blocks.BlocksTC;
import thaumcraft.api.golems.GolemHelper;
import thaumcraft.api.items.ItemsTC;
import thaumcraft.api.research.ResearchItem;
import thaumcraft.api.research.ResearchPage;
import thaumcraft.api.research.ScanBlock;
import thaumcraft.api.research.ScanItem;
import thaumcraft.api.research.ScanningManager;

public class ResearchsTF {
    public static void init() {
        if (Config.ITEM_SEAL_COPIER) {
            (new ResearchItem("SEAL_COPIER", "GOLEMANCY",
                    new AspectList()
                            .add(Aspect.MIND, 3)
                            .add(Aspect.MECHANISM, 3),
                    -1, 6, 1,
                    new Object[] {
                            new ItemStack(ItemsTF.sealCopier) }))
                    .setPages(
                            new ResearchPage[] {
                                    new ResearchPage(
                                            "thaumic_fifimize.research_page.SEAL_COPIER.0"),
                                    new ResearchPage(RecipesTF.sealCopier) })
                    .setParents(
                            new String[] { "CONTROLSEALS", "INFUSION" })
                    .setSecondary().registerResearchItem();
        }
        if (Config.PRIMORDIAL_PEARL_DUP) {
            (new ResearchItem("PRIMORDIAL_PEARL_DUP", "ELDRITCH",
                    new AspectList()
                            .add(Aspect.AIR, 3)
                            .add(Aspect.EARTH, 3)
                            .add(Aspect.FIRE, 3)
                            .add(Aspect.WATER, 3)
                            .add(Aspect.ORDER, 3)
                            .add(Aspect.ENTROPY, 3),
                    3, 5, 2,
                    new Object[] {
                            new ItemStack(ItemsTF.primordialMote) }))
                    .setPages(
                            new ResearchPage[] {
                                    new ResearchPage(
                                            "thaumic_fifimize.research_page.PRIMORDIAL_PEARL_DUP.0"),
                                    new ResearchPage(RecipesTF.primordialMote),
                                    new ResearchPage(RecipesTF.primordialPearl) })
                    .setParents(
                            new String[] { "PRIMPEARL" })
                    .registerResearchItem();
        }
        if (Config.SEAL_ARCANE_CRAFT) {
            (new ResearchItem("SEAL_ARCANE_CRAFT", "GOLEMANCY",
                    new AspectList()
                            .add(Aspect.DESIRE, 8)
                            .add(Aspect.CRAFT, 6)
                            .add(Aspect.SENSES, 4)
                            .add(Aspect.WATER, 4)
                            .add(Aspect.ORDER, 4),
                    -4, 8, 1,
                    new Object[] {
                            GolemHelper.getSealStack(SealsTF.sealArcaneCraft.getKey()) }))
                    .setPages(
                            new ResearchPage[] {
                                    new ResearchPage(
                                            "thaumic_fifimize.research_page.SEAL_ARCANE_CRAFT.0"),
                                    new ResearchPage(RecipesTF.sealArcaneCraft) })
                    .setParents(
                            new String[] { "SEALUSE", "PRIMPEARL" })
                    .registerResearchItem();
        }
        if (Config.STONE_DUP) {
            (new ResearchItem("STONE_DUP", "ALCHEMY",
                    (new AspectList()).add(Aspect.EARTH, 10),
                    -3, 3, 1,
                    new Object[] {
                            new ItemStack(Blocks.stone, 1, 1),
                            new ItemStack(Blocks.stone, 1, 3),
                            new ItemStack(Blocks.stone, 1, 5) }))
                    .setPages(new ResearchPage[] {
                            new ResearchPage("thaumic_fifimize.research_page.STONE_DUP.0"),
                            new ResearchPage(RecipesTF.stoneDuplication_1),
                            new ResearchPage(RecipesTF.stoneDuplication_2),
                            new ResearchPage(RecipesTF.stoneDuplication_3) })
                    .setParents(new String[] { "ALCHEMICALDUPLICATION" }).setSecondary().registerResearchItem();
        }
        if (Config.STONE_MANUFACTURE) {
            (new ResearchItem("STONE_MANUFACTURE", "ALCHEMY",
                    (new AspectList())
                            .add(Aspect.ENTROPY, 10)
                            .add(Aspect.FIRE, 4)
                            .add(Aspect.SOUL, 4)
                            .add(Aspect.DARKNESS, 4),
                    -2, -2, 1,
                    new Object[] {
                            new ItemStack(Blocks.gravel),
                            new ItemStack(Blocks.sand),
                            new ItemStack(Blocks.netherrack),
                            new ItemStack(Blocks.soul_sand),
                            new ItemStack(Blocks.end_stone), }))
                    .setPages(new ResearchPage[] {
                            new ResearchPage("thaumic_fifimize.research_page.STONE_MANUFACTURE.0"),
                            new ResearchPage(RecipesTF.gravel),
                            new ResearchPage(RecipesTF.sand),
                            new ResearchPage(RecipesTF.netherrack),
                            new ResearchPage(RecipesTF.soulSand),
                            new ResearchPage(RecipesTF.endStone) })
                    .setParents(new String[] { "ALCHEMICALMANUFACTURE" }).registerResearchItem();
        }
        if (Config.MAGIC_PLANT) {
            ScanningManager.addScannableThing(new ScanItem("!SHIMMERLEAF", new ItemStack(BlocksTC.shimmerleaf)));
            ScanningManager.addScannableThing(new ScanBlock("!SHIMMERLEAF", BlocksTC.shimmerleaf));
            ScanningManager.addScannableThing(new ScanItem("!CINDERPEARL", new ItemStack(BlocksTC.cinderpearl)));
            ScanningManager.addScannableThing(new ScanBlock("!CINDERPEARL", BlocksTC.cinderpearl));
            ScanningManager.addScannableThing(new ScanItem("!VISHROOM", new ItemStack(BlocksTC.vishroom)));
            ScanningManager.addScannableThing(new ScanBlock("!VISHROOM", BlocksTC.vishroom));
            (new ResearchItem("MAGIC_PLANT", "ALCHEMY",
                    (new AspectList())
                            .add(Aspect.PLANT, 10)
                            .add(Aspect.AURA, 4),
                    -7, -4, 1,
                    new Object[] {
                            new ItemStack(BlocksTC.shimmerleaf),
                            new ItemStack(BlocksTC.cinderpearl),
                            new ItemStack(BlocksTC.vishroom) }))
                    .setPages(new ResearchPage[] {
                            new ResearchPage("thaumic_fifimize.research_page.MAGIC_PLANT.0"),
                            new ResearchPage(RecipesTF.shimmerleaf),
                            new ResearchPage(RecipesTF.cinderpearl),
                            new ResearchPage(RecipesTF.vishroom) })
                    .setParents(new String[] { "ETHEREALBLOOM", "ROD_greatwood", "!SHIMMERLEAF", "!CINDERPEARL",
                            "!VISHROOM" })
                    .setSecondary().registerResearchItem();
        }
    }
}
