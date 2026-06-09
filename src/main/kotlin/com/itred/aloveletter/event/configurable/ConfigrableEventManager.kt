package com.itred.aloveletter.event.configurable

import com.itred.aloveletter.ALoveLetter
import com.itred.aloveletter.event.configurable.server.BlueAxolotlPing
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.config.ModConfig
import net.minecraftforge.fml.event.config.ModConfigEvent
import net.minecraftforge.fml.loading.FMLEnvironment
import thedarkcolour.kotlinforforge.forge.MOD_CONTEXT

@Mod.EventBusSubscriber(modid = ALoveLetter.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
object ConfigrableEventManager {

    private val modEventBus = MOD_CONTEXT.getKEventBus()

    private val CONFIGURABLE_EVENT_HANDLERS: Map<ModConfig.Type, List<IConfigurableEventHandler>> = mapOf(
        Pair(ModConfig.Type.COMMON, listOf()),
        Pair(ModConfig.Type.CLIENT, listOf(

        )),
        Pair(ModConfig.Type.SERVER, listOf(
            BlueAxolotlPing
        ))
    )

    @SubscribeEvent
    fun onConfigLoad(event: ModConfigEvent.Loading) {

        val modConfig = event.config

        if (!modConfig.modId.equals(ALoveLetter.MODID)) {
            return
        }

        val eventHandlerGroup = CONFIGURABLE_EVENT_HANDLERS[modConfig.type] ?: return

        for (eventHandler: IConfigurableEventHandler in eventHandlerGroup) {


            ALoveLetter.LOGGER.info("Querying configurable event handler \"{}\" from config \"{}\"", eventHandler::class.simpleName, modConfig.type)
            if (eventHandler.shouldEnable()) {
                ALoveLetter.LOGGER.info("Loading event handler \"{}\" from config \"{}\"!", eventHandler::class.simpleName, modConfig.type)
                eventHandler.enable(modEventBus)
            }
        }

    }

    @SubscribeEvent
    fun onConfigUnload(event: ModConfigEvent.Unloading) {

        val modConfig = event.config

        if (!modConfig.modId.equals(ALoveLetter.MODID)) {
            return
        }

        val eventHandlerGroup = CONFIGURABLE_EVENT_HANDLERS[modConfig.type] ?: return

        ALoveLetter.LOGGER.info("Unloading all event handlers from config \"{}\"", modConfig.type)
        for (eventHandler: IConfigurableEventHandler in eventHandlerGroup) {
            ALoveLetter.LOGGER.info("Unloading event handler \"{}\"...", eventHandler::class.simpleName)
            eventHandler.disable(modEventBus)
        }

    }

    @SubscribeEvent
    fun onConfigReload(event: ModConfigEvent.Reloading) {

        val modConfig = event.config

        if (!modConfig.modId.equals(ALoveLetter.MODID)) {
            return
        }



        // Ensure proper side
        if (
            modConfig.type != ModConfig.Type.COMMON &&
            ((FMLEnvironment.dist == Dist.CLIENT && modConfig.type != ModConfig.Type.CLIENT)
            || (FMLEnvironment.dist == Dist.DEDICATED_SERVER && modConfig.type != ModConfig.Type.SERVER))
        ) {
            return
        }

        val eventHandlerGroup = CONFIGURABLE_EVENT_HANDLERS[modConfig.type] ?: return

        for (eventHandler: IConfigurableEventHandler in eventHandlerGroup) {

            ALoveLetter.LOGGER.info("Querying configurable event handler \"{}\" from config \"{}\"", eventHandler::class.simpleName, modConfig.type)

            if (eventHandler.isEnabled && eventHandler.shouldEnable()) {
                // If it's enabled, and should be enabled according to the new config, then its fine.
                ALoveLetter.LOGGER.info("No action needed for event handler \"{}\" from config \"{}\"", eventHandler::class.simpleName, modConfig.type)
            } else if (eventHandler.isEnabled) {
                // If it's enabled, and should be disabled according to the new config, then disable it.
                ALoveLetter.LOGGER.info("Unloading event handler \"{}\" from config \"{}\"...", eventHandler::class.simpleName, modConfig.type)
                eventHandler.disable(modEventBus)
            } else if (eventHandler.shouldEnable()) {
                // If it's disabled, and should be enabled according to the new config, then enable it.
                ALoveLetter.LOGGER.info("Loading event handler \"{}\" from config \"{}\"!", eventHandler::class.simpleName, modConfig.type)
                eventHandler.enable(modEventBus)
            }

        }


    }







}