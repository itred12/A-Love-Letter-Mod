package com.itred.aloveletter.datagen.assets

import com.itred.aloveletter.ALoveLetter
import com.itred.aloveletter.registrar.ALLItems
import net.minecraft.data.PackOutput
import net.minecraftforge.client.model.generators.ItemModelProvider
import net.minecraftforge.common.data.ExistingFileHelper

class PackItemModelProvider(pack: PackOutput, existingFileHelper: ExistingFileHelper): ItemModelProvider(pack, ALoveLetter.MODID, existingFileHelper) {

    override fun registerModels() {
        basicItem(ALLItems.SPEED_COLA)

    }


}