package com.pickleaf.thaumic_fifimize.common;

import com.pickleaf.thaumic_fifimize.common.seal.*;
import com.pickleaf.thaumic_fifimize.Config;

import thaumcraft.api.golems.GolemHelper;
import thaumcraft.api.golems.seals.ISeal;
import thaumcraft.common.entities.construct.golem.seals.SealHandler;

public class SealsTF {
    public static ISeal sealArcaneCraft;
    public static ISeal sealRequest;

    public static void init() {
        if (Config.SEAL_ARCANE_CRAFT) {
            sealArcaneCraft = (ISeal) new SealArcaneCraft();
            GolemHelper.registerSeal(sealArcaneCraft);
        }
        if (Config.SEAL_REQUEST) {
            sealRequest = (ISeal) new SealFillAdvancedRequest();
            SealHandler.types.replace(sealRequest.getKey(), sealRequest);
        }
    }
}
