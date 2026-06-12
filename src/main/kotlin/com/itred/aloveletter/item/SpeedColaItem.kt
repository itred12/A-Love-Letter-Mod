package com.itred.aloveletter.item

import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.item.PotionItem

class SpeedColaItem(properties: Properties) : PotionItem(properties) {

    override fun getEatingSound(): SoundEvent? {
        return SoundEvents.HONEY_DRINK
    }

    override fun getDrinkingSound(): SoundEvent? {
        return SoundEvents.HONEY_DRINK
    }

}