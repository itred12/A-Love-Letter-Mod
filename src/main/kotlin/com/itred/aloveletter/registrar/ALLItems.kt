package com.itred.aloveletter.registrar

import com.itred.aloveletter.ALoveLetter
import com.itred.aloveletter.item.JusticeWeaponItem
import com.itred.aloveletter.item.SpeedColaItem
import com.itred.aloveletter.item.WaxPaperItem
import net.minecraft.core.registries.Registries
import net.minecraft.network.chat.Component
import net.minecraft.world.food.FoodProperties
import net.minecraft.world.item.*
import net.minecraftforge.eventbus.api.IEventBus
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
                output.accept(WAX_PAPER)
                output.accept(ENCHANTED_PARCHMENT)

            }
            .build()
    }


    // ITEMS

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

    val ENCHANTED_PARCHMENT by newComplexItem("enchanted_parchment") {
        // Object keyword before this allows us to turn this into an anonymous class we can override
        object: Item(
            Item.Properties()
                .rarity(Rarity.RARE)
        ) {
            override fun canGrindstoneRepair(stack: ItemStack?): Boolean {
                return true
            }

            // Enchanted glint
            override fun isFoil(pStack: ItemStack?): Boolean {
                return true
            }
        }
    }


    val WAX_PAPER by newComplexItem("wax_paper") {
        WaxPaperItem(Item.Properties())
    }

    val JUSTICE_GUN by newComplexItem("justice_weapon") {
        JusticeWeaponItem(Item.Properties()
            .rarity(Rarity.RARE)
        )
    }



    private fun newSimpleItem(name: String): ObjectHolderDelegate<Item> {
        return this.registry.registerObject(name) { Item(Item.Properties()) }
    }

    private fun newComplexItem(name: String, preConfiguredItem: () -> Item): ObjectHolderDelegate<Item> {
        return this.registry.registerObject(name, preConfiguredItem)

    }


    public override fun register(modbus: IEventBus) {
        super.register(modbus)
        CREATIVE_TAB_REGISTER.register(modbus)
    }

}