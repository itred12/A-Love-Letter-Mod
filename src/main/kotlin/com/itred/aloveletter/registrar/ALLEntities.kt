package com.itred.aloveletter.registrar

import com.itred.aloveletter.entity.JusticeBulletEntity
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.MobCategory
import net.minecraftforge.registries.ForgeRegistries
import thedarkcolour.kotlinforforge.forge.ObjectHolderDelegate
import thedarkcolour.kotlinforforge.forge.registerObject

object ALLEntities : AbstractRegistrar<EntityType<*>>(ForgeRegistries.ENTITY_TYPES) {

    val JUSTICE_BULLET by registerEntityType<JusticeBulletEntity>("justice_bullet",
        EntityType.Builder.of(::JusticeBulletEntity, MobCategory.MISC)
            .sized(0.25F, 0.25F)
            .clientTrackingRange(4)
            .updateInterval(10)
    )


    private fun <T: Entity> registerEntityType(key: String, builder: EntityType.Builder<T>): ObjectHolderDelegate<EntityType<T>> {
        return registry.registerObject( key) { builder.build(key) }
    }



}