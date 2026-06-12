package com.itred.aloveletter.datagen.data.registry

import net.minecraft.core.registries.Registries
import net.minecraft.data.worldgen.BootstapContext
import net.minecraft.world.damagesource.DamageType

object DamageTypeRegistryProvider : AbstractPackRegistrar<DamageType>(Registries.DAMAGE_TYPE) {

    val SUCROSE_SICKNESS = this.createKey("sugar_crash")

    override fun bootstrap(context: BootstapContext<DamageType>) {
        context.register(SUCROSE_SICKNESS, DamageType("sugarCrashed", 0.0F))
    }

}