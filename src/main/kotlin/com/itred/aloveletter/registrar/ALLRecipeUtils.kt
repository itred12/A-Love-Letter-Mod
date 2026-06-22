package com.itred.aloveletter.registrar

import com.google.gson.JsonArray
import com.itred.aloveletter.datagen.data.recipe.ParchmentToBookSingleBindingRecipe
import net.minecraft.core.NonNullList
import net.minecraft.world.inventory.CraftingContainer
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer
import net.minecraftforge.registries.ForgeRegistries
import thedarkcolour.kotlinforforge.forge.registerObject

object ALLRecipeUtils : AbstractRegistrar<RecipeSerializer<*>>(ForgeRegistries.RECIPE_SERIALIZERS) {

    val PARCHMENT_TO_BOOK_RECIPE_SERIALIZER by registry.registerObject(
        "parchment_to_book",
        { SimpleCraftingRecipeSerializer(::ParchmentToBookSingleBindingRecipe) }
    )

    // Mostly referenced from a function of the same name in the serializer of `net.minecraft.world.item.crafting.ShapelessRecipe`
    fun itemsFromJson(ingredientArray: JsonArray): NonNullList<Ingredient> {
        val list = NonNullList.create<Ingredient>()

        for (index in 0..<ingredientArray.size()) {
            val maybeIngredient = Ingredient.fromJson(ingredientArray[index], false)
            list.add(maybeIngredient)
        }

        return list
    }

    fun getUniqueItems(list: MutableList<ItemStack>): List<Item> {

        val outList = mutableListOf<Item>()

        for (stack in list) {
            val item = stack.item
            if (!outList.contains(item)) {
                outList.add(item)
            }
        }

        return outList
    }

    /** Returns true if the slot tied to a given index is touching the edge of the given crafting grid, and false otherwise */
    fun isSlotTouchingEdge(container: CraftingContainer, slotIndex: Int): Boolean {

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



}