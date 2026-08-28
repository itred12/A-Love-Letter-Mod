package com.itred.aloveletter.enchantment

import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TridentItem
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.EnchantmentCategory
import net.minecraft.world.item.enchantment.Enchantments

class WhirlpoolEnchantment(rarity: Rarity, category: EnchantmentCategory, vararg applicableSlots: EquipmentSlot)
    : Enchantment(rarity, category, applicableSlots) {

    // Range from 1,
    override fun getMinLevel(): Int {
        return 1
    }

    // To level 3
    override fun getMaxLevel(): Int {
        return 3
    }

    // Only incompatible with Knockback
    // (not attainable for Tridents under normal conditions, but with other mods in the mix, I figured it'd be good practice)
    override fun checkCompatibility(pOther: Enchantment): Boolean {
        return pOther != Enchantments.KNOCKBACK && pOther != Enchantments.RIPTIDE
    }


    // Applicable to tridents and anything extending its class (may be a bad idea)
    override fun canEnchant(pStack: ItemStack): Boolean {
        return pStack.item is TridentItem
    }




}