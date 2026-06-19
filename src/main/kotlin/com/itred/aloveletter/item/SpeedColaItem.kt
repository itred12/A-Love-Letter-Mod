package com.itred.aloveletter.item

import com.itred.aloveletter.registrar.ALLStatusEffects
import net.minecraft.advancements.CriteriaTriggers
import net.minecraft.server.level.ServerPlayer
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundEvents
import net.minecraft.stats.Stats
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.*
import net.minecraft.world.level.Level
import net.minecraft.world.level.gameevent.GameEvent
import kotlin.math.min

class SpeedColaItem(properties: Properties) : Item(properties) {

    private val STACK_LIMIT = 3
    private val DURATION = 4 * 60 // in seconds
    private val BYPRODUCT_ITEM = Items.GLASS_BOTTLE // Produced after consuming

    override fun getUseDuration(pStack: ItemStack?): Int {
        return 32
    }

    override fun getUseAnimation(pStack: ItemStack?): UseAnim? {
        return UseAnim.DRINK
    }

    override fun finishUsingItem(pStack: ItemStack?, pLevel: Level?, pLivingEntity: LivingEntity?): ItemStack? {

        if (pStack == null || pLivingEntity == null || pLevel == null) {
            return pStack
        }

        // Stack the effect
        val effect = pLivingEntity.getEffect(ALLStatusEffects.HYPERACTIVE)
        if (effect == null) {
            pLivingEntity.addEffect(
                MobEffectInstance(
                    ALLStatusEffects.HYPERACTIVE,
                    DURATION * 20,
                    0
                )
            )
        } else {
            pLivingEntity.addEffect(
                MobEffectInstance(
                    ALLStatusEffects.HYPERACTIVE,
                    DURATION * 20,
                    min(STACK_LIMIT - 1, effect.amplifier + 1) // Amplifier is index-0, so we subtract 1 from the limit to get the *actual* limit
                )
            )
        }


        // Following along with the same "use checklist" that PotionItem uses

        // If we're on the server side, trigger the advancement criteria
        if (pLivingEntity is ServerPlayer) {
            CriteriaTriggers.CONSUME_ITEM.trigger(pLivingEntity, pStack)
        }

        // If we're a player, increment the "used item" stat and shrink the stack if we aren't in creative mode
        if (pLivingEntity is Player) {
            pLivingEntity.awardStat(Stats.ITEM_USED.get(this))
        }

        if (pLivingEntity !is Player || !pLivingEntity.abilities.instabuild) {
            pStack.shrink(1)

            // If there's nothing left in the stack after shrinking it, we can return the remaining item as-is
            if (pStack.isEmpty) {
                return ItemStack(BYPRODUCT_ITEM)
            }

            // Otherwise, we have to give the item to the entity's inventory
            if (pLivingEntity is Player) {
                pLivingEntity.inventory.add(ItemStack(BYPRODUCT_ITEM))
            }
        }

        pLivingEntity.gameEvent(GameEvent.DRINK)
        return pStack


    }



    override fun use(
        pLevel: Level?,
        pPlayer: Player?,
        pUsedHand: InteractionHand?
    ): InteractionResultHolder<ItemStack?>? {
        return ItemUtils.startUsingInstantly(pLevel, pPlayer, pUsedHand)
    }

    override fun getEatingSound(): SoundEvent? {
        return SoundEvents.HONEY_DRINK
    }

    override fun getDrinkingSound(): SoundEvent? {
        return SoundEvents.HONEY_DRINK
    }

}