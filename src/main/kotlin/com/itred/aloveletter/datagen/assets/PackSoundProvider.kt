package com.itred.aloveletter.datagen.assets

import com.itred.aloveletter.ALoveLetter
import com.itred.aloveletter.registrar.ALLSounds
import net.minecraft.data.PackOutput
import net.minecraft.resources.ResourceLocation
import net.minecraft.sounds.SoundEvent
import net.minecraftforge.common.data.ExistingFileHelper
import net.minecraftforge.common.data.SoundDefinition
import net.minecraftforge.common.data.SoundDefinitionsProvider
import java.util.function.Supplier

class PackSoundProvider(pack: PackOutput, existingFileHelper: ExistingFileHelper): SoundDefinitionsProvider(pack, ALoveLetter.MODID, existingFileHelper) {

    companion object {
        const val SUBTITLE_PREFIX: String = "subtitles." + ALoveLetter.MODID + "."

        fun stripSoundResourceLocationForSubtitle(location: ResourceLocation): String {
            return location.toString().substringAfter('.')
        }
    }

    override fun registerSounds() {

        addSimpleSound(ALLSounds.BLUEAXOLOTL_BW, "effect/blueaxolotl_shiny_bw")
        addSimpleSound(ALLSounds.BLUEAXOLOTL_PLA, "effect/blueaxolotl_shiny_pla")


        addSimpleSound(ALLSounds.DISC_FLOWERMAN, "disc/tobyfox_camellia-flowerman")

        addFloweryVoiceClip(ALLSounds.FLOWERY_VOICECLIP_HOO, "hoo")
        addFloweryVoiceClip(ALLSounds.FLOWERY_VOICECLIP_HA, "huh")
        addFloweryVoiceClip(ALLSounds.FLOWERY_VOICECLIP_IMFALLING, "imfalling")
        addFloweryVoiceClip(ALLSounds.FLOWERY_VOICECLIP_GOODBYE, "goodbye")
        addFloweryVoiceClip(ALLSounds.FLOWERY_VOICECLIP_FLOWERY, "flowery2")
        addFloweryVoiceClip(ALLSounds.FLOWERY_VOICECLIP_ITSMEFLOWERY, "itsmeflowery")
        addFloweryVoiceClip(ALLSounds.FLOWERY_VOICECLIP_HEYGUYS, "heyguys")
        addFloweryVoiceClip(ALLSounds.FLOWERY_VOICECLIP_SORRYTOKEEPYOUWAITING, "sorrytokeepyouwaiting2")

        addFloweryVoiceClip(ALLSounds.FLOWERY_VOICECLIP_SUSTINGUS, "sustingus")
        addFloweryVoiceClip(ALLSounds.FLOWERY_VOICECLIP_LEAFITTOME, "leafittome")
        addFloweryVoiceClip(ALLSounds.FLOWERY_VOICECLIP_YES, "yes")

        addFloweryVoiceClip(ALLSounds.FLOWERY_VOICECLIP_JARONA, "jarona1", "jarona2", "jarona3", "jarona4")

        addFloweryVoiceClip(ALLSounds.FLOWERY_VOICECLIP_ALLACCORDINGTOPLANT, "allaccordingtoplant")
        addFloweryVoiceClip(ALLSounds.FLOWERY_VOICECLIP_GETACHANCE, "getachance1", "getachance2")
        addFloweryVoiceClip(ALLSounds.FLOWERY_VOICECLIP_HEHITSMYJARONA, "hehitsmyjarona")
        addFloweryVoiceClip(ALLSounds.FLOWERY_VOICECLIP_TAKETHAT, "takethat")
        addFloweryVoiceClip(ALLSounds.FLOWERY_VOICECLIP_SORRYABOUTTHATGUYS, "sorryaboutthatguys")
        addFloweryVoiceClip(ALLSounds.FLOWERY_VOICECLIP_WHATAPREDICTABLECREATURE, "whatapredictablecreature")

        addFloweryVoiceClip(ALLSounds.FLOWERY_VOICECLIP_LENDMEYOURPOWER, "lendmeyourpower")
        addFloweryVoiceClip(ALLSounds.FLOWERY_VOICECLIP_HEREICOMESANFRANDISC, "hereicomesanfrandisc")
        addFloweryVoiceClip(ALLSounds.FLOWERY_VOICECLIP_ONEMOREFORTHEFANS, "onemoreforthefans")

        addFloweryVoiceClip(ALLSounds.FLOWERY_VOICECLIP_MYKING, "myking")
        addFloweryVoiceClip(ALLSounds.FLOWERY_VOICECLIP_SORRYTOKEEPALADYINWAITING,"sorrytokeepaladyinwaiting")


    }



    /** Returns a basic sound definition, with only one sound. The path is a slash-concatenated path starting from the assets/aloveletter/sounds folder. */
    private fun simpleSoundDefinition(filePath: String): SoundDefinition {
        return SoundDefinition.definition()
            .with(SoundDefinition.Sound.sound(ALoveLetter.modLoc(filePath), SoundDefinition.SoundType.SOUND))
    }

    private fun simpleSoundDefinition(vararg filePaths: String): SoundDefinition {
        val definition = SoundDefinition.definition()
        for (indvPath in filePaths) {
            definition.with(SoundDefinition.Sound.sound(ALoveLetter.modLoc(indvPath), SoundDefinition.SoundType.SOUND))
        }
        return definition
    }

    /** Adds a complete sound, with subtitle key included */
    private fun addSimpleSound(event: Supplier<SoundEvent>, filePath: String) {
        add(event,
            simpleSoundDefinition(filePath)
                .subtitle(SUBTITLE_PREFIX + stripSoundResourceLocationForSubtitle(event.get().location))
            )
    }

    private fun addSimpleSound(event: Supplier<SoundEvent>, vararg filePaths: String) {
        add(event,
            simpleSoundDefinition(*filePaths)
                .subtitle(SUBTITLE_PREFIX + stripSoundResourceLocationForSubtitle(event.get().location))
        )
    }

    /** Yes! */
    private fun addFloweryVoiceClip(event: Supplier<SoundEvent>, clipContents: String) {
        addSimpleSound(event, "effect/discery/flowery_voiceclip_$clipContents")
    }

    private fun addFloweryVoiceClip(event: Supplier<SoundEvent>, vararg clipContents: String) {
        val newContents: Array<String> = Array(clipContents.size) { index -> "effect/discery/flowery_voiceclip_${clipContents[index]}" }
        addSimpleSound(event, *newContents)
    }





}