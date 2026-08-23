package com.itred.aloveletter.config

import com.itred.aloveletter.config.ALLConfig.CLIENT_CONIFG
import com.itred.aloveletter.config.ALLConfig.COMMON_CONFIG
import com.itred.aloveletter.config.ALLConfig.SCREEN_NAME
import com.itred.aloveletter.config.impl.AbstractConfigSection
import com.itred.aloveletter.event.configurable.server.BlueAxolotlPing
import dev.isxander.yacl3.api.ConfigCategory
import dev.isxander.yacl3.api.OptionFlag
import dev.isxander.yacl3.api.OptionGroup
import dev.isxander.yacl3.api.YetAnotherConfigLib
import dev.isxander.yacl3.api.controller.EnumControllerBuilder
import dev.isxander.yacl3.api.controller.IntegerSliderControllerBuilder
import dev.isxander.yacl3.api.controller.StringControllerBuilder
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder
import net.minecraft.network.chat.Component

object ALLConfigScreen {

    object ClientGeneral : AbstractConfigSection(CLIENT_CONIFG.configSpec) {

        override val side: ConfigSide = ConfigSide.CLIENT
        override val screenTranslationKey = "client.general"

        fun populateCategory(): ConfigCategory {

            return encompassingCategoryBuilder()
                .option(
                    simpleTemplateOption(CLIENT_CONIFG.blueAxolotlPingSFX)
                        .controller({opt -> EnumControllerBuilder.create<BlueAxolotlPing.PingSoundEffect>(opt).enumClass(BlueAxolotlPing.PingSoundEffect::class.java)})
                        .build()
                ).build()

        }
    }

    object CommonFunMisc : AbstractConfigSection(COMMON_CONFIG.configSpec) {

        override val side: ConfigSide = ConfigSide.COMMON
        override val screenTranslationKey: String = "funmisc"

        fun populateCategory(): ConfigCategory {

            return encompassingCategoryBuilder()
                //.option(LabelOption.create(Component.translatable("config.aloveletter.common.serveroptionbanner")))
                .group(
                    OptionGroup.createBuilder()
                        .name(Component.translatable("config.aloveletter.optiongroup.blueaxolotltweaks"))
                        .options(listOf(
                            // Ping masterswitch
                            simpleTemplateOption(COMMON_CONFIG.blueAxolotPingMasterSwitch)
                                .controller(TickBoxControllerBuilder::create)
                                .build(),

                            // Ping range
                            simpleTemplateOption(COMMON_CONFIG.blueAxolotlPingRange)
                                .controller { option -> IntegerSliderControllerBuilder.create(option)
                                    .range(1, 256)
                                    .step(1)
                                }
                                .build(),

                            // Natural spawn toggle
                            simpleTemplateOption(COMMON_CONFIG.blueAxolotNaturalSpawn)
                                .controller(TickBoxControllerBuilder::create)
                                .build(),

                            // Natural spawn odds
                            simpleTemplateOption(COMMON_CONFIG.blueAxolotNaturalSpawnChance)
                                .controller { option -> IntegerSliderControllerBuilder.create(option)
                                    .range(1, 8192)
                                    .step(1)
                                }
                                .build(),


                            // Bred spawn odds
                            simpleTemplateOption(COMMON_CONFIG.blueAxolotBredSpawnChance)
                                .controller { option -> IntegerSliderControllerBuilder.create(option)
                                    .range(1, 8192)
                                    .step(1)
                                }
                                .build(),

                        ))
                        .collapsed(true)
                        .build()
                )
                .build()

        }

    }

    object CommonBalance : AbstractConfigSection(COMMON_CONFIG.configSpec) {

        override val side: ConfigSide = ConfigSide.COMMON
        override val screenTranslationKey: String = "balance"

        fun populateCategory(): ConfigCategory {

            return encompassingCategoryBuilder()
                .options(listOf(

                    // Enable bow nerf
                    simpleTemplateOption(COMMON_CONFIG.rebalanceEnableBowNerf)
                        .controller(TickBoxControllerBuilder::create)
                        .build(),

                    // Enable crossbow buffs
                    simpleTemplateOption(COMMON_CONFIG.rebalanceEnableCrossbowBuffs)
                        .controller(TickBoxControllerBuilder::create)
                        .build(),

                    // Enable no invul on multishot
                    simpleTemplateOption(COMMON_CONFIG.rebalanceEnableMultishotNoInvulnerability)
                        .controller(TickBoxControllerBuilder::create)
                        .build(),

                    // Bow draw reset nerf
                    simpleTemplateOption(COMMON_CONFIG.rebalanceEnableBowDrawResetOnHit)
                        .controller(TickBoxControllerBuilder::create)
                        .build()
                ))
                .group(
                    // Bow draw reset exceptions
                    listTemplateOption(COMMON_CONFIG.rebalanceBowDrawResetExceptions)
                        .controller(StringControllerBuilder::create)
                        .initial("namespace:id")
                        .collapsed(true)
                        .build(),
                )
                .build()
        }

    }

    object CommonDurabilityTweaks : AbstractConfigSection(COMMON_CONFIG.configSpec) {

        override val side: ConfigSide = ConfigSide.COMMON
        override val screenTranslationKey: String = "durabilitytweaks"


        fun populateCategory(): ConfigCategory {

            return encompassingCategoryBuilder()
                //.option(LabelOption.create(Component.translatable("config.aloveletter.common.serveroptionbanner")))

                .options(listOf(

                    // Enable durability tweaks
                    simpleTemplateOption(COMMON_CONFIG.durabilityEnableUnbreakableItemTweaks)
                        .controller(TickBoxControllerBuilder::create)
                        .build(),

                ))

                .groups(listOf(
                    // Unbreakable items list
                    listTemplateOption(COMMON_CONFIG.durabilityUnbreakableItemList)
                    .controller(StringControllerBuilder::create)
                    .initial("namespace:id")
                    .collapsed(true)
                    .flag(OptionFlag.GAME_RESTART)
                    .build(),

                    // Unbreakable tiers list
                    listTemplateOption(COMMON_CONFIG.durabilityUnbreakableTierList)
                        .controller(StringControllerBuilder::create)
                        .initial("namespace:id")
                        .collapsed(true)
                        .flag(OptionFlag.GAME_RESTART)
                        .build(),
                    ))
                .build()


        }

    }

    fun constructConfigScreen(screenBuilder: YetAnotherConfigLib.Builder): YetAnotherConfigLib.Builder {
        return screenBuilder
            .title(Component.literal(SCREEN_NAME))
            .category(ClientGeneral.populateCategory())
            .category(CommonFunMisc.populateCategory())
            .category(CommonBalance.populateCategory())
            .category(CommonDurabilityTweaks.populateCategory())

    }

}