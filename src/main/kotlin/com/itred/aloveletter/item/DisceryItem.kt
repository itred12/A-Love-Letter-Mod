package com.itred.aloveletter.item

import com.itred.aloveletter.ALoveLetter
import com.itred.aloveletter.registrar.ALLSounds
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundSource
import net.minecraft.util.RandomSource
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.Entity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.RecordItem
import net.minecraft.world.item.context.UseOnContext
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.JukeboxBlock
import net.minecraft.world.phys.Vec3
import java.util.function.Supplier

class DisceryItem(comparatorValue: Int, soundSupplier: Supplier<SoundEvent>, builder: Properties, lengthInTicks: Int)
    : RecordItem(comparatorValue, soundSupplier, builder, lengthInTicks) {


    companion object {

        const val TAG_KEY_LASTPICKUP: String = "aloveletter.last_pickup"
        const val TAG_KEY_HELDBEOFRE: String = "aloveletter.was_selected_before"

        const val PICKUP_VOICECLIP_DELAY: Int = 100

        val DROPPED_SOUNDS = arrayOf(
            ALLSounds.FLOWERY_VOICECLIP_IMFALLING,
            ALLSounds.FLOWERY_VOICECLIP_GOODBYE
        )

        val PICKUP_SOUNDS = arrayOf(
            ALLSounds.FLOWERY_VOICECLIP_HEYGUYS,
            ALLSounds.FLOWERY_VOICECLIP_SORRYTOKEEPYOUWAITING,
            ALLSounds.FLOWERY_VOICECLIP_FLOWERY,
            ALLSounds.FLOWERY_VOICECLIP_ITSMEFLOWERY
        )

        val SELECT_SOUNDS = arrayOf(
            ALLSounds.FLOWERY_VOICECLIP_LEAFITTOME,
            ALLSounds.FLOWERY_VOICECLIP_FLOWERY,
            ALLSounds.FLOWERY_VOICECLIP_SUSTINGUS,
            ALLSounds.FLOWERY_VOICECLIP_YES,
        )

        val HURT_ENEMY_SOUNDS = arrayOf(
            ALLSounds.FLOWERY_VOICECLIP_JARONA,
            ALLSounds.FLOWERY_VOICECLIP_JARONA,
            ALLSounds.FLOWERY_VOICECLIP_HOO,
            ALLSounds.FLOWERY_VOICECLIP_HA
        )

        val KILL_SOUNDS = arrayOf(
            ALLSounds.FLOWERY_VOICECLIP_HEHITSMYJARONA,
            ALLSounds.FLOWERY_VOICECLIP_ALLACCORDINGTOPLANT,
            ALLSounds.FLOWERY_VOICECLIP_WHATAPREDICTABLECREATURE,
            ALLSounds.FLOWERY_VOICECLIP_TAKETHAT,
            ALLSounds.FLOWERY_VOICECLIP_GETACHANCE,
            ALLSounds.FLOWERY_VOICECLIP_SORRYABOUTTHATGUYS
        )

        val INSERT_JUKEBOX_SOUNDS = arrayOf(
            ALLSounds.FLOWERY_VOICECLIP_LENDMEYOURPOWER,
            ALLSounds.FLOWERY_VOICECLIP_HEREICOMESANFRANDISC,
            ALLSounds.FLOWERY_VOICECLIP_ONEMOREFORTHEFANS
        )

        val REMOVE_JUKEBOX_SOUNDS = arrayOf(
            ALLSounds.FLOWERY_VOICECLIP_MYKING,
            ALLSounds.FLOWERY_VOICECLIP_HEHITSMYJARONA,
            ALLSounds.FLOWERY_VOICECLIP_SORRYTOKEEPALADYINWAITING
        )




        fun playRandomSoundInListWithRandomPitch(list: Array<Supplier<SoundEvent>>, entity: Entity, randomSource: RandomSource, volume: Float = 1f) {

            val sound = list[
                randomSource.nextInt(list.size)
            ]

            entity.playSound(
                sound.get(),
                volume,
                randomSource.nextInt(95, 105).toFloat() / 100f
            )
        }

        fun playRandomSoundInListWithRandomPitchOnServer(list: Array<Supplier<SoundEvent>>, level: Level, position: Vec3, randomSource: RandomSource, volume: Float = 1f) {

            val sound = list[
                randomSource.nextInt(list.size)
            ]

            ALoveLetter.LOGGER.info(sound.get().location)

            level.playSeededSound(
                null,
                position.x,
                position.y,
                position.z,
                sound.get(),
                SoundSource.RECORDS,
                volume,
                randomSource.nextInt(95, 105).toFloat() / 100f,
                randomSource.nextLong()
            )

        }

    }


    override fun useOn(pContext: UseOnContext): InteractionResult? {

        val level = pContext.level
        val block = level.getBlockState(pContext.clickedPos)
        val player = pContext.player

        if (block.block is JukeboxBlock && !block.getValue(JukeboxBlock.HAS_RECORD)) {
            playRandomSoundInListWithRandomPitchOnServer(INSERT_JUKEBOX_SOUNDS, level, pContext.clickLocation, level.random)
        }


        return super.useOn(pContext)
    }


    override fun inventoryTick(
        pStack: ItemStack,
        pLevel: Level,
        pEntity: Entity,
        pSlotId: Int,
        pIsSelected: Boolean
    ) {
        super.inventoryTick(pStack, pLevel, pEntity, pSlotId, pIsSelected)

        // Only run once every three ticks
        if (pLevel.gameTime % 3 != 0L) {
            return
        }


        // Get the compound tag and look for some other things we're storing
        val stackTags = pStack.orCreateTag

        // Grab the last pickup tag,
        val lastPickupTime = stackTags.getLong(TAG_KEY_LASTPICKUP)

        // If it's greater than the current game time (only occurs after world restart), then we cant use it, so we should reset it
        if (lastPickupTime > pLevel.gameTime) {
            stackTags.remove(TAG_KEY_LASTPICKUP)
        }


        // If at least 100 ticks have passed since it was picked up (to give the pickup voiceclip some time to play)
        if (pLevel.gameTime - lastPickupTime > 100) {

            // If it isn't selected, reset the selected flag
            if (!pIsSelected) {
                stackTags.putBoolean(TAG_KEY_HELDBEOFRE, false)
            } else if (!stackTags.getBoolean(TAG_KEY_HELDBEOFRE)) {
                // if it is selected, and it hasn't been selected on the previous tick (so this only plays once, the instant it's selected), play the "selected" sounds
                stackTags.putBoolean(TAG_KEY_HELDBEOFRE, true)
                playRandomSoundInListWithRandomPitch(SELECT_SOUNDS, pEntity, pLevel.random)

            }


        }

    }




}