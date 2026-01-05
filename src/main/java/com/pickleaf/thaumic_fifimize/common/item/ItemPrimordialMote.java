package com.pickleaf.thaumic_fifimize.common.item;

import com.pickleaf.thaumic_fifimize.common.IHasName;
import net.minecraft.item.Item;
import thaumcraft.api.golems.ISealDisplayer;

public class ItemPrimordialMote extends Item implements IHasName, ISealDisplayer {

    public ItemPrimordialMote() {
        super();
    }
    @Override
    public String getName() {
        return "primordial_mote";
    }
}
