package com.pickleaf.thaumic_fifimize;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin.MCVersion;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin.TransformerExclusions;
import java.util.Map;
import java.util.Vector;

import com.pickleaf.thaumic_fifimize.core.EarlyConfigLoader;

@MCVersion("1.8.9")
@TransformerExclusions({ "com.pickleaf.thaumic_fifimize." })
public class ThaumicFifimizeCoreMod implements IFMLLoadingPlugin {

    @Override
    public String[] getASMTransformerClass() {
        Vector<String> classes = new Vector<String>(10);
        EarlyConfigLoader.loadConfigEarly();
        classes.add("com.pickleaf.thaumic_fifimize.core.SilverLeavesDropChanceTransformer");
        if (Config.FIX_SEALS_INTERACT_DOUBLE_CHEST)
            classes.add("com.pickleaf.thaumic_fifimize.core.InventoryUtilsTransformer");
        return (String[])classes.toArray(new String[classes.size()]);
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