package com.itred.aloveletter.event.persistent.common

import com.itred.aloveletter.ALoveLetter
import com.itred.aloveletter.item.DisceryItem
import net.minecraftforge.event.entity.item.ItemTossEvent
import net.minecraftforge.event.entity.living.LivingDeathEvent
import net.minecraftforge.event.entity.living.LivingHurtEvent
import net.minecraftforge.event.entity.player.PlayerEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, modid = ALoveLetter.MODID)
object DisceryEvents {

    @SubscribeEvent
    fun onItemDrop(event: ItemTossEvent) {

        val itemEntity = event.entity
        val player = event.player

        if (itemEntity.item.item is DisceryItem) {

            DisceryItem.playRandomSoundInListWithRandomPitch(
                DisceryItem.DROPPED_SOUNDS,
                itemEntity,
                player.random,
                0.5f
            )

        }

    }


    @SubscribeEvent
    fun onItemPickup(event: PlayerEvent.ItemPickupEvent) {

        val itemStack = event.stack
        val player = event.entity

        if (itemStack.item is DisceryItem) {

            itemStack.orCreateTag.putLong(DisceryItem.TAG_KEY_LASTPICKUP, player.level().gameTime)

            DisceryItem.playRandomSoundInListWithRandomPitchOnServer(
                DisceryItem.PICKUP_SOUNDS, player.level(), player.position(), player.random
            )


        }

    }

    @SubscribeEvent
    fun onKill(event: LivingDeathEvent) {

        val attacker = event.source.entity
        if (attacker != null && attacker.handSlots.any { stack -> stack.item is DisceryItem }) {
            DisceryItem.playRandomSoundInListWithRandomPitchOnServer(DisceryItem.KILL_SOUNDS, attacker.level(), attacker.position(), event.entity.random)
        }

    }


    @SubscribeEvent
    fun onHit(event: LivingHurtEvent) {

        val attacker = event.source.entity
        if (attacker != null && attacker.handSlots.any { stack -> stack.item is DisceryItem }) {
            DisceryItem.playRandomSoundInListWithRandomPitchOnServer(DisceryItem.HURT_ENEMY_SOUNDS, attacker.level(), attacker.position(), event.entity.random)
        }

    }

}