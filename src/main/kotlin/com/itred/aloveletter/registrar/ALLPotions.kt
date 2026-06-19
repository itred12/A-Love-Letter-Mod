package com.itred.aloveletter.registrar

import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.item.alchemy.Potion
import net.minecraftforge.registries.ForgeRegistries
import thedarkcolour.kotlinforforge.forge.registerObject

object ALLPotions : AbstractRegistrar<Potion>(ForgeRegistries.POTIONS) {


    val POTION_HYPERACTIVE by registry.registerObject("hyperactive") {
        Potion(MobEffectInstance(ALLStatusEffects.HYPERACTIVE, 4 * 60 * 20))
    }

}