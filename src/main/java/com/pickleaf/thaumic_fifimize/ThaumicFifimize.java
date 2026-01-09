package com.pickleaf.thaumic_fifimize;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import com.pickleaf.thaumic_fifimize.common.CommonProxy;

@Mod(modid = ThaumicFifimize.MODID, version = ThaumicFifimize.VERSION, name = ThaumicFifimize.NAME, dependencies = "required-after:Thaumcraft@[5.2,);after:Automagy", acceptedMinecraftVersions = "1.8.9")
public class ThaumicFifimize {
    public static final String MODID = "thaumic_fifimize";
    public static final String VERSION = "0.4";
    public static final String NAME = "Thaumic Fifimize";

    @Instance(ThaumicFifimize.MODID)
    public static ThaumicFifimize instance;

    @SidedProxy(clientSide = "com.pickleaf.thaumic_fifimize.client.ClientProxy", serverSide = "com.pickleaf.thaumic_fifimize.common.CommonProxy")
    public static CommonProxy proxy;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        proxy.preInit(event);
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }
}
