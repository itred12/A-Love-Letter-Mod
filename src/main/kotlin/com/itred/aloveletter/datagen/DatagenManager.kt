package com.itred.aloveletter.datagen

import com.itred.aloveletter.ALoveLetter
import net.minecraft.data.PackOutput
import net.minecraftforge.data.event.GatherDataEvent
import net.minecraftforge.eventbus.api.EventPriority
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod


@Mod.EventBusSubscriber(modid = ALoveLetter.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
object DatagenManager {

    @SubscribeEvent(priority = EventPriority.LOWEST)
    fun registerDataGenerators(event: GatherDataEvent) {

        val generator = event.generator
        val pack: PackOutput = generator.packOutput
        val existingFileHelper = event.existingFileHelper
        val lookupProvider = event.lookupProvider


        generator.addProvider(event.includeClient(), PackSoundProvider(pack, existingFileHelper))
        generator.addProvider(event.includeClient(), PackLanguageProvider(pack))
    }

}