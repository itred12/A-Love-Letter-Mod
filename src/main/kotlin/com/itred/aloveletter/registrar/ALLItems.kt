package com.itred.aloveletter.registrar

import com.itred.aloveletter.item.SpeedColaItem
import net.minecraft.world.food.FoodProperties
import net.minecraft.world.item.Item
import net.minecraft.world.item.Rarity
import net.minecraftforge.registries.ForgeRegistries
import thedarkcolour.kotlinforforge.forge.ObjectHolderDelegate
import thedarkcolour.kotlinforforge.forge.registerObject

object ALLItems: AbstractRegistrar<Item>(ForgeRegistries.ITEMS) {


    val SPEED_COLA by newComplexItem("scp_207") {
        SpeedColaItem( Item.Properties()
            .rarity(Rarity.UNCOMMON)
            .food(
                FoodProperties.Builder()
                    .alwaysEat()
                    .build()

            )
        )
    }


    private fun newSimpleItem(name: String): ObjectHolderDelegate<Item> {
        return this.registry.registerObject(name) { Item(Item.Properties()) }
    }

    private fun newComplexItem(name: String, preConfiguredItem: () -> Item): ObjectHolderDelegate<Item> {
        return this.registry.registerObject(name, preConfiguredItem)

    }

}