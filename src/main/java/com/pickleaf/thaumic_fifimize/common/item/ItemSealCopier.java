/**
 *  Thaumic Augmentation
 *  Copyright (c) 2019 TheCodex6824.
 *
 *  This file is part of Thaumic Augmentation.
 *
 *  Thaumic Augmentation is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Thaumic Augmentation is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with Thaumic Augmentation.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.pickleaf.thaumic_fifimize.common.item;

import java.util.List;

import com.pickleaf.thaumic_fifimize.common.IHasName;
import com.pickleaf.thaumic_fifimize.common.ItemsTF;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants.NBT;
import thaumcraft.api.golems.GolemHelper;
import thaumcraft.api.golems.ISealDisplayer;
import thaumcraft.api.golems.seals.ISealEntity;
import thaumcraft.api.golems.seals.SealPos;
import thaumcraft.common.entities.construct.golem.ItemGolemBell;
import thaumcraft.common.entities.construct.golem.seals.SealEntity;
import thaumcraft.common.entities.construct.golem.seals.SealHandler;

public class ItemSealCopier extends Item implements IHasName, ISealDisplayer {

    public ItemSealCopier() {
        super();
        setMaxStackSize(1);
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
        if (stack.hasTagCompound() && stack.getTagCompound().hasKey("sealType", NBT.TAG_STRING)) {
            tooltip.add(StatCollector.translateToLocalFormatted("thaumic_fifimize.text.stored_seal", GolemHelper.getSealStack(stack.getTagCompound().getString("sealType")).getDisplayName()));
        }
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
        if (player.isSneaking()) {
            player.getHeldItem().setTagCompound(null);
            world.playSoundEffect(player.posX, player.posY, player.posZ, "thaumcraft:scan", 0.5F, 1F);
            return new ItemStack(ItemsTF.sealCopier, 1, 0);
        } else
            return super.onItemRightClick(itemStack, world, player);
    }

    @Override
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side,
            float hitX, float hitY, float hitZ) {
        if (!world.isRemote) {
            ISealEntity seal = ItemGolemBell.getSeal(player);
            if (seal != null) {
                ItemStack held = player.getHeldItem();
                if (held.hasTagCompound() && held.getTagCompound().hasKey("seal", NBT.TAG_COMPOUND)) {
                    if (held.getTagCompound().getString("sealType").equals(seal.getSeal().getKey()) && (!seal.isLocked()
                            || seal.getOwner().isEmpty() || seal.getOwner().equals(player.getUniqueID().toString()))) {

                        world.playSoundEffect(player.posX, player.posY, player.posZ, "random.orb", 0.6F,
                                1.0F + world.rand.nextFloat() * 0.1F);
                        SealPos oldPos = seal.getSealPos();
                        SealHandler.removeSealEntity(world, oldPos, true);
                        seal.readNBT(held.getTagCompound().getCompoundTag("seal"));
                        seal.getSealPos().face = oldPos.face;
                        seal.getSealPos().pos = oldPos.pos;
                        SealHandler.addSealEntity(world, (SealEntity) seal);
                        player.swingItem();
                        return true;
                    }
                } else if (!seal.isLocked() || seal.getOwner().isEmpty()
                        || seal.getOwner().equals(player.getUniqueID().toString())) {
                    world.playSoundEffect(player.posX, player.posY, player.posZ, "thaumcraft:scan", 0.5F, 1F);
                    NBTTagCompound newCompound = new NBTTagCompound();
                    newCompound.setTag("seal", seal.writeNBT());
                    newCompound.setString("sealType", seal.getSeal().getKey());
                    held.setTagCompound(newCompound);
                    held.setItemDamage(1);
                    player.swingItem();
                    return true;
                }
            }
        }
        return super.onItemUseFirst(stack, player, world, pos, side, hitX, hitY, hitZ);
    }

    @Override
    public String getName() {
        return "seal_copier";
    }
}
