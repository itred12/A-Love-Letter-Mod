package com.itred.aloveletter.config

import dev.isxander.yacl3.api.ConfigCategory
import dev.isxander.yacl3.api.LabelOption
import dev.isxander.yacl3.api.YetAnotherConfigLib
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder
import net.minecraft.network.chat.Component
import net.minecraftforge.common.ForgeConfigSpec

object ALLConfig {

    const val SCREEN_NAME = "Mod config"

    val COMMON_CONFIG: CommonConfig
    val CLIENT_CONIFG: ClientConfig
    val SERVER_CONFIG: ServerConfig


    class CommonConfig(commonBuilder: ForgeConfigSpec.Builder) : AbstractConfigSection() {

        override val screenName = "Common Config"
        override val screenTooltip = "Common Config"

        val testConfigValue = commonBuilder
            .comment("Test!")
            .define("testConfigValue", true)


        fun populateCategory(): ConfigCategory {

            return encompassingCategoryBuilder()
                .option(LabelOption.create(Component.literal("Server config can be found in saves/<worldname>/serverconfig")))
                .option(
                    simpleTemplateOption(testConfigValue)
                        .controller(TickBoxControllerBuilder::create)
                        .build()
                )
                .build()


        }




    }

    class ClientConfig(clientBuilder: ForgeConfigSpec.Builder): AbstractConfigSection() {

        override val screenName = "Client Config"
        override val screenTooltip = "Client Config"

        val testConfigValue = clientBuilder
            .comment("Test!")
            .define("testConfigValue", true)


        fun populateCategory(): ConfigCategory {

            return encompassingCategoryBuilder()
                .option(
                    simpleTemplateOption(testConfigValue)
                        .controller(TickBoxControllerBuilder::create)
                        .build()
                ).build()


        }

    }

    class ServerConfig(serverBuilder: ForgeConfigSpec.Builder) {
        // No in-world config screen in this version, so no point in making a server config screen
        lateinit var configSpec: ForgeConfigSpec

    }

    init {

        // Construct each of the config sub-classes, Forge turns them into pairs of the class itself and their spec for us
        val COMMON_PAIR = ForgeConfigSpec.Builder().configure(::CommonConfig)
        val CLIENT_PAIR = ForgeConfigSpec.Builder().configure(::ClientConfig)
        val SERVER_PAIR = ForgeConfigSpec.Builder().configure(::ServerConfig)

        // Store 'em all to be registered and grabbed later
        COMMON_CONFIG = COMMON_PAIR.left
        COMMON_CONFIG.configSpec = COMMON_PAIR.right

        CLIENT_CONIFG = CLIENT_PAIR.left
        CLIENT_CONIFG.configSpec = CLIENT_PAIR.right

        SERVER_CONFIG = SERVER_PAIR.left
        SERVER_CONFIG.configSpec = SERVER_PAIR.right


    }

    fun constructConfigScreen(screenBuilder: YetAnotherConfigLib.Builder): YetAnotherConfigLib.Builder {
        return screenBuilder
            .title(Component.literal(SCREEN_NAME))
            .category(
                COMMON_CONFIG.populateCategory()
            )
            .category(CLIENT_CONIFG.populateCategory())

    }





}