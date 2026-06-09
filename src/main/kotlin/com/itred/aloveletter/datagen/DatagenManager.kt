package com.itred.aloveletter.datagen

import net.minecraftforge.data.event.GatherDataEvent
import net.minecraftforge.eventbus.api.EventPriority
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod


@Mod.EventBusSubscriber
class DatagenManager {

    @SubscribeEvent(priority = EventPriority.LOWEST)
    fun registerDataGenerators(event: GatherDataEvent) {

    }

}