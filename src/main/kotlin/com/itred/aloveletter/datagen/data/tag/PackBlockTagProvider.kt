package com.itred.aloveletter.datagen.data.tag

import com.itred.aloveletter.ALoveLetter
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraftforge.common.data.BlockTagsProvider
import net.minecraftforge.common.data.ExistingFileHelper
import java.util.concurrent.CompletableFuture

class PackBlockTagProvider(pack: PackOutput, lookupProvider: CompletableFuture<HolderLookup.Provider>, existingFileHelper: ExistingFileHelper)
    : BlockTagsProvider(pack, lookupProvider, ALoveLetter.MODID, existingFileHelper) {

    override fun addTags(pProvider: HolderLookup.Provider?) {

    }

}