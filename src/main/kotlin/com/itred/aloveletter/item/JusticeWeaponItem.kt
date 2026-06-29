package com.itred.aloveletter.item

import com.itred.aloveletter.entity.JusticeBulletEntity
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.ListTag
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.*
import net.minecraft.world.level.Level
import org.joml.Quaternionf
import java.util.function.Predicate
import kotlin.math.roundToInt

class JusticeWeaponItem(properties: Properties) : ProjectileWeaponItem(properties) {



    companion object {

        const val MAX_AMMO_COUNT = 6
        val TIME_TO_LOAD = (0.5 * 20) // Ticks
            .roundToInt()

        const val TAG_AMMO = "Ammunition" // List of ammo holders, fired from index 6 for now
        const val TAG_ISLOADED = "Loaded" // Boolean

        val DEFAULT_PROJECTILE_ITEM = Items.ARROW // Used only for creative mode players for now
    }




    override fun getAllSupportedProjectiles(): Predicate<ItemStack?> {
        return Predicate { itemStack ->
            itemStack?.item is ArrowItem
        }
    }

    override fun getDefaultProjectileRange(): Int {
        return 8
    }

    override fun use(
        pLevel: Level,
        pPlayer: Player,
        pUsedHand: InteractionHand
    ): InteractionResultHolder<ItemStack> {

        val weaponStack = pPlayer.getItemInHand(pUsedHand)



        // If crouching, try to load ammo if possible
        if (pPlayer.isCrouching) {

            if (NBTHelpers.canLoadAmmo(weaponStack) && !pPlayer.getProjectile(weaponStack).isEmpty) {
                pPlayer.startUsingItem(pUsedHand)
                return InteractionResultHolder.consume(weaponStack)
            } else {
                return InteractionResultHolder.fail(weaponStack)
            }

        } else {

            if (NBTHelpers.isLoaded(weaponStack)) {

                val ammo = NBTHelpers.popAmmo(weaponStack)

                spawnAndFireBullet(pPlayer, pLevel, weaponStack, ammo)

                if (pLevel.isClientSide) {
                    pLevel.playSound(pPlayer, pPlayer.blockPosition(), SoundEvents.CROSSBOW_SHOOT, SoundSource.MASTER, 1.0f, 1.0f)
                }

                if (NBTHelpers.getNBTProjectileCount(weaponStack) == 0) {
                    NBTHelpers.setLoaded(weaponStack, false)
                }


                return InteractionResultHolder.consume(weaponStack)
            }

            return InteractionResultHolder.fail(weaponStack)
        }


    }


    override fun finishUsingItem(pStack: ItemStack, pLevel: Level, pLivingEntity: LivingEntity): ItemStack {
        if (loadAmmoFromInventory(pLivingEntity, pStack) && pLevel.isClientSide) {
            pLevel.playSound(pLivingEntity, pLivingEntity.blockPosition(), SoundEvents.LEVER_CLICK, SoundSource.MASTER, 0.5f, 1.0f)
        }
        return pStack
    }


    override fun getUseDuration(pStack: ItemStack?): Int {
        return TIME_TO_LOAD + 3
    }


    private fun  loadAmmoFromInventory(user: LivingEntity, weaponStack: ItemStack): Boolean {

        val isCreative = user is Player && user.abilities.instabuild

        if (!NBTHelpers.canLoadAmmo(weaponStack)) {
            return false
        }

        var ammoStack = user.getProjectile(weaponStack)
        if (ammoStack.isEmpty && isCreative) {
            ammoStack = ItemStack(DEFAULT_PROJECTILE_ITEM)
        }

        return tryLoadAmmo(user, weaponStack, ammoStack, isCreative)

    }




    private fun tryLoadAmmo(user: LivingEntity, weaponStack: ItemStack, ammoStack: ItemStack, isCreative: Boolean): Boolean {

        if (ammoStack.isEmpty) {
            return false
        }

        // Whether the stack is the "fake" stack given by default for creative players whom load it without ammo
        // This means stuff like tipped arrows are still used but we don't attempt to decrement the "fake" stack given freely to creative players
        val isFakeStack = isCreative && ammoStack.item == DEFAULT_PROJECTILE_ITEM

        var pickedAmmo: ItemStack
        if (!isFakeStack && !isCreative) {

            pickedAmmo = ammoStack.split(1)
            // Only can really remove items from entities with an inventory, such as players
            if (ammoStack.isEmpty && user is Player) {
                user.inventory.removeItem(ammoStack)
            }

        } else {
            pickedAmmo = ammoStack.copy()
        }

        NBTHelpers.nbtLoadProjectile(weaponStack, pickedAmmo)

        if (!NBTHelpers.isLoaded(weaponStack)) {
            NBTHelpers.setLoaded(weaponStack, true)
        }

        return true

    }

    private fun spawnAndFireBullet(user: LivingEntity, level: Level, weaponStack: ItemStack, bulletStack: ItemStack) {

        val bullet = JusticeBulletEntity(level, user, bulletStack, 5)

        val userUpVec = user.getUpVector(1.0F)
        val aimQuaterion = (Quaternionf().setAngleAxis(1.0, userUpVec.x, userUpVec.y, userUpVec.z))
        val userViewVec = user.getViewVector(1.0F)

        val aimVec3 = userViewVec.toVector3f().rotate(aimQuaterion)
        bullet.shoot(userViewVec.x, userViewVec.y, userViewVec.z, 10.0F, 0.0F)

        level.addFreshEntity(bullet)
    }



    override fun getUseAnimation(pStack: ItemStack?): UseAnim? {
        return UseAnim.CROSSBOW
    }



    private object NBTHelpers {

        /** Loads a projectile into the stack's ammo tag, ignoring any and all limitations */
        @JvmStatic
        fun nbtLoadProjectile(gunStack: ItemStack, ammoStack: ItemStack) {
            val stackTag = gunStack.orCreateTag

            val loadedAmmoList = if (stackTag.contains(TAG_AMMO, 9)) stackTag.getList(TAG_AMMO, 10) else ListTag()

            // Translate the ammo stack into NBT
            val ammoTag = CompoundTag()
            ammoStack.save(ammoTag)
            loadedAmmoList.add(ammoTag)

            stackTag.put(TAG_AMMO, loadedAmmoList)
        }

        /** Returns a list of all ammo itemstacks in the gun */
        @JvmStatic
        fun nbtGetProjectileList(gunStack: ItemStack): List<ItemStack> {
            val gatheredAmmoList = mutableListOf<ItemStack>()

            val stackTag = gunStack.tag

            if (stackTag != null && stackTag.contains(TAG_AMMO, 9)) {

                // Iterate through elements to add them to the list
                val ammoInGun = stackTag.getList(TAG_AMMO, 10)
                for (i in 0..<ammoInGun.size) {
                    val ammoStackAsTag = ammoInGun.getCompound(i)
                    gatheredAmmoList.add(ItemStack.of(ammoStackAsTag))
                }

            }

            return gatheredAmmoList
        }

        /** Returns the count of all projectiles loaded in the gun.
         *
         * Slightly more efficient than calling nbtGetProjectileList().size */
        @JvmStatic
        fun getNBTProjectileCount(gunStack: ItemStack): Int {
            val stackTag = gunStack.tag

            return if (stackTag != null && stackTag.contains(TAG_AMMO, 9)) {
                // Instead of iterating through this list to turn it all into item stacks, just grab its element count
                stackTag.getList(TAG_AMMO, 10).size
            } else {
                0
            }
        }

        @JvmStatic
        fun isLoaded(gunStack: ItemStack): Boolean {
            val stackTag = gunStack.tag
            return (stackTag != null && stackTag.contains(TAG_ISLOADED) && stackTag.getBoolean(TAG_ISLOADED))
        }

        @JvmStatic
        fun setLoaded(gunStack: ItemStack, value: Boolean) {
            val stackTag = gunStack.orCreateTag
            stackTag.putBoolean(TAG_ISLOADED, value)
        }

        @JvmStatic
        fun canLoadAmmo(gunStack: ItemStack): Boolean {
            return getNBTProjectileCount(gunStack) < MAX_AMMO_COUNT
        }

        /** Removes the ammo at the given index and returns it. Stack may be empty if the weapon has no ammo.  */
        @JvmStatic
        fun popAmmo(gunStack: ItemStack, index: Int = -1): ItemStack {
            val stackTag = gunStack.tag

            var ammoStack = ItemStack.EMPTY

            if (stackTag != null && stackTag.contains(TAG_AMMO, 9)) {



                val ammoTag = stackTag.getList(TAG_AMMO, 10)
                val removedAmmo = ammoTag.getCompound(
                    // Investigated, and otherwise -1 index would indeed throw an index-out-of-bounds exception
                    if (index < 0) (ammoTag.size - 1) else index
                )


                ammoTag.remove(removedAmmo)

                // Re-store the modified list of ammo
                stackTag.put(TAG_AMMO, ammoTag)

                ammoStack = ItemStack.of(removedAmmo)

            }

            return ammoStack
        }

    }

}