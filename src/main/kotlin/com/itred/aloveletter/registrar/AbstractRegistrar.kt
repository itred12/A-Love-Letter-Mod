package com.itred.aloveletter.registrar

import com.itred.aloveletter.ALoveLetter
import net.minecraftforge.eventbus.api.IEventBus
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.IForgeRegistry


// Just in case you thought I couldnt go to further lengths to be lazy
open class AbstractRegistrar<T>(targetRegistry: IForgeRegistry<T>)  {

    val registry: DeferredRegister<T> = DeferredRegister.create(targetRegistry, ALoveLetter.MODID)!!



    companion object {

        fun registerAll(modBus: IEventBus) {
            ALLItems.register(modBus)
            ALLSounds.register(modBus)
            ALLStatusEffects.register(modBus)
            ALLPotions.register(modBus)
        }

    }

    protected fun register(modbus: IEventBus) {
        ALoveLetter.LOGGER.info("Registering ${ALoveLetter.resourceLocationToHumanReadable(this.registry.registryName)}s!")
        registry.register(modbus)
    }


}