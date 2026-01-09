package com.pickleaf.thaumic_fifimize.common.seal;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import java.util.UUID;

import com.mojang.authlib.GameProfile;
import com.pickleaf.thaumic_fifimize.ThaumicFifimize;
import com.pickleaf.thaumic_fifimize.common.IHasName;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.EnumPacketDirection;
import net.minecraft.network.NetworkManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.crafting.IArcaneRecipe;
import thaumcraft.api.golems.EnumGolemTrait;
import thaumcraft.api.golems.IGolemAPI;
import thaumcraft.api.golems.seals.ISealEntity;
import thaumcraft.api.golems.tasks.Task;
import thaumcraft.api.wands.IWand;
import thaumcraft.common.entities.construct.golem.seals.SealFiltered;
import thaumcraft.common.entities.construct.golem.tasks.TaskHandler;
import thaumcraft.common.lib.network.FakeNetHandlerPlayServer;
import thaumcraft.common.entities.construct.EntityOwnedConstruct;
import thaumcraft.common.tiles.crafting.TileArcaneWorkbench;

public class SealArcaneCraft extends SealFiltered implements IHasName {
   int delay = (new Random(System.nanoTime())).nextInt(30);
   int cache = -1;
   IArcaneRecipe cacheRecipe = null;
   InventoryCrafting cacheInv = null;
   FakePlayer fakePlayer = null;
   ResourceLocation icon = new ResourceLocation(ThaumicFifimize.MODID, "items/seal_arcane_craft");

   public SealArcaneCraft() {
   }

   public String getName() {
      return "arcane_craft";
   }

   public String getKey() {
      return ThaumicFifimize.MODID + ":" + getName();
   }

   public void updateCache(World world, ISealEntity seal) {
      TileEntity te = world.getTileEntity(seal.getSealPos().pos);
      if (te != null && te instanceof TileArcaneWorkbench) {
         cacheInv = (InventoryCrafting) ((TileArcaneWorkbench) te).inventory;
      }
      @SuppressWarnings("rawtypes")
      Iterator recipes = ThaumcraftApi.getCraftingRecipes().iterator();

      while (recipes.hasNext()) {
         Object var = recipes.next();
         if (var instanceof IArcaneRecipe &&
               ((IArcaneRecipe) var).matches(cacheInv, world, null)) {
            cacheRecipe = (IArcaneRecipe) var;
         }
      }
   }

   public IInventory[] getInv(World world, BlockPos pos) {
      IInventory[] invs = new IInventory[6];
      int index = 0;
      for (EnumFacing face : EnumFacing.VALUES) {
         if (face == EnumFacing.UP)
            continue;
         TileEntity te = world.getTileEntity(pos.offset(face));
         if (te != null && te instanceof IInventory) {
            invs[index] = (IInventory) te;
            index++;
         }
      }
      return invs;
   }

   public void print(String s) {
      System.out.println("[Thaumic Fifimize] " + s);
   }

   public void tickSeal(World world, ISealEntity seal) {
      if (cacheInv == null ||
            cacheRecipe == null) {
         updateCache(world, seal);
      } else if (this.delay % 200 == 0) {
         updateCache(world, seal);
      }

      if (this.delay++ % 20 == 0 &&
            cacheRecipe != null &&
            cacheInv != null) {
         Task task = TaskHandler.getTask(world.provider.getDimensionId(), cache);
         if (task == null || task.isReserved() || task.isSuspended() || task.isCompleted()) {
            task = new Task(seal.getSealPos(), seal.getSealPos().pos);
            task.setPriority(seal.getPriority());
            cache = task.getId();
            TaskHandler.addTask(world.provider.getDimensionId(), task);
         }
      }
   }

   public void onTaskStarted(World world, IGolemAPI golem, Task task) {
   }

   private Comparator<ItemStack> itemStackComparator = (item1, item2) -> {
      ItemStack item3 = item2.copy();
      item3.stackSize = item1.stackSize;
      if (ItemStack.areItemStacksEqual(item1, item3)) {
         return 0;
      }
      return item1.stackSize - item2.stackSize;
   };

   public boolean onTaskCompletion(World world, IGolemAPI golem, Task task) {
      // 获得配方所需的物品数量
      CountItemList itemCountMap = new CountItemList();
      for (int i = 0; i < cacheInv.getSizeInventory(); i++) {
         ItemStack stack = cacheInv.getStackInSlot(i);
         if (stack != null) {
            print(stack.getItem().getUnlocalizedName());
            int count = stack.stackSize;
            itemCountMap.merge(stack, count);
         }
      }
      itemCountMap.print();
      // 从周围的箱子中获得将要被消耗物品
      print("1");
      IInventory[] invs = getInv(world, task.getPos());
      print("1.01");
      CountItemList itemCountMapDecrased = new CountItemList(itemCountMap);
      print("1.02");
      for (IInventory inv : invs) {
         if (inv == null)
            continue;
         for (int a = 0; a < inv.getSizeInventory(); a++) {
            ItemStack stack = inv.getStackInSlot(a);
            if (stack == null)
               continue;
            print("1.11");
            if (itemCountMapDecrased.contains(stack)) {
               print("1.12");
               if (itemCountMapDecrased.get(stack) < 0)
                  break;
               print("1.13");
               int count = stack.stackSize;
               itemCountMapDecrased.merge(stack, -count);
            }
         }
      }
      print("1.2");
      // 检查物品是否足够
      itemCountMapDecrased.print();
      {
         ItemStack[] items = itemCountMapDecrased.items;
         int[] counts = itemCountMapDecrased.counts;
         for (int i = 0; i < 9; i++) {
            if (items[i] == null)
               continue;
            if (counts[i] > 0) {
               task.setSuspended(true);
               return false;
            }
            counts[i] = 0;
         }
      }
      print("1.25");
      // 获得奥术工作台TileEntity
      TileEntity te = world.getTileEntity(task.getPos());
      if (te == null || !(te instanceof TileArcaneWorkbench)) {
         print("1.27");
         task.setSuspended(true);
         return false;
      }
      TileArcaneWorkbench teAW = (TileArcaneWorkbench) te;
      print("1.3");
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
      // 检测法杖魔力

      print("2");
      if (fakePlayer == null) {
         this.fakePlayer = FakePlayerFactory.get((WorldServer) world,
               new GameProfile((UUID) null, "FakeThaumcraftGolem"));
         this.fakePlayer.playerNetServerHandler = new FakeNetHandlerPlayServer(this.fakePlayer.mcServer,
               new NetworkManager(EnumPacketDirection.CLIENTBOUND), this.fakePlayer);
      }
      EntityPlayer player = (EntityPlayer) ((EntityOwnedConstruct) golem).getOwnerEntity();
      if (player == null) {
         player = fakePlayer;
      }
      if (wand.consumeAllVis(wadnStack, player, cacheRecipe.getAspects(), false, true)) {
         // 傀儡获得配方输出
         ItemStack result = cacheRecipe.getCraftingResult(cacheInv);
         if (result != null) {
            golem.holdItem(result.copy());
            golem.swingArm();

            world.playSoundAtEntity((Entity) golem, "random.pop", 0.125F,
                  ((world.rand.nextFloat() - world.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
         } else {
            task.setSuspended(true);
            return false;
         }
         // 消耗物品
         print("3");
         for (IInventory inv : invs) {
            if (inv == null)
               break;
            for (int a = 0; a < inv.getSizeInventory(); a++) {
               print("3.1");
               ItemStack stack = inv.getStackInSlot(a);
               if (stack == null)
                  continue;
               print("3.2");
               if (!itemCountMap.contains(stack))
                  continue;
               print("3.3");
               if (stack.stackSize < itemCountMap.get(stack)
                     - itemCountMapDecrased.get(stack)) {
                  itemCountMapDecrased.merge(stack, stack.stackSize);
                  inv.setInventorySlotContents(a, null);
               } else {
                  stack.stackSize = stack.stackSize -
                        (itemCountMap.get(stack) - itemCountMapDecrased.get(stack));
                  if (stack.stackSize <= 0) {
                     inv.setInventorySlotContents(a, null);
                  }
                  itemCountMap.remove(stack);
               }
            }
         }
         print("4");
         // 消耗法杖魔力
         wand.consumeAllVis(wadnStack, player, cacheRecipe.getAspects(), true, true);
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
}
