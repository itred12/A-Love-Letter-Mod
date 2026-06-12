package com.itred.aloveletter.datagen.assets

import com.itred.aloveletter.ALoveLetter
import com.itred.aloveletter.registrar.ALLItems
import com.itred.aloveletter.registrar.ALLSounds
import net.minecraft.data.PackOutput
import net.minecraft.sounds.SoundEvent
import net.minecraftforge.common.data.LanguageProvider
import java.util.function.Supplier

class PackLanguageProvider(pack: PackOutput): LanguageProvider(pack, ALoveLetter.MODID, "en_us") {

    companion object {
        //private const val SUBTITLE_PREFIX: String = "subtitles." + ALoveLetter.MODID + "."
    }

    override fun addTranslations() {

        itemTranslations()
        subtitleTranslations()

    }

    private fun itemTranslations() {

        add(ALLItems.SPEED_COLA, "SCP-207")

    }

    // Make my life easier here, section it off a bit more
    private fun subtitleTranslations() {

        addSubtitle(ALLSounds.BLUEAXOLOTL_BW, "Blue axolotl twinkles")
        addSubtitle(ALLSounds.BLUEAXOLOTL_PLA, "Blue axolotl twinkles")

    }


    /** Add a translation for a subtitle of a given sound event, assuming the sound event was added through PackSoundProvider$addSimpleSound(). */
    private fun addSubtitle(event: Supplier<SoundEvent>, translation: String) {
        add(
            // The same logic the soundprovider uses to make the subtitles, so they'll always be the same
            PackSoundProvider.Companion.SUBTITLE_PREFIX + PackSoundProvider.Companion.stripSoundResourceLocationForSubtitle(event.get().location),
            translation
        )
    }

}