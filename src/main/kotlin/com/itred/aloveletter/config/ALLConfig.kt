package com.itred.aloveletter.config

import com.electronwill.nightconfig.core.EnumGetMethod
import com.itred.aloveletter.event.configurable.server.BlueAxolotlPing
import dev.isxander.yacl3.api.ConfigCategory
import dev.isxander.yacl3.api.LabelOption
import dev.isxander.yacl3.api.YetAnotherConfigLib
import dev.isxander.yacl3.api.controller.EnumControllerBuilder
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

        val blueAxolotlPingSFX = clientBuilder
            .comment(
                "Plays a fitting sound effect when you're near a blue axolotl, or when one spawns in near you.",
                "",
                "Blue Axolotl Ping must be enabled in the server config for this to take effect.",
                "",
                "Values:",
                "",
                "  NONE - No sound effect plays.",
                "",
                "  BW - Plays the Shiny Pokemon sound effect from Pokemon Black and White.",
                "",
                "  PLA - Plays the Shiny Pokemon sound effect from Pokemon Legends: Arceus and Pokemon Legends: ZA.",
                ""
            )
            .defineEnum(
                "blueAxolotlPing",
                BlueAxolotlPing.PingSoundEffect.PLA,
                EnumGetMethod.ORDINAL_OR_NAME_IGNORECASE,
                BlueAxolotlPing.PingSoundEffect.PLA,
                BlueAxolotlPing.PingSoundEffect.BW,
                BlueAxolotlPing.PingSoundEffect.NONE
            )


        fun populateCategory(): ConfigCategory {

            return encompassingCategoryBuilder()
                .option(
                    simpleTemplateOption(blueAxolotlPingSFX)
                        .controller({opt -> EnumControllerBuilder.create<BlueAxolotlPing.PingSoundEffect>(opt).enumClass(BlueAxolotlPing.PingSoundEffect::class.java)})
                        .build()
                ).build()


        }

    }

    class ServerConfig(serverBuilder: ForgeConfigSpec.Builder) {
        // No in-world config screen in this version, so no point in making a server config screen
        lateinit var configSpec: ForgeConfigSpec


        val blueAxolotPingMasterSwitch = serverBuilder
            .push("blueaxolotltweaks")
            .comment("Enable or disable playing a sound effect when a player goes near a blue axolotl.",
                "",
                "The sound effect itself can also be enabled or disabled on the client's end, but it will always respect this option.")
            .worldRestart()
            .define("blueAxolotlPingMasterswitch", true)

        val blueAxolotlPingRange: ForgeConfigSpec.IntValue = serverBuilder
            .comment("If blueAxolotlPingMasterswitch is enabled, this controls the range in which blue axolotls will play the sound effect for a given player (radius, in blocks, of a sphere centered around the axolotl).",)
            .defineInRange("blueAxolotlPingRange", 32, 1, 256)

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