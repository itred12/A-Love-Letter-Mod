package com.itred.aloveletter.datagen.data.tag

import com.itred.aloveletter.ALoveLetter
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.data.PackOutput
import net.minecraft.data.tags.ItemTagsProvider
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.level.block.Block
import net.minecraftforge.common.data.ExistingFileHelper
import java.util.concurrent.CompletableFuture

class PackItemTagProvider(pack: PackOutput, lookupProvider: CompletableFuture<HolderLookup.Provider>, blockTags: CompletableFuture<TagLookup<Block>>?, existingFileHelper: ExistingFileHelper) : ItemTagsProvider(pack, lookupProvider, blockTags, ALoveLetter.MODID, existingFileHelper) {

    companion object {

        val BOOK_BINDING_SINGLE_ITEMS = newItemTag("book_binding_single_ingredients")

        private fun newItemTag(name: String): TagKey<Item> {
            return TagKey.create(Registries.ITEM, ALoveLetter.modLoc(name))
        }
    }



    override fun addTags(pProvider: HolderLookup.Provider?) {
        tag(BOOK_BINDING_SINGLE_ITEMS)
            .add(Items.LEATHER)
    }


}