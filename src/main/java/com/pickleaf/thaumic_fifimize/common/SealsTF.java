package com.pickleaf.thaumic_fifimize.common;

import com.pickleaf.thaumic_fifimize.common.seal.*;
import com.pickleaf.thaumic_fifimize.Config;

import thaumcraft.api.golems.GolemHelper;
import thaumcraft.api.golems.seals.ISeal;

public class SealsTF {
    public static ISeal sealArcaneCraft;

    public static void init() {
        if (Config.SEAL_ARCANE_CRAFT) {
            sealArcaneCraft = (ISeal) new SealArcaneCraft();
            GolemHelper.registerSeal(sealArcaneCraft);
        }
    }
}
