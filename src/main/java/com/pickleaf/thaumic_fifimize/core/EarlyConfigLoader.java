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
            if (!configFile.exists()) {
                configFile.createNewFile();
            }
            Configuration cfg = new Configuration(configFile);
            load(cfg);
            if (cfg.hasChanged())
                cfg.save();
        } catch (Exception e) {
            ThaumicFifimize.printErr("Failed to load early config: " + e.getMessage());
        }

    }

    private static void load(Configuration cfg) {
        // 需要修改类代码
        Config.SILVERSAPLING_DROPCHANCE = cfg.getInt("leavesPerSilverSapling", Configuration.CATEGORY_GENERAL, 100,
                1, Integer.MAX_VALUE, "平均多少个树叶掉落一个银树树苗 Vanilla:200");
        Config.FIX_SEALS_INTERACT_DOUBLE_CHEST = cfg.getBoolean("fixSealsInteractDoubleChest",
                Configuration.CATEGORY_GENERAL, true,
                "修复印记与大箱子交互时只作用于一半的漏洞");
        Config.SEAL_USE_CAN_ACTIVATE_INFUSION = cfg.getBoolean("enableSealUseActivateInfusion",
                Configuration.CATEGORY_GENERAL, true,
                "启用\"操控印记：使用\"可以激活注魔合成");
        // 配方
        Config.PRIMORDIAL_PEARL_DUP = cfg.getBoolean("primordialPearlDuplicatiopn", Config.CATEGORY_RECIPE, true,
                "启用元始珍珠复制配方");
        Config.STONE_DUP = cfg.getBoolean("stonesDuplicatiopn", Config.CATEGORY_RECIPE, true,
                "启用石头复制配方");
        Config.STONE_MANUFACTURE = cfg.getBoolean("stonesManufacture", Config.CATEGORY_RECIPE, true,
                "启用世界基质制造配方");
        Config.MAGIC_PLANT = cfg.getBoolean("magicPlants", Config.CATEGORY_RECIPE, true,
                "启用魔法植物(烈焰草,水银花)配方");
        // 物品
        Config.ITEM_SEAL_COPIER = cfg.getBoolean("ItemSealCopier", Config.CATEGORY_ITEM, true,
                "启用物品印记拓印器");
        Config.SEAL_ARCANE_CRAFT = cfg.getBoolean("SealArcaneCraft", Config.CATEGORY_ITEM, true,
                "启用物品奥数合成印记");
        Config.SEAL_REQUEST = cfg.getBoolean("SealRequest", Config.CATEGORY_ITEM, true,
                "为\"高级操控印记：填充\"添加根据白名单请求物品供应的能力");
    }
}