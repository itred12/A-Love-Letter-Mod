package com.itred.aloveletter.entity

class JusticeBulletEntityOld {
    /*
    companion object {

        val DATA_BULLET_ITEM = SynchedEntityData.defineId(JusticeBulletEntityOld::class.java, EntityDataSerializers.ITEM_STACK)
        // 0 - 255, how many entities the bullet can pierce through before stopping
        val DATA_PIERCE_LEVEL = SynchedEntityData.defineId(JusticeBulletEntityOld::class.java, EntityDataSerializers.BYTE)

        val DRAG = 0.99f // Velocity is multiplied by this value every tick


    }

    var life: Int = 0
    // Contains every entity hit by this bullet, so each entity will be hit only once when performing hit detection
    val ignoredEntities = IntOpenHashSet()

    constructor(entity: EntityType<out JusticeBulletEntityOld>, level: Level) : super(entity, level) {

    }

    constructor(entityType: EntityType<out JusticeBulletEntityOld>, level: Level, x: Double, y: Double, z: Double, ammoStack: ItemStack, pierce: Byte?) : this(ALLEntities.JUSTICE_BULLET, level) {
        this.setPos(x, y, z)
        // Set data
        this.entityData.set(DATA_BULLET_ITEM, ammoStack.copy())
        if (pierce != null) {
            this.entityData.set(DATA_PIERCE_LEVEL, pierce)
        }
    }

    constructor(level: Level, shooter: LivingEntity, ammoStack: ItemStack, pierce: Byte) :
            this(ALLEntities.JUSTICE_BULLET, level, shooter.x, shooter.eyeY - 0.1, shooter.z, ammoStack, pierce) {
                this.owner = shooter
    }

    public override fun addAdditionalSaveData(pCompound: CompoundTag) {
        super.addAdditionalSaveData(pCompound)
        pCompound.putShort("life", this.life.toShort())
        pCompound.putByte("PierceLevel", this.getPierceLevel())

    }

    public override fun readAdditionalSaveData(pCompound: CompoundTag) {
        super.readAdditionalSaveData(pCompound)
        this.life = pCompound.getShort("life").toInt()
        this.setPierceLevel(pCompound.getByte("PierceLevel"))

    }

    fun setPierceLevel(pPierceLevel: Byte) {
        this.entityData.set(DATA_PIERCE_LEVEL, pPierceLevel)
    }

    fun getPierceLevel(): Byte {
        return this.entityData.get(DATA_PIERCE_LEVEL)
    }



    private fun tickDespawn() {
        ++this.life
        if (this.life >= 1200) {
            this.discard()
        }
    }

    private fun resetPiercedEntities() {
        if (this.piercedAndKilledEntities != null) {
            this.piercedAndKilledEntities.clear()
        }

        if (this.piercingIgnoreEntityIds != null) {
            this.piercingIgnoreEntityIds.clear()
        }
    }

    override fun defineSynchedData() {
        this.entityData.define(DATA_BULLET_ITEM, ItemStack.EMPTY)
        this.entityData.define(DATA_PIERCE_LEVEL, 0.toByte())
    }

    override fun getItem(): ItemStack? {
        val item = this.entityData.get(DATA_BULLET_ITEM)
        return if (!item.isEmpty) item else ItemStack(JusticeWeaponItem.DEFAULT_PROJECTILE_ITEM)
    }

    override fun onHitBlock(pResult: BlockHitResult?) {
        // Destroyed on block hit

        if (this.level().isClientSide) {
            this.level().addParticle(
                DustParticleOptions(
                    this.position().toVector3f(), 1.0f
                    ),
                this.x, this.y, this.z, 1.0, 1.0, 1.0
            )
        }

        this.discard()
    }

    // Modifiable function for grabbing an entity hit by an arrow. Grabbed from AbstractArrow.
    // Appears to cast a hitbox from the start and end vector, using the projectiles bounding box, and then filtering w/ a function
    // Pretty similar to Roblox's getPartsInPart!
    private fun findHitEntity(pStartVec: Vec3, pEndVec: Vec3): EntityHitResult? {
        return ProjectileUtil.getEntityHitResult(
            this.level(),
            this,
            pStartVec,
            pEndVec,
            this.boundingBox.expandTowards(this.deltaMovement).inflate(1.0),
            Predicate { entity: Entity? -> this.canHitEntity(entity) })
    }

    override fun canHitEntity(p_36743_: Entity): Boolean {
        return super.canHitEntity(p_36743_)
                && (this.piercingIgnoreEntityIds == null || !this.piercingIgnoreEntityIds.contains(
            p_36743_.getId()
        )) && !this.ignoredEntities.contains(p_36743_.getId())
    }

    override fun tick() {
        super.tick()

        val moveVec = this.deltaMovement

        this.move(MoverType.SELF, moveVec)
        this.deltaMovement = moveVec

        var inGround = false


        val blockpos = this.blockPosition()
        val blockstate = this.level().getBlockState(blockpos)
        if (!blockstate.isAir) {
            val voxelshape = blockstate.getCollisionShape(this.level(), blockpos)
            if (!voxelshape.isEmpty) {
                val vec31 = this.position()

                for (aabb in voxelshape.toAabbs()) {
                    if (aabb.move(blockpos).contains(vec31)) {
                        inGround = true
                        break
                    }
                }
            }
        }

        val position = this.position()
        // Our next position is our current position + the movement vector
        var projectedPosition = position.add(moveVec)

        // So THIS is how you do hitboxes!
        var hitresult: HitResult? = this.level().clip(
            ClipContext(
                position,
                projectedPosition,
                ClipContext.Block.COLLIDER,
                ClipContext.Fluid.NONE,
                this
            )
        )

        // If we ended up hitting something, snap our projected position to wherever we hit
        if (hitresult!!.type != HitResult.Type.MISS) {
            projectedPosition = hitresult.getLocation()
        }

        while (!this.isRemoved) {
            // Look for any entities crossing our path
            var entityhitresult: EntityHitResult? = this.findHitEntity(position, projectedPosition)

            // If we end up finding one, overwrite the level clip hit result with our entity-based one(?)
            if (entityhitresult != null) {
                hitresult = entityhitresult
            }

            // If we ended up hitting something, and that something is indeed an entity,
            // do onnneee more check to make sure the victim can be harmed by the attacker (i.e., two players whom are not on the same team)
            if (hitresult != null && hitresult.type == HitResult.Type.ENTITY) {
                val victim = (hitresult as EntityHitResult).entity
                val owner = this.owner

                if (victim is Player && owner is Player && !owner.canHarmPlayer(victim)) {
                    hitresult = null
                    entityhitresult = null
                }
            }


            // Biggol Neoforge compat chunk
            if (hitresult != null && hitresult.type != HitResult.Type.MISS) {
                // Let other mods in here with this event
                when (ForgeEventFactory.onProjectileImpactResult(this, hitresult)) {

                    // If skipping the entity,
                    ImpactResult.SKIP_ENTITY -> {
                        if (hitresult.type != HitResult.Type.ENTITY) { // If there is no entity, we just return default behaviour
                            this.onHit(hitresult)
                            this.hasImpulse = true
                            break
                        }
                        // Ignore it as if hit, but nullify the hit result
                        ignoredEntities.add(entityhitresult!!.entity.id)
                        entityhitresult = null
                    }

                    // (i.e., being blocked by a shield(? though it just destroys it...))
                    ImpactResult.STOP_AT_CURRENT_NO_DAMAGE -> {
                        this.discard()
                        entityhitresult = null // Don't process any further
                    }

                    ImpactResult.STOP_AT_CURRENT -> {
                        this.setPierceLevel(0.toByte())
                        this.onHit(hitresult)
                        this.hasImpulse = true
                    }

                    ImpactResult.DEFAULT -> {
                        this.onHit(hitresult)
                        this.hasImpulse = true
                    }
                }
            }

            // If we didnt hit an entity, or we're at 0 pierce, break the hitbox loop
            if (entityhitresult == null || this.getPierceLevel() <= 0) {
                break
            }
            hitresult = null
        }

        // If the above loop was broken due to the entity being discarded, do nothing else
        if (this.isRemoved) {
            return
        }

        // Now we move the bullet
        this.deltaMovement = moveVec.scale(DRAG.toDouble())
        // If we're affected by gravity, bring down the y velocity more than the other two
        if (!this.isNoGravity) {
            val newDeltaMovement = this.deltaMovement
            this.setDeltaMovement(newDeltaMovement.x, newDeltaMovement.y - 0.05, newDeltaMovement.z)
        }

        this.setPos(position.x + moveVec.x, position.y + moveVec.y, position.z + moveVec.z)
        this.checkInsideBlocks()

    }
    */
}