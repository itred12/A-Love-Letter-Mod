package com.itred.aloveletter.event

import com.itred.aloveletter.ALoveLetter
import net.minecraftforge.eventbus.api.IEventBus
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.config.ModConfig
import net.minecraftforge.fml.event.config.ModConfigEvent


class EventManager(val modBus: IEventBus) {

    val CONFIGURABLE_EVENT_HANDLERS: Map<ModConfig.Type, List<IConfigurableEventHandler>> = mapOf(
        Pair(ModConfig.Type.COMMON, listOf()),
        Pair(ModConfig.Type.CLIENT, listOf()),
        Pair(ModConfig.Type.SERVER, listOf())
    )

    @SubscribeEvent
    private fun onConfigReload(event: ModConfigEvent.Loading) {

        val config = event.config

        if (!config.modId.equals(ALoveLetter.MODID)) {
            return
        }

    }

}