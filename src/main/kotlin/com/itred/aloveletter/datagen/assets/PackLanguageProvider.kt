package com.itred.aloveletter.datagen.assets

import com.itred.aloveletter.ALoveLetter
import com.itred.aloveletter.config.ALLConfigScreen
import com.itred.aloveletter.config.impl.AbstractConfigSection
import com.itred.aloveletter.datagen.data.registry.DamageTypeRegistryProvider
import com.itred.aloveletter.registrar.ALLItems
import com.itred.aloveletter.registrar.ALLSounds
import com.itred.aloveletter.registrar.ALLStatusEffects
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.data.PackOutput
import net.minecraft.resources.ResourceKey
import net.minecraft.sounds.SoundEvent
import net.minecraft.world.damagesource.DamageType
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.item.RecordItem
import net.minecraft.world.item.alchemy.Potion
import net.minecraftforge.common.data.LanguageProvider
import java.util.concurrent.CompletableFuture
import java.util.function.Supplier

class PackLanguageProvider(pack: PackOutput, val lookupProvider: CompletableFuture<HolderLookup.Provider>): LanguageProvider(pack, ALoveLetter.MODID, "en_us") {

    override fun addTranslations() {

        addItem(ALLItems.SPEED_COLA, "SCP-207", "Harmfully increases motor functions.", "Stacks up to 3")
        add("${ALLItems.SPEED_COLA.descriptionId}.speedModifier", "+%s%% Speed (+%s%% per stack)")
        add("${ALLItems.SPEED_COLA.descriptionId}.damageOverTime", "-%s health/%s seconds (-%s health, -%s second(s) per stack)")

        add(ALLItems.WAX_PAPER, "Wax Paper")
        add(ALLItems.ENCHANTED_PARCHMENT, "Enchanted Parchment")

        addMusicDiscItem(ALLItems.DISCERY, "Discery", "Toby Fox and Camellia", "Flower Man")


        addSubtitle(ALLSounds.BLUEAXOLOTL_BW, "Blue axolotl twinkles")
        addSubtitle(ALLSounds.BLUEAXOLOTL_PLA, "Blue axolotl twinkles")

        addSubtitle(ALLSounds.FLOWERY_VOICECLIP_HA, "Ha!")
        addSubtitle(ALLSounds.FLOWERY_VOICECLIP_HOO, "Hoo!")
        addSubtitle(ALLSounds.FLOWERY_VOICECLIP_IMFALLING, "I'm falling!")
        addSubtitle(ALLSounds.FLOWERY_VOICECLIP_GOODBYE, "Goodbye...")
        addSubtitle(ALLSounds.FLOWERY_VOICECLIP_ITSMEFLOWERY, "It's me, Flowery!")
        addSubtitle(ALLSounds.FLOWERY_VOICECLIP_HEYGUYS, "Hey guys!")
        addSubtitle(ALLSounds.FLOWERY_VOICECLIP_FLOWERY, "Flowery.")
        addSubtitle(ALLSounds.FLOWERY_VOICECLIP_SORRYTOKEEPYOUWAITING, "Sorry to keep you waiting!")
        addSubtitle(ALLSounds.FLOWERY_VOICECLIP_LEAFITTOME, "Leaf it to me!")
        addSubtitle(ALLSounds.FLOWERY_VOICECLIP_YES, "Yes!")
        addSubtitle(ALLSounds.FLOWERY_VOICECLIP_SUSTINGUS, "Sustingus!!")
        addSubtitle(ALLSounds.FLOWERY_VOICECLIP_JARONA, "Jarona!")
        addSubtitle(ALLSounds.FLOWERY_VOICECLIP_ALLACCORDINGTOPLANT, "All according t- all according to plant.")
        addSubtitle(ALLSounds.FLOWERY_VOICECLIP_GETACHANCE, "Get a chance.")
        addSubtitle(ALLSounds.FLOWERY_VOICECLIP_HEHITSMYJARONA, "Heh, it's my Jarona!")
        addSubtitle(ALLSounds.FLOWERY_VOICECLIP_SORRYABOUTTHATGUYS, "Sorry about that, guys.")
        addSubtitle(ALLSounds.FLOWERY_VOICECLIP_TAKETHAT, "Take that!")
        addSubtitle(ALLSounds.FLOWERY_VOICECLIP_WHATAPREDICTABLECREATURE, "What a predictable creature.")
        addSubtitle(ALLSounds.FLOWERY_VOICECLIP_LENDMEYOURPOWER, "LEND ME YOUR POWER!!")
        addSubtitle(ALLSounds.FLOWERY_VOICECLIP_HEREICOMESANFRANDISC, "Here I come San-Frandisc-")
        addSubtitle(ALLSounds.FLOWERY_VOICECLIP_ONEMOREFORTHEFANS, "One more for the fans!")
        addSubtitle(ALLSounds.FLOWERY_VOICECLIP_MYKING, "My king!")
        addSubtitle(ALLSounds.FLOWERY_VOICECLIP_SORRYTOKEEPALADYINWAITING, "Sorry to keep a lady in waiting.")


        addSubtitle(ALLSounds.DISC_FLOWERMAN, "Flower Man by Toby Fox and Camellia plays")

        addDamageType(
            DamageTypeRegistryProvider.SUCROSE_SICKNESS,
            normalDeath = "%s's blood turned to caffeine",
            assist = "%s experienced a heart attack whilst trying to escape %s"
            )

        add(ALLStatusEffects.HYPERACTIVE, "Hyperactive")

        addCreativeTab(ALLItems.ALLTab, "A Love Letter")


        addConfigCategory(ALLConfigScreen.ClientGeneral, "Client Config", "Client-sided things added by the mod")
        addConfigCategory(ALLConfigScreen.CommonFunMisc, "Fun / Misc", "Fun or miscellaneous things added by the mod")
        addConfigCategory(ALLConfigScreen.CommonBalance, "Balance Tweaks", "Balancing changes added by the mod")
        addConfigCategory(ALLConfigScreen.CommonDurabilityTweaks, "Durability Tweaks", "Durability changes added by the mod")

        addOptionGroup("blueaxolotltweaks", "Blue Axolotl Tweaks")

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

    /** Adds an item with a varying amount of tooltip descriptions.
     *
     * For every tooltip string provided, the translation key for that tooltip is (itemkey).tooltip_N, with N being the tooltip number, index-1. */
    fun addItem(key: Item, nameTranslation: String, vararg tooltips: String) {
        add(key, nameTranslation)

        var tooltipIndex = 1
        for (tooltip in tooltips) {
            add("${key.descriptionId}.tooltip_${tooltipIndex}", tooltip)
            tooltipIndex++
        }
    }

    fun addPotion(potion: Potion, translation: String) {
        val prefix = Items.POTION.descriptionId + ".effect."
        add(potion.getName(prefix), translation)
    }

    fun addCreativeTab(tab: CreativeModeTab, translation: String) {
        //add("itemGroup.${ALoveLetter.MODID}.$tabName", translation)
        add(tab.displayName.string, translation)
    }



    fun addConfigCategory(section: AbstractConfigSection, nameTranslation: String, description: String) {
        val fullKey = "${AbstractConfigSection.CATEGORY_PREFIX}.${section.side}.${section.screenTranslationKey}"
        add(fullKey, nameTranslation)
        add("$fullKey.tooltip", description)
    }

    fun addOptionGroup(key: String, translation: String) {
        add("config.aloveletter.optiongroup.$key", translation)
    }


    fun addMusicDiscItem(item: RecordItem, nameTranslation: String, authors: String, songTitle: String ) {
        add(item.descriptionId, nameTranslation)
        add(item.descriptionId + ".desc", "$authors - $songTitle")
    }

}