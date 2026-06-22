package com.itred.aloveletter.datagen.data.recipe

import com.itred.aloveletter.registrar.ALLItems
import com.itred.aloveletter.registrar.ALLRecipeUtils
import net.minecraft.core.RegistryAccess
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.inventory.CraftingContainer
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.CraftingBookCategory
import net.minecraft.world.item.crafting.CustomRecipe
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.level.Level

class ParchmentToBookRowBindingRecipe(id: ResourceLocation, recipeCategory: CraftingBookCategory) : CustomRecipe(id, recipeCategory) {

    // One of these items in any slot can be used to bind enchanted parchment into a book
    val SINGLE_BINDING_INGREDIENTS = arrayOf(
        Items.LEATHER
    )

    // Three of these items adjacent can also be used instead
    val ROW_BINDING_INGREDIENTS = arrayOf(
        Items.STRING
    )

    val PARCHMENT = ALLItems.ENCHANTED_PARCHMENT



    override fun matches(
        pContainer: CraftingContainer,
        pLevel: Level?
    ): Boolean {

        // For X, Y of the table's width and height, and index of a slot's current index:
            // The slots to the right of a given slot can be found by adding 1 to index, provided `index % X != X`,
            // The slots to the left of a given slot can be found by subtracting 1 from the index, provided `index % X != 0`
        //
            // The slots above a given slot can be found by subtracting X from the index, provided `ceil(index + 1 / Y) != 1`
            // The slots nelow a given slot can be found by adding X to the index, provided `ceil(index + 1 / Y) != Y`

        var bindingType = RecipeBindingType.NONE

        var bindingIndex = mutableListOf<Int>()


        var hasParchment = false

        for (slotindex in 0..<pContainer.containerSize) {

            val stack = pContainer.getItem(slotindex)

            if (ROW_BINDING_INGREDIENTS.contains(stack.item)) {

                // Because the crafting grid is scanned left-to-right, we can always trust that if we have the leftmost slot here,
                // then

                bindingIndex.add(slotindex)



            }

            if (SINGLE_BINDING_INGREDIENTS.contains(stack.item)) {

                if (isSlotTouchingEdge(pContainer, slotindex) && bindingType == RecipeBindingType.NONE) {
                    bindingType = RecipeBindingType.SINGLE
                } else {
                    // If the binding type is already set or this is in the wrong slot, must be a disallowed arrangment
                    return false
                }

            }



        }

        return false

    }

    override fun assemble(
        pContainer: CraftingContainer?,
        pRegistryAccess: RegistryAccess?
    ): ItemStack? {
        return null
    }

    override fun canCraftInDimensions(pWidth: Int, pHeight: Int): Boolean {
        return (pWidth > 1 || pHeight > 1) && (pWidth < 4 && pHeight < 4)
    }

    override fun getSerializer(): RecipeSerializer<*> {
        return ALLRecipeUtils.PARCHMENT_TO_BOOK_RECIPE_SERIALIZER
    }


    /** Returns true if the slot tied to a given index is touching the edge of the given crafting grid, and false otherwise */
    private fun isSlotTouchingEdge(container: CraftingContainer, slotIndex: Int): Boolean {

        val widthIndex = slotIndex % container.width
        val heightIndex = slotIndex + 1 / container.height

        if (widthIndex == 0 || widthIndex == container.width) {
            return true
        }

        if (heightIndex == 1 || heightIndex == container.height) {
            return true
        }

        return false

    }

    private fun getContinuousItems(container: CraftingContainer, startingSlotIndex: Int, items: List<Item>) {

        val foundItems = mutableListOf<Item>()

        var end = false

        while (end == false) {

            val widthIndex = startingSlotIndex % container.width
            val heightIndex = startingSlotIndex + 1 / container.height

            // Up
            for (i in heightIndex downTo 2) {
                val indexAbove = startingSlotIndex - container.width
                foundItems.add(container.getItem(indexAbove).item)
            }

        }


    }

    private fun searchForUnbrokenTrioOf(pContainer: CraftingContainer, items: List<Item>, searchPoint: Int) {

        val widthIndex = searchPoint % pContainer.width
        val heightIndex = searchPoint + 1 / pContainer.height

        var foundItems = 1

        while (foundItems < 3) {

        }

        val patternMatched = false
        // Detect if we're in the middle of a row
        if (widthIndex != 0 && widthIndex != (pContainer.width - 1)) {

            // We then look to the left or right, or above x2, above & below, or below x2
            var heightTicker = pContainer.height - heightIndex


        }

    }


    enum class RecipeBindingType {
        SINGLE,
        SIDE,
        NONE
    }
}