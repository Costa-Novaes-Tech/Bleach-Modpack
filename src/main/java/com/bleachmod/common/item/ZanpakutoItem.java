package com.bleachmod.common.item;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tiers;

/** Identity weapons retain their combat tier but never wear out, including legacy damaged stacks. */
public final class ZanpakutoItem extends SwordItem {
    public ZanpakutoItem() { super(Tiers.IRON, 3, -2.4F, new Properties().stacksTo(1)); }
    @Override public int getMaxDamage(ItemStack stack) { return 0; }
    @Override public boolean isDamageable(ItemStack stack) { return false; }
    @Override public boolean isBarVisible(ItemStack stack) { return false; }
    @Override public boolean isValidRepairItem(ItemStack stack, ItemStack ingredient) { return false; }
}
