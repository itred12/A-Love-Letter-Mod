package com.itred.aloveletter.registrar

import com.itred.aloveletter.ALoveLetter
import net.minecraft.sounds.SoundEvent
import net.minecraftforge.registries.ForgeRegistries
import java.util.function.Supplier

object ALLSounds: AbstractRegistrar<SoundEvent>(ForgeRegistries.SOUND_EVENTS) {

    val BLUEAXOLOTL_PLA: Supplier<SoundEvent> = newSoundEvent("sound.effect.blueaxolotl_pla")
    val BLUEAXOLOTL_BW: Supplier<SoundEvent> = newSoundEvent("sound.effect.blueaxolotl_bw")

    val FLOWERY_VOICECLIP_HOO: Supplier<SoundEvent> = newSoundEffect("flowery_voiceclip_hoo")
    val FLOWERY_VOICECLIP_HA: Supplier<SoundEvent> = newSoundEffect("flowery_voiceclip_ha")
    val FLOWERY_VOICECLIP_IMFALLING: Supplier<SoundEvent> = newSoundEffect("flowery_voiceclip_imfalling")
    val FLOWERY_VOICECLIP_GOODBYE: Supplier<SoundEvent> = newSoundEffect("flowery_voiceclip_goodbye")

    val FLOWERY_VOICECLIP_FLOWERY: Supplier<SoundEvent> = newSoundEffect("flowery_voiceclip_flowery")
    val FLOWERY_VOICECLIP_HEYGUYS: Supplier<SoundEvent> = newSoundEffect("flowery_voiceclip_heyguys")
    val FLOWERY_VOICECLIP_ITSMEFLOWERY: Supplier<SoundEvent> = newSoundEffect("flowery_voiceclip_itsmeflowery")
    val FLOWERY_VOICECLIP_SORRYTOKEEPYOUWAITING: Supplier<SoundEvent> = newSoundEffect("flowery_voiceclip_sorrytokeepyouwaiting")

    val FLOWERY_VOICECLIP_LEAFITTOME: Supplier<SoundEvent> = newSoundEffect("flowery_voiceclip_leafittome")
    val FLOWERY_VOICECLIP_SUSTINGUS: Supplier<SoundEvent> = newSoundEffect("flowery_voiceclip_sustingus")
    val FLOWERY_VOICECLIP_YES: Supplier<SoundEvent> = newSoundEffect("flowery_voiceclip_yes")

    val FLOWERY_VOICECLIP_JARONA: Supplier<SoundEvent> = newSoundEffect("flowery_voiceclip_jarona")

    val FLOWERY_VOICECLIP_HEHITSMYJARONA: Supplier<SoundEvent> = newSoundEffect("flowery_voiceclip_hehitsmyjarona")
    val FLOWERY_VOICECLIP_GETACHANCE: Supplier<SoundEvent> = newSoundEffect("flowery_voiceclip_getachance")
    val FLOWERY_VOICECLIP_SORRYABOUTTHATGUYS: Supplier<SoundEvent> = newSoundEffect("flowery_voiceclip_sorryaboutthatguys")
    val FLOWERY_VOICECLIP_WHATAPREDICTABLECREATURE: Supplier<SoundEvent> = newSoundEffect("flowery_voiceclip_whatapredictablecreature")
    val FLOWERY_VOICECLIP_TAKETHAT: Supplier<SoundEvent> = newSoundEffect("flowery_voiceclip_takethat")
    val FLOWERY_VOICECLIP_ALLACCORDINGTOPLANT: Supplier<SoundEvent> = newSoundEffect("flowery_voiceclip_allaccordingtoplant")

    val FLOWERY_VOICECLIP_HEREICOMESANFRANDISC: Supplier<SoundEvent> = newSoundEffect("flowery_voiceclip_hereicomesanfrandisc")
    val FLOWERY_VOICECLIP_LENDMEYOURPOWER: Supplier<SoundEvent> = newSoundEffect("flowery_voiceclip_lendmeyourpower")

    val FLOWERY_VOICECLIP_ONEMOREFORTHEFANS: Supplier<SoundEvent> = newSoundEffect("flowery_voiceclip_onemoreforthefans")
    val FLOWERY_VOICECLIP_MYKING: Supplier<SoundEvent> = newSoundEffect("flowery_voiceclip_myking")
    val FLOWERY_VOICECLIP_SORRYTOKEEPALADYINWAITING: Supplier<SoundEvent> = newSoundEffect("flowery_voiceclip_sorrytokeepaladyinwaiting")


    val DISC_FLOWERMAN: Supplier<SoundEvent> = newSoundEvent("sound.disc.tobyfox_camellia-flowerman")


    private fun newSoundEffect(name: String): Supplier<SoundEvent> {
        return newSoundEvent("sound.effect.$name")
    }

    private fun newSoundEvent(name: String): Supplier<SoundEvent> {
        return this.registry.register(name, { SoundEvent.createVariableRangeEvent(ALoveLetter.modLoc(name)) })
    }

}