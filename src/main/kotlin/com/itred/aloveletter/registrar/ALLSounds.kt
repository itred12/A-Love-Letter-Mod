package com.itred.aloveletter.registrar

import com.itred.aloveletter.ALoveLetter
import net.minecraft.sounds.SoundEvent
import net.minecraftforge.registries.ForgeRegistries
import java.util.function.Supplier

object ALLSounds: AbstractRegistrar<SoundEvent>(ForgeRegistries.SOUND_EVENTS) {

    val BLUEAXOLOTL_PLA: Supplier<SoundEvent> = newSoundEvent("sound.effect.blueaxolotl_pla")
    val BLUEAXOLOTL_BW: Supplier<SoundEvent> = newSoundEvent("sound.effect.blueaxolotl_bw")

    private fun newSoundEvent(name: String): Supplier<SoundEvent> {
        return this.registry.register(name, { SoundEvent.createVariableRangeEvent(ALoveLetter.modLoc(name)) })
    }

}