package com.itred.aloveletter.mobeffect

import com.itred.aloveletter.datagen.data.registry.DamageTypeRegistryProvider
import net.minecraft.core.registries.Registries
import net.minecraft.util.FastColor
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.damagesource.DamageType
import net.minecraft.world.effect.MobEffectCategory
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.entity.ai.attributes.Attributes
import java.util.*
import kotlin.math.max

object HyperactiveStatusEffect : CustomMobEffect(
    MobEffectCategory.NEUTRAL,
    FastColor.ARGB32.color(255, 73, 38, 5)
    //4793861
    // 73, 38, 5
    ) {

    // Jump boost is 0.1, this is half
    private const val JUMP_BONUS_PER_LEVEL = 0.05F

    override val effectTickInterval: Int = 100

    private const val INTERVAL_SHRINK_PER_LEVEL = 30
    private const val LOWEST_EFFECT_TICK_INTERVAL = 40
    private const val DAMAGE_PER_LEVEL = 1

    override fun applyEffectTick(pLivingEntity: LivingEntity?, pAmplifier: Int) {

        // TODO: Saturation not consumed except from healing

        // TODO: FIND SOME BETTER WAY TO MAKE A DAMAGE SOURCE PLEASE GOD
        val type = pLivingEntity?.level()?.registryAccess()?.lookupOrThrow<DamageType>(Registries.DAMAGE_TYPE)?.getOrThrow(
            DamageTypeRegistryProvider.SUCROSE_SICKNESS)

        pLivingEntity?.hurt(
            DamageSource(type), (1 + DAMAGE_PER_LEVEL * (pAmplifier + 1.0F))
        )

    }

    // Change the effect tick interval linearly per-level rather than quadratically(?)
    override fun isDurationEffectTick(duration: Int, amplifier: Int): Boolean {
        val interval = max(LOWEST_EFFECT_TICK_INTERVAL, effectTickInterval - (INTERVAL_SHRINK_PER_LEVEL * amplifier))
        return duration % interval == 0

    }

    init {
        // Same as speed
        this.addAttributeModifier(
            Attributes.MOVEMENT_SPEED,
            UUID.randomUUID().toString(),
            0.25,
            AttributeModifier.Operation.MULTIPLY_TOTAL
            )
    }

    override fun getAttributeModifierValue(pAmplifier: Int, pModifier: AttributeModifier?): Double {
        // Level of 0 is 0.5 (< speed 2) and every level above that adds 0.25 (< 1 level of speed)
        return 0.25 * (pAmplifier + 2)
    }

    fun getJumpBonus(amplifier: Int): Float {
        return (amplifier + 1.0F) * JUMP_BONUS_PER_LEVEL
    }

    fun addSafeBlocksOfFallDistance(amplifier: Int): Float {
        return amplifier.toFloat() // Same as jump boost, every level is an extra 1 block of fall damage safety
    }





}