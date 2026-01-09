package com.pickleaf.thaumic_fifimize.common.seal;

import net.minecraft.item.ItemStack;

public class CountItemList {
    public ItemStack[] items;
    public int[] counts;

    public CountItemList() {
        this.items = new ItemStack[9];
        this.counts = new int[9];
    }

    public CountItemList(CountItemList other) {
        this.items = new ItemStack[9];
        this.counts = new int[9];
        for (int i = 0; i < 9; i++) {
            if (other.items[i] == null)
                continue;
            this.items[i] = other.items[i].copy();
            this.counts[i] = other.counts[i];
        }
    }

    public static boolean equal(ItemStack stackA, ItemStack stackB) {
        if (stackA == stackB)
            return true;
        if (stackA == null ||
                stackB == null)
            return false;
        return stackA.getItem() != stackB.getItem() ? false
                : (stackA.getItemDamage() != stackB.getItemDamage() ? false
                        : (stackA.getTagCompound() == null && stackB.getTagCompound() != null ? false
                                : stackA.getTagCompound() == null
                                        || stackA.getTagCompound().equals(stackB.getTagCompound())));
    }

    public void merge(ItemStack stackVar, int count) {
        for (int i = 0; i < 9; i++) {
            if (items[i] == null) {
                items[i] = stackVar.copy();
                counts[i] = count;
                break;
            }
            if (equal(items[i], stackVar)) {
                counts[i] += count;
                break;
            }
        }
    }

    public boolean contains(ItemStack stackVar) {
        for (ItemStack stack : items) {
            if (equal(stack, stackVar)) {
                return true;
            }
        }
        return false;
    }

    public int get(ItemStack stackVar) {
        for (int i = 0; i < 9; i++) {
            if (equal(items[i], stackVar)) {
                return counts[i];
            }
        }
        return Integer.MIN_VALUE;
    }

    public void remove(ItemStack stackVar) {
        for (int i = 0; i < 9; i++) {
            if (equal(items[i], stackVar)) {
                items[i] = null;
                for (int j = i; j < 8; j++) {
                    items[i] = items[i + 1];
                }
                return;
            }
        }
    }

    public void replace(ItemStack stackVar, int count) {
        for (ItemStack stack : items) {
            if (stack == null) {
                break;
            }
            if (equal(stack, stackVar)) {
                stack.stackSize = count;
            }
        }
    }

    public ItemStack[] itemSet() {
        return this.items;
    }

    public void print() {
        System.out.println("[Thaumic Fifimize] ");
        for (int i = 0; i < 9; i++) {
            if (items[i] == null)
                return;
            System.out.println(items[i].getDisplayName() + " " + counts[i]);
        }
    }
}
