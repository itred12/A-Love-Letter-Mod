package com.itred.aloveletter.registrar

import com.itred.aloveletter.enchantment.WhirlpoolEnchantment
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.EnchantmentCategory
import net.minecraftforge.registries.ForgeRegistries
import thedarkcolour.kotlinforforge.forge.ObjectHolderDelegate
import thedarkcolour.kotlinforforge.forge.registerObject

object ALLEnchantments : AbstractRegistrar<Enchantment>(ForgeRegistries.ENCHANTMENTS) {

    val WHIRLPOOL = register("whirlpool", WhirlpoolEnchantment(
        Enchantment.Rarity.UNCOMMON, // Matches other trident enchantments
        EnchantmentCategory.TRIDENT,
        EquipmentSlot.MAINHAND
    ))


    private fun register(id: String, enchantment: Enchantment): ObjectHolderDelegate<Enchantment> {
        return this.registry.registerObject(id, { enchantment })
    }
}