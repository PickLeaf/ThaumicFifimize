package com.pickleaf.thaumic_fifimize;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin.MCVersion;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin.TransformerExclusions;
import java.util.Map;

@MCVersion("1.8.9")
@TransformerExclusions({"com.pickleaf.thaumic_fifimize."})
public class ThaumicFifimizeCoreMod implements IFMLLoadingPlugin {
    
    @Override
    public String[] getASMTransformerClass() {
        return new String[] { "com.pickleaf.thaumic_fifimize.SilverLeavesDropChanceTransformer" };
    }
    
    @Override
    public String getModContainerClass() {
        return null;
    }
    
    @Override
    public String getSetupClass() {
        return null;
    }
    
    @Override
    public void injectData(Map<String, Object> data) {
    }
    
    @Override
    public String getAccessTransformerClass() {
        return null;
    }
}