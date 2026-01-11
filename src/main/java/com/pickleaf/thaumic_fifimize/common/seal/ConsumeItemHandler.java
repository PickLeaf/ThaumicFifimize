package com.pickleaf.thaumic_fifimize.common.seal;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class ConsumeItemHandler {
        public CountItemList itemCountMap;
        public CountItemList itemCountMapDecrased;
        public IInventory[] invs;

        public ConsumeItemHandler() {
        }

        public void consume() {
            for (IInventory inv : invs) {
                if (inv == null)
                    break;
                for (int a = 0; a < inv.getSizeInventory(); a++) {
                    // 别动这个循环！！！
                    ItemStack stack = inv.getStackInSlot(a);
                    if (stack == null)
                        continue;
                    if (!itemCountMap.contains(stack))
                        continue;
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
        }
    }