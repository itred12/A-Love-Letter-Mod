package com.itred.aloveletter.registrar

import com.itred.aloveletter.entity.JusticeBulletEntity
import com.itred.aloveletter.entity.WhirlpoolEnchantmentAnchorEntity
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.MobCategory
import net.minecraftforge.registries.ForgeRegistries
import thedarkcolour.kotlinforforge.forge.ObjectHolderDelegate
import thedarkcolour.kotlinforforge.forge.registerObject

object ALLEntities : AbstractRegistrar<EntityType<*>>(ForgeRegistries.ENTITY_TYPES) {

    val JUSTICE_BULLET by registerEntityType<JusticeBulletEntity>("justice_bullet",
        EntityType.Builder.of(::JusticeBulletEntity, MobCategory.MISC)
            .sized(0.5F, 0.5F)
            .clientTrackingRange(4)
            .updateInterval(20)
    )

    val WHIRPLPOOL_ENCHANTMENT_ANCHOR by registerEntityType<WhirlpoolEnchantmentAnchorEntity>(
        "whirlpool_enchantment_anchor",
        EntityType.Builder.of(::WhirlpoolEnchantmentAnchorEntity, MobCategory.MISC)
            .sized(1.0f, 1.0f)
            .clientTrackingRange(8)
            .updateInterval(2   )
    )


    private fun <T: Entity> registerEntityType(key: String, builder: EntityType.Builder<T>): ObjectHolderDelegate<EntityType<T>> {
        return registry.registerObject( key) { builder.build(key) }
    }



}