package com.itred.aloveletter.event.configurable

import com.itred.aloveletter.ALoveLetter
import com.itred.aloveletter.event.configurable.serverloadedworldevents.BlueAxolotlPing
import com.itred.aloveletter.event.configurable.serverloadedworldevents.BlueAxolotlTweaks
import com.itred.aloveletter.event.configurable.serverloadedworldevents.UnbreakableTiersItems
import net.minecraftforge.event.server.ServerAboutToStartEvent
import net.minecraftforge.event.server.ServerStoppingEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod
import thedarkcolour.kotlinforforge.forge.MOD_CONTEXT

@Mod.EventBusSubscriber(modid = ALoveLetter.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
object ConfigrableEventManager {

    private val modEventBus = MOD_CONTEXT.getKEventBus()

    /** Event listeners in these classes will be loaded and unloaded when the logical client leaves and rejoins the world.
     * Common config values present when enabling/disabling them are thus the values as listed on the client, not the server. */
    private val ClientLoadedWorldEvents: List<IConfigurableEventHandler> = listOf()

    /** Event listeners in these classes will be loaded and unloaded as the logical server starts and shuts down.
     * Commonn config values present when enabling/disabling them are thus the values as present on the server/server-hoster's client. */
    private val ServerLoadedWorldEvents: List<IConfigurableEventHandler> = listOf(
        UnbreakableTiersItems,
        BlueAxolotlPing,
        BlueAxolotlTweaks
    )



    @SubscribeEvent
    fun onWorldLoad(event: ServerAboutToStartEvent) {

        for (eventHandler in ServerLoadedWorldEvents) {

            ALoveLetter.LOGGER.info("Querying server-loaded world event handler \"{}\"...", eventHandler::class.simpleName)
            if (eventHandler.shouldEnable()) {
                ALoveLetter.LOGGER.info("Loading server-loaded world event handler \"{}\"!", eventHandler::class.simpleName)
                eventHandler.enable(modEventBus)
            }

        }

    }

    @SubscribeEvent
    fun onWorldUnload(event: ServerStoppingEvent) {

        for (eventHandler in ServerLoadedWorldEvents) {

            ALoveLetter.LOGGER.info("Unloading server-loaded world event handler \"{}\"...", eventHandler::class.simpleName)
            eventHandler.disable(modEventBus)

        }

    }







}