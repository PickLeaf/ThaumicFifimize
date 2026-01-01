package com.pickleaf.thaumic_fifimize.core;

import java.io.File;

import com.pickleaf.thaumic_fifimize.Config;
import com.pickleaf.thaumic_fifimize.ThaumicFifimize;

import net.minecraftforge.common.config.Configuration;

public class EarlyConfigLoader {
    public static void loadConfigEarly() {
        try {
            File configDir = new File("config");
            if (!configDir.exists()) {
                configDir.mkdirs();
            }
            File configFile = new File(configDir, ThaumicFifimize.MODID + ".cfg");
            if (!configFile.exists()){
                configFile.createNewFile();
            }

            Configuration cfg = new Configuration(configFile);
            Config.SILVERSAPLING_DROPCHANCE = cfg.getInt("leavesPerSilverSapling", Configuration.CATEGORY_GENERAL, 100,
                    1, Integer.MAX_VALUE, "平均多少个树叶掉落一个银树树苗 Vanilla:200");
            if (cfg.hasChanged())
                cfg.save();
        } catch (Exception e) {
            System.err.println("[ThaumicFifimize] Failed to load early config: " + e.getMessage());
        }

    }
}