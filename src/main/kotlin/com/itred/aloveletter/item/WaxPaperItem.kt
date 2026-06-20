package com.itred.aloveletter.item

import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.enchantment.Enchantment

class WaxPaperItem(properties: Item.Properties) : Item(properties) {

    override fun isEnchantable(pStack: ItemStack): Boolean {
        return pStack.count == 1
    }

    override fun getEnchantmentValue(stack: ItemStack?): Int {
        return 25 // +3 points from gold
    }

    override fun canApplyAtEnchantingTable(stack: ItemStack?, enchantment: Enchantment?): Boolean {
        return true
    }

}