package com.itred.aloveletter.event.persistent.common

import com.itred.aloveletter.ALoveLetter
import com.itred.aloveletter.registrar.ALLPotions
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.alchemy.Potion
import net.minecraft.world.item.alchemy.PotionUtils
import net.minecraft.world.item.alchemy.Potions
import net.minecraft.world.item.crafting.Ingredient
import net.minecraftforge.common.brewing.BrewingRecipeRegistry
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = ALoveLetter.MODID)
object UnconventionalRecipeRegistrar {

    @SubscribeEvent
    fun onCommonSetup(event: FMLCommonSetupEvent) {
        event.enqueueWork() {
            // Add recipes from unconventional holders/providers (non-data-driven) here.
            // For example:

            // Potions
            BrewingRecipeRegistry.addRecipe(
                Ingredient.of(getItemStackOfPotion(Potions.STRONG_SWIFTNESS)),
                Ingredient.of(Items.FERMENTED_SPIDER_EYE),
                getItemStackOfPotion(ALLPotions.POTION_HYPERACTIVE)
            )





        }



    }

    private fun getItemStackOfPotion(potion: Potion): ItemStack {
        return PotionUtils.setPotion(ItemStack(Items.POTION), potion)
    }
}