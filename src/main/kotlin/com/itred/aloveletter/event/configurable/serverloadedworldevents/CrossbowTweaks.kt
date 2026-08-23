package com.itred.aloveletter.event.configurable.serverloadedworldevents

import com.itred.aloveletter.config.ALLConfig
import com.itred.aloveletter.event.configurable.IConfigurableEventHandler
import net.minecraft.world.entity.projectile.AbstractArrow
import net.minecraft.world.entity.projectile.Arrow
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.entity.living.LivingHurtEvent
import net.minecraftforge.eventbus.api.IEventBus
import net.minecraftforge.eventbus.api.SubscribeEvent
import kotlin.math.min

object CrossbowTweaks : IConfigurableEventHandler {

    const val TAG_ID = "aloveletter.ismultishotarrow"
    // The damage cap on each subsequent multishot arrow
    const val SUBSEQUENT_MULTISHOT_ARROW_MAX_DAMAGE = 7f


    // The amount of time that needs to pass, in game ticks, for new multishot arrows to not be considered a part of an older batch
    // (thus having only the damage of every arrow after the first reduced, instead of all of them)
    // Tracked using the target's hurtTime property, and as such, must be less than 10.
    const val SUBSEQUENT_MULTISHOT_ARROW_TIME_LENIENCY = 5


    var multishotTweaks: Boolean = false
    var crossbowDamageBuff: Boolean = false

    override var isEnabled: Boolean = false

    @SubscribeEvent
    fun reduceMultishotSubsequentDamage(event: LivingHurtEvent) {

        val targetEntity = event.entity
        val source = event.source

        val arrowEntity = source.directEntity


        if (arrowEntity !is Arrow || !arrowEntity.shotFromCrossbow() || !arrowEntity.tags.contains(TAG_ID)) {
            return
        }

        val lastDirectEntity = targetEntity.lastDamageSource?.directEntity

        if (
            lastDirectEntity is AbstractArrow
            && lastDirectEntity.shotFromCrossbow()
            && lastDirectEntity.tags.contains(TAG_ID)
            && targetEntity.hurtTime > 10 - SUBSEQUENT_MULTISHOT_ARROW_TIME_LENIENCY
            ) {
            event.amount = min(event.amount, SUBSEQUENT_MULTISHOT_ARROW_MAX_DAMAGE)
        }


    }


    override fun shouldEnable(): Boolean {
        return ALLConfig.COMMON_CONFIG.rebalanceEnableCrossbowBuffs.get() || ALLConfig.COMMON_CONFIG.rebalanceEnableMultishotNoInvulnerability.get()
    }

    override fun enable(modBus: IEventBus) {
        isEnabled = true

        crossbowDamageBuff = ALLConfig.COMMON_CONFIG.rebalanceEnableCrossbowBuffs.get()
        multishotTweaks = ALLConfig.COMMON_CONFIG.rebalanceEnableMultishotNoInvulnerability.get()
        if (multishotTweaks) {
            MinecraftForge.EVENT_BUS.register(this)
        }
    }

    override fun disable(modBus: IEventBus) {
        isEnabled = false
        MinecraftForge.EVENT_BUS.unregister(this)

    }
}