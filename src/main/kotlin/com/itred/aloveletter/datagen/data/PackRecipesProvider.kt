package com.itred.aloveletter.datagen.data

import com.itred.aloveletter.ALoveLetter
import com.itred.aloveletter.registrar.ALLRecipeUtils
import net.minecraft.data.PackOutput
import net.minecraft.data.recipes.FinishedRecipe
import net.minecraft.data.recipes.RecipeProvider
import net.minecraft.data.recipes.SpecialRecipeBuilder
import java.util.function.Consumer

class PackRecipesProvider(pack: PackOutput) : RecipeProvider(pack) {


    override fun buildRecipes(writer: Consumer<FinishedRecipe?>?) {

        SpecialRecipeBuilder.special(ALLRecipeUtils.PARCHMENT_TO_BOOK_RECIPE_SERIALIZER).save(writer, ALoveLetter.modLoc("parchment_to_book_single_binding").toString())
    }


}