package com.pickleaf.thaumic_fifimize.common;

import com.pickleaf.thaumic_fifimize.Config;
import net.minecraft.item.ItemStack;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.research.ResearchItem;
import thaumcraft.api.research.ResearchPage;

public class ResearchsTF {
    public static void init() {
        if (Config.ITEM_SEAL_COPIER)
            (new ResearchItem("SEAL_COPIER", "GOLEMANCY",
                    new AspectList()
                            .add(Aspect.MIND, 3)
                            .add(Aspect.MECHANISM, 3),
                    -1, 6, 1,
                    new Object[] {
                            new ItemStack(ItemsTF.sealCopier) }))
                    .setPages(
                            new ResearchPage[] {
                                    new ResearchPage("thaumic_fifimize.research_page.SEAL_COPIER.0"),
                                    new ResearchPage(RecipesTF.sealCopier) })
                    .setParents(
                            new String[] { "CONTROLSEALS", "INFUSION" })
                    .setSecondary().registerResearchItem();
        if (Config.PRIMORDIAL_PEARL_DUP)
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
                                    new ResearchPage("thaumic_fifimize.research_page.PRIMORDIAL_PEARL_DUP.0"),
                                    new ResearchPage(RecipesTF.primordialMote),
                                    new ResearchPage(RecipesTF.primordialPearl) })
                    .setParents(
                            new String[] { "PRIMPEARL" })
                    .registerResearchItem();
    }

}
