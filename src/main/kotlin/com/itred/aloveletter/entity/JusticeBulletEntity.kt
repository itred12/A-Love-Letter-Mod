package com.itred.aloveletter.entity

import com.itred.aloveletter.item.JusticeWeaponItem
import com.itred.aloveletter.registrar.ALLEntities
import net.minecraft.network.syncher.EntityDataSerializers
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.MoverType
import net.minecraft.world.entity.projectile.ItemSupplier
import net.minecraft.world.entity.projectile.Projectile
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

class JusticeBulletEntity : Projectile, ItemSupplier {

    companion object {
        val DATA_BULLET_ITEM = SynchedEntityData.defineId(JusticeBulletEntity::class.java, EntityDataSerializers.ITEM_STACK)
    }

    constructor(entity: EntityType<out JusticeBulletEntity>, level: Level) : super(entity, level) {

    }

    constructor(entityType: EntityType<out JusticeBulletEntity>, level: Level, x: Double, y: Double, z: Double, ammoStack: ItemStack) : this(ALLEntities.JUSTICE_BULLET, level) {
        this.setPos(x, y, z)
        this.entityData.set(DATA_BULLET_ITEM, ammoStack.copy())
    }

    constructor(level: Level, shooter: LivingEntity, ammoStack: ItemStack) :
            this(ALLEntities.JUSTICE_BULLET, level, shooter.x, shooter.eyeY - 0.1, shooter.z, ammoStack) {
                this.owner = shooter
    }

    override fun tick() {
        super.tick()
        val moveVec = this.deltaMovement
        this.move(MoverType.SELF, moveVec)
        this.deltaMovement = moveVec
    }

    override fun defineSynchedData() {
        this.entityData.define(DATA_BULLET_ITEM, ItemStack.EMPTY)
    }

    override fun getItem(): ItemStack? {
        val item = this.entityData.get(DATA_BULLET_ITEM)
        return if (!item.isEmpty) item else ItemStack(JusticeWeaponItem.DEFAULT_PROJECTILE_ITEM)
    }
}