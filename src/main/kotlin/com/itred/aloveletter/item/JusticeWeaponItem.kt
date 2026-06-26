package com.itred.aloveletter.item

import com.itred.aloveletter.ALoveLetter
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.ListTag
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.ProjectileWeaponItem
import net.minecraft.world.level.Level
import java.util.function.Predicate

class JusticeWeaponItem(properties: Properties) : ProjectileWeaponItem(properties) {

    companion object {
        // Stats
        const val LOAD_TIME = 20 * 1 // Ticks
        const val MAX_PROJECTILES = 6

        const val PROJECTILE_TAG_NAME = "Ammunition"
        const val CHARGED_TAG_NAME = "Loaded"

        private val DEFAULT_PROJECTILE_ITEM = Items.ARROW



        @JvmStatic
        fun isLoaded(gunStack: ItemStack): Boolean {
            val rootTag = gunStack.tag
            return rootTag != null && rootTag.getBoolean(CHARGED_TAG_NAME)
        }

        @JvmStatic
        fun setLoaded(gunStack: ItemStack, value: Boolean) {
            val rootTag = gunStack.orCreateTag
            rootTag.putBoolean(CHARGED_TAG_NAME, value)
        }

        @JvmStatic
        fun canLoad(gunStack: ItemStack): Boolean {
            return getAllProjectiles(gunStack).size < MAX_PROJECTILES
        }

        @JvmStatic
        private fun getAllProjectiles(gunStack: ItemStack): List<ItemStack> {
            val rootTag = gunStack.tag

            val ammoList = mutableListOf<ItemStack>()

            // Grab the ammo tag, iterate through all of its elements and parse each as an itemstack to add to the list
            if (rootTag != null && rootTag.contains(PROJECTILE_TAG_NAME, 9)) {

                val listTag = rootTag.getList(PROJECTILE_TAG_NAME, 10)
                if (listTag != null) {

                    for (i in 0..<listTag.size) {
                        val compoundTag = listTag.getCompound(i)
                        ammoList.add(ItemStack.of(compoundTag))
                    }

                }

            }

            return ammoList

        }


        @JvmStatic
        private fun loadIntoNBT(gunStack: ItemStack, ammoStack: ItemStack) {
            ALoveLetter.LOGGER.info("test")
            val rootTag = gunStack.orCreateTag

            var currentAmmo: ListTag

            if (rootTag.contains(PROJECTILE_TAG_NAME, 9)) {
                currentAmmo = rootTag.getList(PROJECTILE_TAG_NAME, 10)
            } else {
                currentAmmo = ListTag()
            }

            // Translate the ammo into NBT format
            val ammoTag = CompoundTag()
            ammoStack.save(ammoTag)
            currentAmmo.add(ammoTag)

            // Save it back to the gun stack
            rootTag.put(PROJECTILE_TAG_NAME, currentAmmo)
        }



        @JvmStatic
        private fun tryLoadProjectileFromInventory(user: LivingEntity, gunStack: ItemStack): Boolean {

            // Dont load once we hit max
            if (getAllProjectiles(gunStack).size >= MAX_PROJECTILES) {
                return false
            }

            val isCreativeModePlayer = user is Player && user.abilities.instabuild

            // Ties into an event hook for getting the item's projectile
            var ammoStack = user.getProjectile(gunStack)

            if (ammoStack.isEmpty && isCreativeModePlayer) {
                ammoStack = getDefaultProjectile()
            }

            return loadGivenProjectile(user, gunStack, ammoStack, isCreativeModePlayer)
        }

        @JvmStatic
        private fun loadGivenProjectile(user: LivingEntity, gunStack: ItemStack, ammoStack: ItemStack, isCreativeModePlayer: Boolean): Boolean {
            if (ammoStack.isEmpty) {
                return false
            }

            // Whether the stack is the "fake" stack given by default for creative players whom load it without ammo
            // (tired rn so just following vanilla crossbow logic)
            val isFakeStack = isCreativeModePlayer && ammoStack.item == DEFAULT_PROJECTILE_ITEM

            var pickedAmmo: ItemStack
            if (!isFakeStack && !isCreativeModePlayer) {
                pickedAmmo = ammoStack.split(1)
                // Only can really remove items from entities with an inventory, such as players
                if (ammoStack.isEmpty && user is Player) {
                    user.inventory.removeItem(ammoStack)
                }
            } else {
                pickedAmmo = ammoStack.copy()
            }

            loadIntoNBT(gunStack, pickedAmmo)
            return true
        }



        fun getDefaultProjectile(): ItemStack {
            return ItemStack(DEFAULT_PROJECTILE_ITEM)
        }
    }

    override fun use(
        pLevel: Level,
        pPlayer: Player,
        pUsedHand: InteractionHand
    ): InteractionResultHolder<ItemStack?>? {



        val thisStack = pPlayer.getItemInHand(pUsedHand)
        if (!pPlayer.getProjectile(thisStack).isEmpty) {

            if (canLoad(thisStack)) {
                pPlayer.startUsingItem(pUsedHand)
            }

            return InteractionResultHolder.consume(thisStack)
        } else {
            return InteractionResultHolder.fail(thisStack)
        }


    }

    override fun onUseTick(
        pLevel: Level,
        pLivingEntity: LivingEntity,
        pStack: ItemStack,
        pRemainingUseDuration: Int
    ) {
        // Only do this serverside
        if (!pLevel.isClientSide) {

            if (pStack.useDuration - pRemainingUseDuration > LOAD_TIME) {
                ALoveLetter.LOGGER.info("test2")
                pLivingEntity.releaseUsingItem()
            }

        }
    }

    override fun releaseUsing(pStack: ItemStack, pLevel: Level, pLivingEntity: LivingEntity, pTimeCharged: Int) {
        if (!pLevel.isClientSide) {
            val chargedTime = pStack.useDuration - pTimeCharged

            if (chargedTime > LOAD_TIME && !isLoaded(pStack) && tryLoadProjectileFromInventory(pLivingEntity, pStack)) {
                setLoaded(pStack, true)
            }
        }

    }


    override fun getAllSupportedProjectiles(): Predicate<ItemStack?> {
        return Predicate({itemStack -> itemStack?.`is`(Items.ARROW) ?: false})
    }

    override fun getDefaultProjectileRange(): Int {
        return 1
    }

    override fun getUseDuration(pStack: ItemStack?): Int {
        return LOAD_TIME + 3
    }





}