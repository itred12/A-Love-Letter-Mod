package com.itred.aloveletter.datagen

import com.itred.aloveletter.ALoveLetter
import com.itred.aloveletter.datagen.assets.PackItemModelProvider
import com.itred.aloveletter.datagen.assets.PackLanguageProvider
import com.itred.aloveletter.datagen.assets.PackSoundProvider
import com.itred.aloveletter.datagen.data.PackRecipesProvider
import com.itred.aloveletter.datagen.data.PackRegistriesGenerator
import com.itred.aloveletter.datagen.data.tag.PackBlockTagProvider
import com.itred.aloveletter.datagen.data.tag.PackItemTagProvider
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
        var lookupProvider = event.lookupProvider

        lookupProvider = generator.addProvider(event.includeServer(), PackRegistriesGenerator(pack, lookupProvider)).registryProvider

        val blockTagProvider = PackBlockTagProvider(pack, lookupProvider, existingFileHelper)
        generator.addProvider(event.includeServer(), blockTagProvider)
        generator.addProvider(event.includeServer(), PackItemTagProvider(pack, lookupProvider, blockTagProvider.contentsGetter(), existingFileHelper))

        generator.addProvider(event.includeServer(), PackRecipesProvider(pack))

        generator.addProvider(event.includeClient(), PackItemModelProvider(pack, existingFileHelper))

        generator.addProvider(event.includeClient(), PackSoundProvider(pack, existingFileHelper))
        generator.addProvider(event.includeClient(), PackLanguageProvider(pack, lookupProvider))



    }

}