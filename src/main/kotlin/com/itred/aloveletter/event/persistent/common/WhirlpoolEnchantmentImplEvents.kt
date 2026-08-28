package com.itred.aloveletter.event.persistent.common

import com.itred.aloveletter.ALoveLetter
import com.itred.aloveletter.entity.WhirlpoolEnchantmentAnchorEntity
import com.itred.aloveletter.registrar.ALLEnchantments
import com.itred.aloveletter.registrar.ALLEntities
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.projectile.ThrownTrident
import net.minecraft.world.item.ItemStack
import net.minecraftforge.event.entity.living.LivingDamageEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod


@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, modid = ALoveLetter.MODID)
object WhirlpoolEnchantmentImplEvents {

    const val TAG_PREFIX = "aloveletter.whirlpoolenchantment."

    const val ANCHOR_ID_KEY = TAG_PREFIX + "AnchorID"
    const val HAS_WHIRLPOOL_KEY = TAG_PREFIX + "HasWhirlpool"



    const val VELOCITY_CAP = 10.0



    // Called via WrapOperation mixin
    fun onTridentThrow(trident: ThrownTrident, tridentStack: ItemStack) {
        trident.persistentData.putBoolean(HAS_WHIRLPOOL_KEY, true)
        val anchorEntity = WhirlpoolEnchantmentAnchorEntity(ALLEntities.WHIRPLPOOL_ENCHANTMENT_ANCHOR, trident,trident.level())
        // We only have the item stack here, and it'd be a pain to send it over and store it, so we just set the pull time from the level here
        anchorEntity.setPullTimeFromLevel(
            tridentStack.getEnchantmentLevel(ALLEnchantments.WHIRLPOOL.get())
        )
        trident.level().addFreshEntity(anchorEntity)

        storeAnchor(trident, anchorEntity)
    }

    fun storeAnchor(trident: ThrownTrident, anchor: WhirlpoolEnchantmentAnchorEntity) {
        trident.persistentData.putUUID(ANCHOR_ID_KEY, anchor.uuid)
    }

    fun getAnchorIfExists(trident: ThrownTrident): WhirlpoolEnchantmentAnchorEntity? {
        if (trident.persistentData.hasUUID(ANCHOR_ID_KEY)) {
            return (trident.level() as ServerLevel).getEntity(trident.persistentData.getUUID(ANCHOR_ID_KEY)) as? WhirlpoolEnchantmentAnchorEntity?
        }
        return null
    }



    @SubscribeEvent
    fun onHit(event: LivingDamageEvent) {

        val target = event.entity
        val source = event.source

        val attacker = source.directEntity
        if (attacker is ThrownTrident && attacker.persistentData.getBoolean(HAS_WHIRLPOOL_KEY)) {
            val anchor = getAnchorIfExists(attacker)
            anchor?.setTarget(target)

        }

    }

}