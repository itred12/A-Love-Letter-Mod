package com.itred.aloveletter.datagen.data

import net.minecraft.data.PackOutput
import net.minecraft.data.recipes.FinishedRecipe
import net.minecraft.data.recipes.RecipeProvider
import java.util.function.Consumer

class PackRecipesProvider(pack: PackOutput) : RecipeProvider(pack) {


    override fun buildRecipes(writer: Consumer<FinishedRecipe?>?) {


    }


}