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

    }

    /** Returns a basic sound definition, with only one sound. The path is a slash-concatenated path starting from the assets/aloveletter/sounds folder. */
    private fun simpleSoundDefinition(filePath: String): SoundDefinition {
        return SoundDefinition.definition()
            .with(SoundDefinition.Sound.sound(ALoveLetter.modLoc(filePath), SoundDefinition.SoundType.SOUND))
    }

    /** Adds a complete sound, with subtitle key included */
    private fun addSimpleSound(event: Supplier<SoundEvent>, filePath: String) {
        add(event,
            simpleSoundDefinition(filePath)
                .subtitle(SUBTITLE_PREFIX + stripSoundResourceLocationForSubtitle(event.get().location))
            )
    }



}