package com.itred.aloveletter.registrar

import com.itred.aloveletter.mobeffect.HyperactiveStatusEffect
import net.minecraft.world.effect.MobEffect
import net.minecraftforge.registries.ForgeRegistries
import thedarkcolour.kotlinforforge.forge.registerObject

object ALLStatusEffects : AbstractRegistrar<MobEffect>(ForgeRegistries.MOB_EFFECTS) {

    val HYPERACTIVE by registry.registerObject("hyperactive") {
        HyperactiveStatusEffect
    }



}