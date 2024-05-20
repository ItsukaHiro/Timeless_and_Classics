package com.tac.guns.enchantment;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;

/**
 * Author: Forked from MrCrayfish, continued by Timeless devs
 */
public class ReclaimedEnchantment extends GunEnchantment
{
    public ReclaimedEnchantment()
    {
        super(Rarity.VERY_RARE, EnchantmentTypes.GUN, new EquipmentSlot[]{EquipmentSlot.MAINHAND}, Type.AMMO);
    }

    @Override
    public int getMaxLevel()
    {
        return 2;
    }
    @Override
    public boolean canEnchant(ItemStack stack) {
        return true;
    }
    @Override
    public boolean isTreasureOnly() {
        return true;
    }
    @Override
    public int getMinCost(int level)
    {
        return 15 + (level - 1) * 10;
    }
    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack) {
        return false;
    }
    @Override
    public int getMaxCost(int level)
    {
        return this.getMinCost(level) + 10;
    }
}
