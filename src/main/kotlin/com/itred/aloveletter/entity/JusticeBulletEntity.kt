package com.itred.aloveletter.entity

import com.itred.aloveletter.item.JusticeWeaponItem
import com.itred.aloveletter.registrar.ALLEntities
import net.minecraft.core.particles.BlockParticleOption
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.network.syncher.EntityDataAccessor
import net.minecraft.network.syncher.EntityDataSerializers
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.projectile.AbstractArrow
import net.minecraft.world.entity.projectile.ItemSupplier
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.phys.BlockHitResult

class JusticeBulletEntity : AbstractArrow, ItemSupplier {

    companion object {
        val DATA_BULLET_ITEM: EntityDataAccessor<ItemStack> = SynchedEntityData.defineId(JusticeBulletEntity::class.java, EntityDataSerializers.ITEM_STACK)

        // Easier to modify at runtime for testing
        // TODO: Revert this to a constant once we settle on a value
        fun getGravity(): Double {
            return 0.01
        }

    }

    var life: Int = 0

    constructor(entity: EntityType<out JusticeBulletEntity>, level: Level) : super(entity, level) {

    }

    constructor(entityType: EntityType<out JusticeBulletEntity>, level: Level, x: Double, y: Double, z: Double, ammoStack: ItemStack, pierce: Byte?) : this(ALLEntities.JUSTICE_BULLET, level) {
        this.setPos(x, y, z)
        // Set data
        this.entityData.set(DATA_BULLET_ITEM, ammoStack.copy())
        if (pierce != null) {
            this.pierceLevel = pierce
        }
    }

    constructor(level: Level, shooter: LivingEntity, ammoStack: ItemStack, pierce: Byte) :
            this(ALLEntities.JUSTICE_BULLET, level, shooter.x, shooter.eyeY - 0.1, shooter.z, ammoStack, pierce) {
        this.owner = shooter
    }
    

    // Never can stick in the ground for now
    override fun getPickupItem(): ItemStack? {
        return ItemStack.EMPTY
    }

    override fun tick() {
        super.tick()

        /*
        if (!this.inGround) {

            // Inverse of the gravity added at the end of AbstractArrow$tick()
            val movementVec = this.deltaMovement.add(0.0, 0.05, 0.0)

            // Now we can add our own (hopefully?)
            this.deltaMovement = movementVec.add(0.0, -getGravity(), 0.0)

        }
         */


    }
    
    override fun defineSynchedData() {
        super.defineSynchedData()
        this.entityData.define(DATA_BULLET_ITEM, ItemStack.EMPTY)
    }

    override fun getItem(): ItemStack? {
        val item = this.entityData.get(DATA_BULLET_ITEM)
        return if (!item.isEmpty) item else ItemStack(JusticeWeaponItem.DEFAULT_PROJECTILE_ITEM)
    }


    override fun onHitBlock(pResult: BlockHitResult) {
        // Destroyed on block hit
        super.onHitBlock(pResult)

        val position = pResult.location

        for (i in 0..<4) {
            this.level().addParticle(
                BlockParticleOption(ParticleTypes.BLOCK, Blocks.AMETHYST_BLOCK.defaultBlockState()),
                position.x, position.y, position.z,
                (random.nextInt(10) - 5).toDouble(), // -5 - 5
                (random.nextInt(2) + 4).toDouble(),  // 2 - 4
                (random.nextInt(10) - 5).toDouble() // -5 - 5
            )
        }

    }


    override fun canHitEntity(p_36743_: Entity): Boolean {
       return super.canHitEntity(p_36743_)
    }

    override fun tickDespawn() {
        life++
        if (life > 0) {
            this.discard()
        }
    }
}