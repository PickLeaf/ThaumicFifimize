package com.pickleaf.thaumic_fifimize.common.seal;

import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import com.mojang.authlib.GameProfile;
import com.pickleaf.thaumic_fifimize.ThaumicFifimize;
import com.pickleaf.thaumic_fifimize.common.IHasName;

import net.minecraft.block.BlockChest;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IEntityOwnable;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.crafting.IArcaneRecipe;
import thaumcraft.api.golems.EnumGolemTrait;
import thaumcraft.api.golems.IGolemAPI;
import thaumcraft.api.golems.seals.ISeal;
import thaumcraft.api.golems.seals.ISealEntity;
import thaumcraft.api.golems.tasks.Task;
import thaumcraft.api.wands.IWand;
import thaumcraft.common.entities.construct.golem.gui.SealBaseContainer;
import thaumcraft.common.entities.construct.golem.gui.SealBaseGUI;
import thaumcraft.common.entities.construct.golem.tasks.TaskHandler;
import thaumcraft.common.lib.research.ResearchManager;
import thaumcraft.common.tiles.crafting.TileArcaneWorkbench;

public class SealArcaneCraft implements ISeal, IHasName {
    private final int radius = 1;
    IArcaneRecipe cacheRecipe;
    int delay = (new Random(System.nanoTime())).nextInt(30);
    int cache = -1;

    ResourceLocation icon = new ResourceLocation(ThaumicFifimize.MODID, "items/seal_arcane_craft");

    public SealArcaneCraft() {
        @SuppressWarnings("rawtypes")
        Iterator recipes = ThaumcraftApi.getCraftingRecipes().iterator();
        while (recipes.hasNext()) {
            Object var = recipes.next();
            if (var instanceof IArcaneRecipe) {
                this.cacheRecipe = (IArcaneRecipe) var;
                break;
            }
        }
    }

    public String getName() {
        return "arcane_craft";
    }

    public String getKey() {
        return ThaumicFifimize.MODID + ":" + getName();
    }

    // private class FakeContainer extends Container {
    // @Override
    // public boolean canInteractWith(EntityPlayer playerIn) {
    // return false;
    // }
    // }

    private InventoryCrafting getWorkbenchInventory(World world, BlockPos pos) {
        TileEntity te = world.getTileEntity(pos);
        if (!(te instanceof TileArcaneWorkbench)) {
            return null;
        }
        return ((TileArcaneWorkbench) te).inventory;
    }

    private IArcaneRecipe getMatchingRecipe(World world, InventoryCrafting inv) {
        if (this.cacheRecipe.matches(inv, world, null)) {
            return this.cacheRecipe;
        }
        if (inv == null) {
            return null;
        }
        @SuppressWarnings("rawtypes")
        Iterator recipes = ThaumcraftApi.getCraftingRecipes().iterator();
        while (recipes.hasNext()) {
            Object var = recipes.next();
            if (var instanceof IArcaneRecipe &&
                    ((IArcaneRecipe) var).matches(inv, world, null)) {
                return (IArcaneRecipe) var;
            }
        }
        return null;
    }

    private String getOwnerPlayerUUID(IGolemAPI golem) {
        Entity entity = golem.getGolemEntity();
        if (entity instanceof IEntityOwnable) {
            return ((IEntityOwnable) entity).getOwnerId();
        }
        return null;
    }

    private IInventory[] getInv(World world, BlockPos pos) {
        IInventory[] invs = new IInventory[6];
        int index = 0;
        for (EnumFacing face : EnumFacing.VALUES) {
            if (face == EnumFacing.UP)
                continue;
            BlockPos offsetPos = pos.offset(face);
            TileEntity te = world.getTileEntity(offsetPos);
            if (te != null && te instanceof IInventory) {
                if (te instanceof TileEntityChest &&
                        (world.getBlockState(offsetPos).getBlock() instanceof BlockChest)) {
                    invs[index] = Blocks.chest.getLockableContainer(world, offsetPos);
                } else {
                    invs[index] = (IInventory) te;
                }
                index++;
            }
        }
        return invs;
    }

    public void tickSeal(World world, ISealEntity seal) {
        if (this.delay++ % 20 == 0) {
            Task task = TaskHandler.getTask(world.provider.getDimensionId(), cache);
            if (task == null || task.isReserved() || task.isSuspended() || task.isCompleted()) {
                BlockPos pos = seal.getSealPos().pos;
                InventoryCrafting workbenchInv = getWorkbenchInventory(world, pos);
                IArcaneRecipe recipe = getMatchingRecipe(world, workbenchInv);
                if (!checkStorageEnough(world, pos, workbenchInv, recipe, null)) {
                    return;
                }
                task = new Task(seal.getSealPos(), pos);
                task.setPriority(seal.getPriority());
                cache = task.getId();
                TaskHandler.addTask(world.provider.getDimensionId(), task);
            }
        }
    }

    private boolean checkStorageEnough(World world, BlockPos pos, InventoryCrafting invCraft,
            IArcaneRecipe cacheRecipe, ConsumeItemHandler hanlder) {
        if (invCraft == null ||
                cacheRecipe == null) {
            return false;
        }
        CountItemList itemCountMap = new CountItemList();
        for (int i = 0; i < invCraft.getSizeInventory(); i++) {
            ItemStack stack = invCraft.getStackInSlot(i);
            if (stack != null) {
                int count = stack.stackSize;
                itemCountMap.merge(stack, count);
            }
        }
        // 从周围的箱子中获得将要被消耗物品
        IInventory[] invs = getInv(world, pos);
        CountItemList itemCountMapDecrased = new CountItemList(itemCountMap);
        for (IInventory inv : invs) {
            if (inv == null)
                continue;
            for (int a = 0; a < inv.getSizeInventory(); a++) {
                ItemStack stack = inv.getStackInSlot(a);
                if (stack == null)
                    continue;
                if (itemCountMapDecrased.contains(stack)) {
                    if (itemCountMapDecrased.get(stack) < 0)
                        break;
                    int count = stack.stackSize;
                    itemCountMapDecrased.merge(stack, -count);
                }
            }
        }
        // 检查物品是否足够
        {
            ItemStack[] items = itemCountMapDecrased.items;
            int[] counts = itemCountMapDecrased.counts;
            for (int i = 0; i < 9; i++) {
                if (items[i] == null)
                    continue;
                if (counts[i] >= 0) {
                    return false;
                }
                counts[i] = 0;
            }
        }
        if (hanlder != null) {
            hanlder.itemCountMap = itemCountMap;
            hanlder.itemCountMapDecrased = itemCountMapDecrased;
            hanlder.invs = invs;
        }
        return true;
    }

    public void onTaskStarted(World world, IGolemAPI golem, Task task) {
    }

    public boolean onTaskCompletion(World world, IGolemAPI golem, Task task) {
        InventoryCrafting workbenchInv = getWorkbenchInventory(world, task.getPos());
        IArcaneRecipe recipe = getMatchingRecipe(world, workbenchInv);
        // 检查物品是否足够
        ConsumeItemHandler consumeHandler = new ConsumeItemHandler();
        if (!checkStorageEnough(world, task.getPos(), workbenchInv, recipe, consumeHandler)) {
            task.setSuspended(true);
            return false;
        }
        // 检查玩家是否完成配方所需研究
        GameProfile profile = MinecraftServer.getServer().getPlayerProfileCache()
                .getProfileByUUID(UUID.fromString(getOwnerPlayerUUID(golem)));
        if (profile == null
                || profile.getName() == null) {
            task.setSuspended(true);
            return false;
        }
        String playerName = profile.getName();
        String[] researchs = recipe.getResearch();
        for (String research : researchs) {
            if (research.isEmpty())
                continue;
            if (!ResearchManager.isResearchComplete(playerName, research)) {
                task.setSuspended(true);
                return false;
            }
        }
        // 获得奥术工作台TileEntity
        TileArcaneWorkbench teAW = (TileArcaneWorkbench) world.getTileEntity(task.getPos());
        // 获得法杖
        ItemStack wadnStack = teAW.inventory.getStackInSlot(10);
        if (wadnStack == null) {
            task.setSuspended(true);
            return false;
        }
        Item item = wadnStack.getItem();
        if (!(item instanceof IWand)) {
            task.setSuspended(true);
            return false;
        }
        IWand wand = (IWand) item;
        // 获得魔力消耗
        AspectList aspects = recipe.getAspects();
        if (aspects == null) {
            aspects = recipe.getAspects(workbenchInv);
            if (aspects == null) {
                task.setSuspended(true);
                return false;
            }
        }
        // 获得盔甲架装备并为本次合成创建临时假人玩家
        FakePlayer fakePlayer = FakePlayerFactory.get((WorldServer) world,
                new GameProfile(null, "FakeThaumcraftGolem"));
        {
            BlockPos pos = teAW.getPos();
            double x = pos.getX();
            double y = pos.getY();
            double z = pos.getZ();
            List<EntityArmorStand> armorStands = world.getEntitiesWithinAABB(
                    EntityArmorStand.class, AxisAlignedBB.fromBounds(
                            x - radius, y - radius, z - radius, x + radius + 1, y + radius + 1, z + radius + 1));
            for (EntityArmorStand armorStand : armorStands) {
                fakePlayer.setCurrentItemOrArmor(1, armorStand.getEquipmentInSlot(1));
                fakePlayer.setCurrentItemOrArmor(2, armorStand.getEquipmentInSlot(2));
                fakePlayer.setCurrentItemOrArmor(3, armorStand.getEquipmentInSlot(3));
                fakePlayer.setCurrentItemOrArmor(4, armorStand.getEquipmentInSlot(4));
                break;
            }
        }
        // 检测法杖魔力
        if (wand.consumeAllVis(wadnStack, fakePlayer, aspects, false, true)) {
            // 傀儡获得配方输出
            ItemStack result = recipe.getCraftingResult(workbenchInv);
            if (result == null) {
                task.setSuspended(true);
                return false;
            }
            golem.holdItem(result.copy());
            golem.swingArm();
            world.playSoundAtEntity((Entity) golem, "random.pop", 0.125F,
                    ((world.rand.nextFloat() - world.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
            // 消耗物品
            consumeHandler.consume();
            // 消耗法杖魔力
            wand.consumeAllVis(wadnStack, fakePlayer, aspects, true, true);
            return true;
        } else {
            task.setSuspended(true);
            return false;
        }
    }

    public boolean canGolemPerformTask(IGolemAPI golem, Task task) {
        ItemStack[] carrying = golem.getCarrying();
        for (int i = 0; i < carrying.length; ++i) {
            if (carrying[i] != null) {
                return false;
            }
        }
        return true;
    }

    public boolean canPlaceAt(World world, BlockPos pos, EnumFacing side) {
        TileEntity te = world.getTileEntity(pos);
        return te != null && te instanceof TileArcaneWorkbench;
    }

    public ResourceLocation getSealIcon() {
        return this.icon;
    }

    public int[] getGuiCategories() {
        return new int[] { 0, 4 };
    }

    public EnumGolemTrait[] getRequiredTags() {
        return new EnumGolemTrait[] { EnumGolemTrait.SMART, EnumGolemTrait.DEFT };
    }

    public EnumGolemTrait[] getForbiddenTags() {
        return new EnumGolemTrait[] { EnumGolemTrait.CLUMSY };
    }

    public void onTaskSuspension(World world, Task task) {
    }

    public void onRemoval(World world, BlockPos pos, EnumFacing side) {
    }

    @Override
    public void readCustomNBT(NBTTagCompound nbt) {
        this.cache = nbt.getInteger("cacheTaskID");
    }

    @Override
    public void writeCustomNBT(NBTTagCompound nbt) {
        nbt.setInteger("cacheTaskID", this.cache);
    }

    @Override
    public Object returnContainer(World world, EntityPlayer player, BlockPos pos, EnumFacing side, ISealEntity seal) {
        return new SealBaseContainer(player.inventory, world, seal);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Object returnGui(World world, EntityPlayer player, BlockPos pos, EnumFacing side, ISealEntity seal) {
        return new SealBaseGUI(player.inventory, world, seal);
    }
}
