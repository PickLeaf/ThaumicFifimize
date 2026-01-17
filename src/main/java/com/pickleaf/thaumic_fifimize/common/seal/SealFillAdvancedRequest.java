package com.pickleaf.thaumic_fifimize.common.seal;

import com.pickleaf.thaumic_fifimize.core.HookHandler;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import thaumcraft.api.golems.GolemHelper;
import thaumcraft.api.golems.IGolemAPI;
import thaumcraft.api.golems.seals.ISealConfigToggles;
import thaumcraft.api.golems.seals.ISealEntity;
import thaumcraft.api.golems.tasks.Task;
import thaumcraft.common.entities.construct.golem.seals.SealFillAdvanced;
import thaumcraft.common.entities.construct.golem.seals.SealHandler;
import thaumcraft.common.lib.utils.InventoryUtils;

public class SealFillAdvancedRequest extends SealFillAdvanced {
    public SealFillAdvancedRequest() {
        super();
        super.props = new ISealConfigToggles.SealToggle[] {
                new ISealConfigToggles.SealToggle(true, "pmeta", "golem.prop.meta"),
                new ISealConfigToggles.SealToggle(true, "pnbt", "golem.prop.nbt"),
                new ISealConfigToggles.SealToggle(false, "pore", "golem.prop.ore"),
                new ISealConfigToggles.SealToggle(false, "pmod", "golem.prop.mod"),
                new ISealConfigToggles.SealToggle(false, "pexist", "golem.prop.exist"),
                new ISealConfigToggles.SealToggle(false, "ppro", "golem.prop.provision.wl") };
    }

    @Override
    public boolean canGolemPerformTask(IGolemAPI golem, Task task) {
        if (golem.getCarrying()[0] == null
                && this.props[5].value
                && !this.isBlacklist()
                && this.getFilterSlot(0) != null) {
            TileEntity te = golem.getGolemWorld().getTileEntity(task.getPos());
            if (!(te instanceof IInventory))
                return false;
            IInventory inv = HookHandler.getDoubleChestInv((IInventory) te);
            EnumFacing side = task.getSealPos().face;
            boolean ignoreDamage = !this.props[0].value;
            boolean ignoreNBT = !this.props[1].value;
            boolean useOre = !this.props[2].value;
            boolean useMod = !this.props[3].value;
            ISealEntity sealEntity = SealHandler.getSealEntity(
                    golem.getGolemWorld().provider.getDimensionId(), task.getSealPos());
            for (ItemStack stack : this.getInv()) {
                if (stack == null)
                    continue;
                int counts = InventoryUtils.inventoryContainsAmount(
                        inv, stack, side, ignoreDamage, ignoreNBT, useOre, useMod);
                if (counts < stack.stackSize) {
                    if (!this.props[0].value){
                        stack.setItemDamage(32767);
                    }
                    GolemHelper.requestProvisioning(golem.getGolemWorld(), sealEntity, stack);
                }
            }
            return false;
        } else {
            return super.canGolemPerformTask(golem, task);
        }
    }

    @Override
    public void readCustomNBT(NBTTagCompound nbt) {
        super.readCustomNBT(nbt);
        NBTTagList nbttaglist = nbt.getTagList("Items", 10);

        for (int i = 0; i < nbttaglist.tagCount(); ++i) {
            NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
            byte b0 = nbttagcompound1.getByte("Slot");
            if (b0 >= 0 && b0 < this.getInv().length) {
                this.getInv()[b0].stackSize = nbttagcompound1.getInteger("integerCounts");
            }
        }
    }

    @Override
    public void writeCustomNBT(NBTTagCompound nbt) {
        super.writeCustomNBT(nbt);
        NBTTagList nbttaglist = nbt.getTagList("Items", 10);

        for (int i = 0; i < nbttaglist.tagCount(); ++i) {
            NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
            int j = nbttagcompound1.getByte("Slot");
            nbttagcompound1.setInteger("integerCounts", this.getInv()[j].stackSize);
        }
    }

    ResourceLocation icon = new ResourceLocation("thaumcraft", "items/seals/seal_fill_advanced");

    public String getKey() {
        return "Thaumcraft:fill_advanced";
    }

    public ResourceLocation getSealIcon() {
        return this.icon;
    }
}
