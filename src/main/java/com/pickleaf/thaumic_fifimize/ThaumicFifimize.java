package com.pickleaf.thaumic_fifimize;

import net.minecraft.init.Blocks;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.common.config.Configuration;

@Mod(modid = ThaumicFifimize.MODID, version = ThaumicFifimize.VERSION)
public class ThaumicFifimize
{
    public static final String MODID = "thaumic_fifimize";
    public static final String VERSION = "0.1";
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        Configuration cfg = new Configuration(event.getSuggestedConfigurationFile());
        Config.SILVERSAPLING_DROPCHANCE = cfg.getInt("leavesPerSilverSapling", Configuration.CATEGORY_GENERAL, 100 , 1, Integer.MAX_VALUE, "平均多少个树叶掉落一个银树树苗 Vanilla:200");
        if (cfg.hasChanged()) cfg.save();
    }
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
    }

    @EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
	}
}
