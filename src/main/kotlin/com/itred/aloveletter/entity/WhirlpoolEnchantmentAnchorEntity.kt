package com.itred.aloveletter.entity

import com.itred.aloveletter.ALoveLetter
import net.minecraft.commands.arguments.EntityAnchorArgument
import net.minecraft.nbt.CompoundTag
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.TraceableEntity
import net.minecraft.world.entity.projectile.ThrownTrident
import net.minecraft.world.level.Level
import net.minecraft.world.phys.Vec3
import java.util.*
import kotlin.math.abs
import kotlin.math.min

class WhirlpoolEnchantmentAnchorEntity : Entity, TraceableEntity {

    companion object {
        const val MAX_VELOCITY = 10
        const val PULL_TIME_PER_LEVEL = 15 // in ticks
    }

    private var ownerUUID: UUID? = null
    private var cachedOwner: Entity? = null

    private var targetUUID: UUID? = null
    private var cachedTarget: Entity? = null

    private var life = 1200 // ticks until despawn
    var pullTime = PULL_TIME_PER_LEVEL // ticks until despawn

    constructor(entityType: EntityType<out WhirlpoolEnchantmentAnchorEntity>, level: Level) : super(entityType, level)

    constructor(entityType: EntityType<out WhirlpoolEnchantmentAnchorEntity>, position: Vec3, level: Level) : this(entityType, level) {
        this.setPos(position)
    }

    constructor(entityType: EntityType<out WhirlpoolEnchantmentAnchorEntity>, owner: ThrownTrident, level: Level) : this(entityType, owner.position(), level) {
        this.setOwner(owner)
    }


    override fun defineSynchedData() {}


    override fun tick() {
        super.tick()

        if (this.level().isClientSide) {
            return
        }

        val ownerTrident = this.getOwner()
        if (ownerTrident != null) {
            this.lookAt(EntityAnchorArgument.Anchor.EYES, ownerTrident.position())
            if (ownerTrident.onGround()) {
                this.tickDespawn()
            }

        } else {
            ALoveLetter.LOGGER.info("Despawning from no trident!")
            this.discard()
        }

        val targetEntity = this.getTarget()
        if (targetEntity != null) {
            tickPullTimer()

            val deltaV = targetEntity.position().vectorTo(this.position())
                .normalize()
                .scale(
                    min(0.5, abs(targetEntity.position().length() - this.position().length()))

                )

            targetEntity.setDeltaMovement(deltaV)
        }
    }


    fun tickDespawn() {
        --this.life
        if (this.life <= 0) {
            ALoveLetter.LOGGER.info("Despawning from despawn timer!")
            this.discard()
        }
    }

    fun tickPullTimer() {
        --this.pullTime
        if (this.pullTime <= 0) {
            ALoveLetter.LOGGER.info("Despawning from pull timer!")
            this.discard()
        }
    }

    fun setPullTimeFromLevel(whirlpoolEnchantmentLevel: Int) {
        this.pullTime = PULL_TIME_PER_LEVEL * whirlpoolEnchantmentLevel
    }


    // Basing this off of how Projectile.class stores entities
    fun setOwner(entity: ThrownTrident?) {
        if (entity != null) {
            this.ownerUUID = entity.uuid
            this.cachedOwner = entity
        }
    }

    fun setTarget(entity: Entity?) {
        if (entity != null) {
            this.targetUUID = entity.uuid
            this.cachedTarget = entity
        }
    }

    override fun getOwner(): Entity? {
        if (this.cachedOwner != null && !this.cachedOwner!!.isRemoved) {
            return this.cachedOwner
        } else if (this.ownerUUID != null && this.level() is ServerLevel) {
            this.cachedOwner = (this.level() as ServerLevel).getEntity(this.ownerUUID)
            return this.cachedOwner
        } else {
            return null
        }
    }

    fun getTarget(): Entity? {
        if (this.cachedTarget != null && !this.cachedTarget!!.isRemoved) {
            return this.cachedTarget
        } else if (this.targetUUID != null && this.level() is ServerLevel) {
            this.cachedTarget = (this.level() as ServerLevel).getEntity(this.targetUUID)
            return this.cachedTarget
        } else {
            return null
        }
    }


    fun ownedBy(entity: ThrownTrident): Boolean {
        return entity.uuid.equals(this.ownerUUID)
    }

    fun targeting(entity: ThrownTrident): Boolean {
        return entity.uuid.equals(this.targetUUID)
    }


    override fun readAdditionalSaveData(pCompound: CompoundTag) {
        if (pCompound.hasUUID("OwnerID")) {
            this.ownerUUID = pCompound.getUUID("OwnerID")
            this.cachedOwner = null
        }
        if (pCompound.hasUUID("TargetID")) {
            this.targetUUID = pCompound.getUUID("TargetID")
            this.cachedTarget = null
        }
    }

    override fun addAdditionalSaveData(pCompound: CompoundTag) {
        if (this.ownerUUID != null) {
            pCompound.putUUID("OwnerID", this.ownerUUID)
        }
        if (this.targetUUID != null) {
            pCompound.putUUID("TargetID", this.targetUUID)
        }
    }




}