package com.itred.aloveletter.event.configurable.serverloadedworldevents

import com.itred.aloveletter.config.ALLConfig
import com.itred.aloveletter.event.configurable.IConfigurableEventHandler
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TieredItem
import net.minecraftforge.eventbus.api.IEventBus
import net.minecraftforge.registries.ForgeRegistries

object UnbreakableTiersItems: IConfigurableEventHandler {

    val unbreakableTiers = mutableListOf<Item>()

    val unbreakableItems = mutableListOf<Item>()

    fun shouldDamage(itemStack: ItemStack): Boolean {
        val item: Item = itemStack.item
        return shouldDamage(item)
    }

    fun shouldDamage(item: Item): Boolean {
        // If this event is disabled, ignore all logic
        if (!this.isEnabled) {
            return true
        }


        // If the item is a tiered item, and its tier is specified as unbreakable...
        if (item is TieredItem) {

            // Then, if any of its tiers' repair ingredients are present in the tiers list, it should be unbreakable
            if (item.tier.repairIngredient.items.any { stack -> unbreakableTiers.contains(stack.item) }) {
                return false
            }

        }

        // Or, if that check fails, if it's in the "unbreakable items list...
        if (unbreakableItems.contains(item)) {
            // The it'll be unbreakable, too
            return false
        }

        // Otherwise, return true to tell the mixin to ignore this function call, and use whatever the original isDamagableItem check had
        return true
    }




    // From the string ID lists of the config, turn them into usable items
    private fun getItemsFromIdList(list: List<String>, target: MutableList<Item>) {

        for (itemId in list) {

            val location = ResourceLocation.tryParse(itemId)
            if (location != null) {
                val item = ForgeRegistries.ITEMS.getValue(location)
                if (item != null) {
                    target.add(item)
                }
            }

        }

    }



    override var isEnabled: Boolean = false


    override fun shouldEnable(): Boolean {
        return ALLConfig.COMMON_CONFIG.durabilityEnableUnbreakableItemTweaks.get()
    }

    override fun enable(modBus: IEventBus) {
        isEnabled = true
        unbreakableTiers.clear()
        getItemsFromIdList(ALLConfig.COMMON_CONFIG.durabilityUnbreakableTierList.get(), unbreakableTiers)

        unbreakableItems.clear()
        getItemsFromIdList(ALLConfig.COMMON_CONFIG.durabilityUnbreakableItemList.get(), unbreakableTiers)
    }



    override fun disable(modBus: IEventBus) {
        isEnabled = false
        unbreakableTiers.clear()
        unbreakableItems.clear()
    }
}