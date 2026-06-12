package com.itred.aloveletter.registrar

import com.itred.aloveletter.mobeffect.HyperactiveStatusEffect
import net.minecraft.util.FastColor
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffectCategory
import net.minecraftforge.registries.ForgeRegistries

object ALLStatusEffects : AbstractRegistrar<MobEffect>(ForgeRegistries.MOB_EFFECTS) {

    val HYPERACTIVE = registry.register("hyperactive") {
        HyperactiveStatusEffect(MobEffectCategory.NEUTRAL, FastColor.ARGB32.color(100, 100, 0, 255))
    }



}