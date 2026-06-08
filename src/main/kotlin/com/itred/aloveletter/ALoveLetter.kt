package com.itred.aloveletter

import com.itred.aloveletter.block.ModBlocks
import dev.isxander.yacl3.api.YetAnotherConfigLib


import net.minecraft.client.Minecraft
import net.minecraft.client.gui.components.ChatComponent
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraftforge.client.ConfigScreenHandler
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.ModLoadingContext
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.config.ModConfig
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent
import net.minecraftforge.fml.event.lifecycle.FMLDedicatedServerSetupEvent
import org.apache.logging.log4j.Level
import org.apache.logging.log4j.LogManager
import thedarkcolour.kotlinforforge.forge.MOD_BUS
import thedarkcolour.kotlinforforge.forge.MOD_CONTEXT
import thedarkcolour.kotlinforforge.forge.registerConfig
import thedarkcolour.kotlinforforge.forge.runForDist
import java.awt.TextComponent

/**
 * Main mod class. Should be an `object` declaration annotated with `@Mod`.
 * The modid should be declared in this object and should match the modId entry
 * in mods.toml.
 *
 * An example for blocks is in the `blocks` package of this mod.
 */
@Mod(ALoveLetter.MODID)
object ALoveLetter {

    const val MODID = "aloveletter"
    val LOGGER = LogManager.getLogger(MODID);



    init {
        val modEventBus = MOD_CONTEXT.getKEventBus()



        // Allows NeoForge to create a config screen for this mod's configs.
        // The config screen is accessed by going to the Mods screen > clicking on your mod > clicking on config.
        // Do not forget to add translations for your config options to the en_us.json file.

        // Register this in the event bus for @RegisterEvent-annotated stuff to run
        MinecraftForge.EVENT_BUS.register(this)

        // Config
        registerConfig(ModConfig.Type.COMMON, ALLConfig.COMMON_CONFIG_SPEC)
        // Uncomment once we actually have config values in here
        registerConfig(ModConfig.Type.CLIENT, ALLConfig.CLIENT_CONFIG_SPEC)
        registerConfig(ModConfig.Type.SERVER, ALLConfig.SERVER_CONFIG_SPEC)


        LOGGER.log(Level.INFO, "Hello world!")

        // Register the KDeferredRegister to the mod-specific event bus
        ModBlocks.REGISTRY.register(MOD_BUS)

        val obj = runForDist(
            clientTarget = {
                MOD_BUS.addListener(::onClientSetup)
                Minecraft.getInstance()
            },
            serverTarget = {
                MOD_BUS.addListener(::onServerSetup)
                "test"
            })

        println(obj)



        ModLoadingContext.get().registerExtensionPoint<ConfigScreenHandler.ConfigScreenFactory>(ConfigScreenHandler.ConfigScreenFactory::class.java, {
            ConfigScreenHandler.ConfigScreenFactory{
                client, parent ->
                YetAnotherConfigLib.createBuilder()
                .build()
                .generateScreen(parent)
            }
        } )




    }


    /**
     * This is used for initializing client specific
     * things such as renderers and keymaps
     * Fired on the mod specific event bus.
     */
    private fun onClientSetup(event: FMLClientSetupEvent) {
        LOGGER.log(Level.INFO, "Initializing client...")

    }

    /**
     * Fired on the global Forge bus.
     */
    private fun onServerSetup(event: FMLDedicatedServerSetupEvent) {
        LOGGER.log(Level.INFO, "Server starting...")
    }


    fun modLoc(path: String): ResourceLocation {
        return ResourceLocation(MODID, path)
    }
}