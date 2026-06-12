package com.itred.aloveletter.datagen.assets

import com.itred.aloveletter.ALoveLetter
import com.itred.aloveletter.datagen.data.registry.DamageTypeRegistryProvider
import com.itred.aloveletter.registrar.ALLItems
import com.itred.aloveletter.registrar.ALLSounds
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.data.PackOutput
import net.minecraft.resources.ResourceKey
import net.minecraft.sounds.SoundEvent
import net.minecraft.world.damagesource.DamageType
import net.minecraftforge.common.data.LanguageProvider
import java.util.concurrent.CompletableFuture
import java.util.function.Supplier

class PackLanguageProvider(pack: PackOutput, val lookupProvider: CompletableFuture<HolderLookup.Provider>): LanguageProvider(pack, ALoveLetter.MODID, "en_us") {

    override fun addTranslations() {

        add(ALLItems.SPEED_COLA, "SCP-207")

        addSubtitle(ALLSounds.BLUEAXOLOTL_BW, "Blue axolotl twinkles")
        addSubtitle(ALLSounds.BLUEAXOLOTL_PLA, "Blue axolotl twinkles")

        addDamageType(
            DamageTypeRegistryProvider.SUCROSE_SICKNESS,
            normalDeath = "%s's blood turned to caffeine",
            assist = "%s experienced a heart attack whilst trying to escape %s"
            )

    }





    /** Add a translation for a subtitle of a given sound event, assuming the sound event was added through PackSoundProvider$addSimpleSound(). */
    fun addSubtitle(event: Supplier<SoundEvent>, translation: String) {
        add(
            // The same logic the soundprovider uses to make the subtitles, so they'll always be the same
            PackSoundProvider.SUBTITLE_PREFIX + PackSoundProvider.stripSoundResourceLocationForSubtitle(event.get().location),
            translation
        )
    }


    /** Adds translations for the death message(s) listed by a given damage type.
     *
     * Every damage type has 3 types of death message, what you put in them and if you specify them at all depends on the usage of the damage type:
     *
     * - `death.attack.<msgId>.item`: if the damage type could have an attacker, and that attacker is holding an item, then this key will be used.
     *
     *     - Has 3 insertions: the name of the victim, the name of the attacker, and the name of the item held.
     *      I.e., "%s was slain by %s using %s".
     *
     *     - **Specify this translation with the `withWeaponItem` argument.**
     *
     *
     * - `death.attack.<msgId>.player`: if the damage type could kill an entity shortly after they had taken damage from a living attacker, then this key will be used.
     *
     *     - Has 2 insertions: the name of the victim and the name of the recent attacker.
     *       I.e., "%s drowned whilst trying to escape %s".
     *
     *     - **Specify this translation with the `assist` argument.**
     *
     *
     * - `death.attack.<msgId>`: if neither of the above two apply, then it's a "normal death", and this key is used.
     *
     *      - Has 2 insertions: the name of the vitcim and the name of the attacker (IF there is one).
     *        I.e., "%s was slain by %s" or "%s drowned".
     *
     *      - **Specify this translation with the `normalDeath` argument.**
     *
     * Further reading: https://minecraft.wiki/w/Damage_type
     *
     * */
    fun addDamageType(key: ResourceKey<DamageType>, normalDeath: String? = null, withWeaponItem: String? = null, assist: String? = null) {

        val registry = lookupProvider.get().lookup(Registries.DAMAGE_TYPE).get() ?: return
        val damageType = registry.getOrThrow(key)

        val translationKey = "death.attack." + damageType.get().msgId

        if (normalDeath != null) {
            add(translationKey, normalDeath)
        }

        if (withWeaponItem != null) {
            add("$translationKey.item", withWeaponItem)
        }

        if (assist != null) {
            add("$translationKey.player", assist)
        }


    }

}