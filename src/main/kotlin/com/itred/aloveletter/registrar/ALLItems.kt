package com.itred.aloveletter.registrar

import com.itred.aloveletter.ALoveLetter
import com.itred.aloveletter.item.SpeedColaItem
import net.minecraft.core.registries.Registries
import net.minecraft.network.chat.Component
import net.minecraft.world.food.FoodProperties
import net.minecraft.world.item.*
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import thedarkcolour.kotlinforforge.forge.ObjectHolderDelegate
import thedarkcolour.kotlinforforge.forge.registerObject

object ALLItems: AbstractRegistrar<Item>(ForgeRegistries.ITEMS) {

    val CREATIVE_TAB_REGISTER: DeferredRegister<CreativeModeTab> = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ALoveLetter.MODID)

    val ALLTab: CreativeModeTab by CREATIVE_TAB_REGISTER.registerObject("maintab") {
        CreativeModeTab.builder()
            .title(Component.translatable("itemGroup." + ALoveLetter.MODID + ".maintab"))
            .icon{ ItemStack(Items.GLASS_BOTTLE) }
            .displayItems { parameters, output ->
                // Items go here
                output.accept(SPEED_COLA)
            }
            .build()
    }


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

    fun registerToCreativeModeTab() {

    }

}