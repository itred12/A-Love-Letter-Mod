package com.itred.aloveletter

import net.minecraftforge.common.ForgeConfigSpec

object ALLConfig {

    val COMMON_CONFIG: CommonConfig
    val CLIENT_CONIFG: ClientConfig
    val SERVER_CONFIG: ServerConfig

    val COMMON_CONFIG_SPEC: ForgeConfigSpec
    val CLIENT_CONFIG_SPEC: ForgeConfigSpec
    val SERVER_CONFIG_SPEC: ForgeConfigSpec



    class CommonConfig(commonBuilder: ForgeConfigSpec.Builder) {

        val TEST_CONFIG_VALUE = commonBuilder
            .comment("Test!")
            .define("testConfigValue", true)

    }

    class ClientConfig(clientBuilder: ForgeConfigSpec.Builder) {

    }

    class ServerConfig(serverBuilder: ForgeConfigSpec.Builder) {

    }

    init {

        // Construct each of the config sub-classes, Forge turns them into pairs of the class itself and their spec for us
        val COMMON_PAIR = ForgeConfigSpec.Builder().configure(::CommonConfig)
        val CLIENT_PAIR = ForgeConfigSpec.Builder().configure(::ClientConfig)
        val SERVER_PAIR = ForgeConfigSpec.Builder().configure(::ServerConfig)

        // Store 'em all to be registered and grabbed later
        COMMON_CONFIG = COMMON_PAIR.left
        CLIENT_CONIFG = CLIENT_PAIR.left
        SERVER_CONFIG = SERVER_PAIR.left

        COMMON_CONFIG_SPEC = COMMON_PAIR.right
        CLIENT_CONFIG_SPEC = CLIENT_PAIR.right
        SERVER_CONFIG_SPEC = SERVER_PAIR.right
    }






}