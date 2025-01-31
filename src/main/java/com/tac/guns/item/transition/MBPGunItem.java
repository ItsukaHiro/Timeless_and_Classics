package com.tac.guns.item.transition;


import com.tac.guns.GunMod;
import com.tac.guns.common.Gun;
import com.tac.guns.item.GunItem;
import com.tac.guns.util.GunEnchantmentHelper;
import com.tac.guns.util.GunModifierHelper;
import com.tac.guns.util.Process;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.KeybindComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Locale;

public class MBPGunItem extends TimelessGunItem {
    public MBPGunItem(Process<Properties> properties)
    {
        super(properties1 -> properties.process(new Properties().stacksTo(1).tab(GunMod.GROUP)));
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flag) {
        Gun modifiedGun = this.getModifiedGun(stack);
        Item ammo = (Item) ForgeRegistries.ITEMS.getValue(modifiedGun.getProjectile().getItem());
        if (ammo != null) {
            tooltip.add((new TranslatableComponent("info.tac.ammo_type", new TranslatableComponent(ammo.getDescriptionId()).withStyle(ChatFormatting.GOLD)).withStyle(ChatFormatting.DARK_GRAY)));
        }

        String additionalDamageText = "";
        CompoundTag tagCompound = stack.getTag();
        float additionalDamage;
        if (tagCompound != null && tagCompound.contains("AdditionalDamage", 99)) {
            additionalDamage = tagCompound.getFloat("AdditionalDamage");
            additionalDamage += GunModifierHelper.getAdditionalDamage(stack);
            if (additionalDamage > 0.0F) {
                additionalDamageText = ChatFormatting.GREEN + " +" + ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format((double)additionalDamage);
            } else if (additionalDamage < 0.0F) {
                additionalDamageText = ChatFormatting.RED + " " + ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format((double)additionalDamage);
            }
        }

        additionalDamage = modifiedGun.getProjectile().getDamage();
        additionalDamage = GunModifierHelper.getModifiedProjectileDamage(stack, additionalDamage);
        additionalDamage = GunEnchantmentHelper.getAcceleratorDamage(stack, additionalDamage);
        tooltip.add((new TranslatableComponent("info.tac.damage", new Object[]{ChatFormatting.GOLD + ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format((double)additionalDamage) + additionalDamageText})).withStyle(ChatFormatting.DARK_GRAY));
        if (tagCompound != null) {
            if (tagCompound.getBoolean("IgnoreAmmo")) {
                tooltip.add((new TranslatableComponent("info.tac.ignore_ammo")).withStyle(ChatFormatting.AQUA));
            } else {
                int ammoCount = tagCompound.getInt("AmmoCount");
                tooltip.add((new TranslatableComponent("info.tac.ammo", new Object[]{ChatFormatting.GOLD.toString() + ammoCount + "/" + GunModifierHelper.getAmmoCapacity(stack, modifiedGun)})).withStyle(ChatFormatting.DARK_GRAY));
            }
        }

        if(tagCompound != null) {
            if (tagCompound.get("CurrentFireMode") == null) {
            } else if (tagCompound.getInt("CurrentFireMode") == 0)
                tooltip.add((new TranslatableComponent("info.tac.firemode_safe", new Object[]{(new KeybindComponent("key.tac.fireSelect")).getString().toUpperCase(Locale.ENGLISH)})).withStyle(ChatFormatting.GREEN));
            else if (tagCompound.getInt("CurrentFireMode") == 1)
                tooltip.add((new TranslatableComponent("info.tac.firemode_semi", new Object[]{(new KeybindComponent("key.tac.fireSelect")).getString().toUpperCase(Locale.ENGLISH)})).withStyle(ChatFormatting.RED));
            else if (tagCompound.getInt("CurrentFireMode") == 2)
                tooltip.add((new TranslatableComponent("info.tac.firemode_auto", new Object[]{(new KeybindComponent("key.tac.fireSelect")).getString().toUpperCase(Locale.ENGLISH)})).withStyle(ChatFormatting.RED));
        }

        if(tagCompound != null)
        {
            GunItem gun = (GunItem)stack.getItem();
            float speed = 0.1f / (1+((gun.getGun().getGeneral().getWeightKilo()*(1+GunModifierHelper.getModifierOfWeaponWeight(stack)) + GunModifierHelper.getAdditionalWeaponWeight(stack)) * 0.0275f));
            speed = Math.max(Math.min(speed, 0.095F), 0.075F);
            if(speed > 0.094f)
                tooltip.add((new TranslatableComponent("info.tac.lightWeightGun", new TranslatableComponent(-((int)((0.1 - speed)*1000))+"%").withStyle(ChatFormatting.RED)).withStyle(ChatFormatting.DARK_AQUA)));
            else if(speed < 0.09 && speed > 0.0825)
                tooltip.add((new TranslatableComponent("info.tac.standardWeightGun", new TranslatableComponent( -((int)((0.1 - speed)*1000))+"%").withStyle(ChatFormatting.RED)).withStyle(ChatFormatting.DARK_GREEN)));
            else
                tooltip.add((new TranslatableComponent("info.tac.heavyWeightGun", new TranslatableComponent(-((int)((0.1 - speed)*1000))+"%").withStyle(ChatFormatting.RED)).withStyle(ChatFormatting.DARK_RED)));
            //tooltip.add((new TranslatableComponent("info.tac.oldRifleScope", new TranslatableComponent("OldScope").withStyle(ChatFormatting.BOLD)).withStyle(ChatFormatting.LIGHT_PURPLE)));
        }

        if(tagCompound != null)
        {
            //tooltip.add((new TranslatableComponent("info.tac.oldRifle", new TranslatableComponent(IAttachment.Type.OLD_SCOPE.getTranslationKey())).withStyle(ChatFormatting.GREEN)));
            tooltip.add((new TranslatableComponent("info.moonshine.oldRifleScope", new TranslatableComponent("Rain").withStyle(ChatFormatting.BOLD)).withStyle(ChatFormatting.BLUE)));
        }
        tooltip.add((new TranslatableComponent("info.tac.attachment_help", new Object[]{(new KeybindComponent("key.tac.attachments")).getString().toUpperCase(Locale.ENGLISH)})).withStyle(ChatFormatting.YELLOW));
    }
    public MBPGunItem() {
        this(properties -> properties);
    }
}