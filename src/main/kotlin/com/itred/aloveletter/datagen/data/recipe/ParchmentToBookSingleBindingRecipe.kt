package com.itred.aloveletter.datagen.data.recipe

import com.itred.aloveletter.datagen.data.tag.PackItemTagProvider
import com.itred.aloveletter.registrar.ALLItems
import com.itred.aloveletter.registrar.ALLRecipeUtils
import net.minecraft.core.RegistryAccess
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.inventory.CraftingContainer
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.CraftingBookCategory
import net.minecraft.world.item.crafting.CustomRecipe
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.EnchantmentHelper
import net.minecraft.world.level.Level

class ParchmentToBookSingleBindingRecipe(id: ResourceLocation, recipeCategory: CraftingBookCategory) : CustomRecipe(id, recipeCategory) {


    // One of these items in any slot can be used to bind enchanted parchment into a book
    val SINGLE_BINDING_INGREDIENTS = PackItemTagProvider.BOOK_BINDING_SINGLE_ITEMS
    val PARCHMENT = ALLItems.ENCHANTED_PARCHMENT

    override fun matches(
        pContainer: CraftingContainer,
        pLevel: Level?
    ): Boolean {

        // Cant check item tags until the assemble step, but I can check for parchment and *absence* of it
        var parchmentCount = 0
        var potentialBindingItemCount = 0

        for (slotindex in 0..<pContainer.containerSize) {

            val stack = pContainer.getItem(slotindex)
            if (stack.isEmpty) {
                continue
            }

            if (stack.`is`(ALLItems.ENCHANTED_PARCHMENT)) {
                parchmentCount++
            } else {
                potentialBindingItemCount++

                // Only one binding item
                if (potentialBindingItemCount > 1) {
                    return false
                }

            }
        }

        // It's potentially a match if there's 1 or more parchment and only one binding item
        return (parchmentCount > 0 || potentialBindingItemCount == 1)

    }

    override fun assemble(
        pContainer: CraftingContainer,
        pRegistryAccess: RegistryAccess
    ): ItemStack? {

        val itemsRegistry = pRegistryAccess.lookupOrThrow(Registries.ITEM)
        val bindingItems = itemsRegistry.getOrThrow(SINGLE_BINDING_INGREDIENTS)


        var bindingItemCount = 0
        val bookEnchantments: MutableMap<Enchantment, Int> = mutableMapOf()


        for (slotindex in 0..<pContainer.containerSize) {

            val stack = pContainer.getItem(slotindex)
            if (stack.isEmpty) {
                continue
            }

            if (stack.`is`(ALLItems.ENCHANTED_PARCHMENT)) {
                val stackEnchants = EnchantmentHelper.getEnchantments(stack)

                for (enchantment in stackEnchants) {
                    val existingLevel = bookEnchantments[enchantment.key]
                    if (existingLevel == null || existingLevel < enchantment.value) {
                        bookEnchantments[enchantment.key] = enchantment.value
                    }

                }

            } else if ( bindingItems.any { stack.item == it.get() }) {
                bindingItemCount++
            } else {
                // If it's any other kind of item, the recipe must be illegal
                return ItemStack.EMPTY
            }
        }

        if (bookEnchantments.isEmpty() || bindingItemCount != 1) {
            return ItemStack.EMPTY
        }

        val outputBook = ItemStack(Items.ENCHANTED_BOOK)
        // May need to sanity-check for if the enchantments are book-applicable, unsure if this is already done by this method or its callees though
        EnchantmentHelper.setEnchantments(bookEnchantments, outputBook)
        return outputBook


    }

    override fun canCraftInDimensions(pWidth: Int, pHeight: Int): Boolean {
        return (pWidth > 1 || pHeight > 1) && (pWidth < 4 && pHeight < 4)
    }

    override fun getSerializer(): RecipeSerializer<*> {
        return ALLRecipeUtils.PARCHMENT_TO_BOOK_RECIPE_SERIALIZER
    }



}