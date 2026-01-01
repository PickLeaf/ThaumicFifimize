package com.pickleaf.thaumic_fifimize;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin.MCVersion;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin.TransformerExclusions;
import java.util.Map;

import com.pickleaf.thaumic_fifimize.core.EarlyConfigLoader;

@MCVersion("1.8.9")
@TransformerExclusions({"com.pickleaf.thaumic_fifimize."})
public class ThaumicFifimizeCoreMod implements IFMLLoadingPlugin {
    
    @Override
    public String[] getASMTransformerClass() {
        EarlyConfigLoader.loadConfigEarly();
        return new String[] { "com.pickleaf.thaumic_fifimize.core.SilverLeavesDropChanceTransformer" };
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