package com.itred.aloveletter.item

import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.ItemUtils
import net.minecraft.world.item.UseAnim
import net.minecraft.world.level.Level

class SpeedColaItem(properties: Properties) : Item(properties) {

    override fun getUseDuration(pStack: ItemStack?): Int {
        return 32
    }

    override fun getUseAnimation(pStack: ItemStack?): UseAnim? {
        return UseAnim.DRINK
    }

    override fun use(
        pLevel: Level?,
        pPlayer: Player?,
        pUsedHand: InteractionHand?
    ): InteractionResultHolder<ItemStack?>? {
        return ItemUtils.startUsingInstantly(pLevel, pPlayer, pUsedHand)
    }

    override fun getEatingSound(): SoundEvent? {
        return SoundEvents.HONEY_DRINK
    }

    override fun getDrinkingSound(): SoundEvent? {
        return SoundEvents.HONEY_DRINK
    }

}