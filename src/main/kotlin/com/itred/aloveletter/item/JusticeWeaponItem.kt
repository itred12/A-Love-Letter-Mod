package com.itred.aloveletter.item

import com.itred.aloveletter.ALoveLetter
import com.itred.aloveletter.entity.JusticeBulletEntity
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.ListTag
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ArrowItem
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.ProjectileWeaponItem
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
            }
            return InteractionResultHolder.consume(weaponStack)
        } else {

            if (NBTHelpers.isLoaded(weaponStack)) {

                val ammo = NBTHelpers.popAmmo(weaponStack)

                val bullet = JusticeBulletEntity(pLevel, pPlayer, ammo)

                ALoveLetter.LOGGER.info(ammo)

                val userUpVec = pPlayer.getUpVector(1.0F)
                val aimQuaterion = (Quaternionf().setAngleAxis(0.0, userUpVec.x, userUpVec.y, userUpVec.z))
                val userViewVec = pPlayer.getViewVector(1.0F)

                val aimVec3 = userViewVec.toVector3f().rotate(aimQuaterion)
                bullet.shoot(aimVec3.x.toDouble(), aimVec3.y.toDouble(), aimVec3.z.toDouble(), 1.0F, 0.0F)

                if (NBTHelpers.getNBTProjectileCount(weaponStack) == 0) {
                    NBTHelpers.setLoaded(weaponStack, false)
                }

                pLevel.addFreshEntity(bullet)

                return InteractionResultHolder.consume(weaponStack)
            }

            return InteractionResultHolder.fail(weaponStack)
        }




    }

    override fun onUseTick(
        pLevel: Level,
        pLivingEntity: LivingEntity,
        pStack: ItemStack,
        pRemainingUseDuration: Int
    ) {

        if (pLevel.isClientSide) {
            return
        }

        val currentUseDuration = pStack.useDuration - pRemainingUseDuration
        if (currentUseDuration > TIME_TO_LOAD) {
            // Resets the use timer and skips to releaseUsing in the itemstack, good for parity
            pLivingEntity.releaseUsingItem()
        }

    }

    override fun releaseUsing(pStack: ItemStack, pLevel: Level, pLivingEntity: LivingEntity, pTimeCharged: Int) {
        val currentUseDuration = pStack.useDuration - pTimeCharged

        if (currentUseDuration > TIME_TO_LOAD) {
            loadAmmoFromInventory(pLivingEntity, pStack)
        }
    }


    fun loadAmmoFromInventory(user: LivingEntity, weaponStack: ItemStack) {

        val isCreative = user is Player && user.abilities.instabuild

        if (!NBTHelpers.canLoadAmmo(weaponStack)) {
            return
        }

        var ammoStack = user.getProjectile(weaponStack)
        if (ammoStack.isEmpty && isCreative) {
            ammoStack = ItemStack(DEFAULT_PROJECTILE_ITEM)
        }

        tryLoadAmmo(user, weaponStack, ammoStack, isCreative)

    }

    fun tryLoadAmmo(user: LivingEntity, weaponStack: ItemStack, ammoStack: ItemStack, isCreative: Boolean): Boolean {

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



    override fun getUseDuration(pStack: ItemStack?): Int {
        return TIME_TO_LOAD + 3
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