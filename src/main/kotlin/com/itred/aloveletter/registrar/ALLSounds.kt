package com.itred.aloveletter.registrar

import com.itred.aloveletter.ALoveLetter
import net.minecraft.sounds.SoundEvent
import net.minecraftforge.eventbus.api.IEventBus
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import java.util.function.Supplier

object ALLSounds {

    private val SOUND_EVENT_REGISTRY: DeferredRegister<SoundEvent> = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS,
        ALoveLetter.MODID)

    val BLUEAXOLOTL_PLA: Supplier<SoundEvent> = newSoundEvent("sound.effect.blueaxolotl_pla")
    val BLUEAXOLOTL_BW: Supplier<SoundEvent> = newSoundEvent("sound.effect.blueaxolotl_bw")

    fun register(modBus: IEventBus) {
        ALoveLetter.LOGGER.info("Registering sound events...")
        SOUND_EVENT_REGISTRY.register(modBus)
    }

    private fun newSoundEvent(name: String): Supplier<SoundEvent> {
        return SOUND_EVENT_REGISTRY.register(name, { SoundEvent.createVariableRangeEvent(ALoveLetter.modLoc(name)) })
    }

}