package com.pickleaf.thaumic_fifimize.common;

import net.minecraft.item.ItemStack;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.research.ResearchItem;
import thaumcraft.api.research.ResearchPage;

public class ResearchsTF {
    public static void init() {
        (new ResearchItem("SEAL_COPIER", "GOLEMANCY",
                new AspectList().add(Aspect.MIND, 3).add(Aspect.MECHANISM, 3),
                -1, 6, 1,
                new Object[] { new ItemStack(ItemsTF.sealCopier) }))
                .setPages(new ResearchPage[] {
                        new ResearchPage("thaumic_fifimize.research_page.SEAL_COPIER.0"),
                        new ResearchPage(RecipesTF.sealCopier) })
                .setParents(new String[] { "CONTROLSEALS", "INFUSION" })
                .setSecondary().registerResearchItem();
    }

}
