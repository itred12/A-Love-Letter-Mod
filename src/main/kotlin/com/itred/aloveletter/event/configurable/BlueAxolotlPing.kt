package com.itred.aloveletter.event.configurable

import com.itred.aloveletter.config.ALLConfig
import com.itred.aloveletter.event.IConfigurableEventHandler
import net.minecraftforge.eventbus.api.IEventBus

object BlueAxolotlPing : IConfigurableEventHandler {


    enum class PingSoundEffect {
        NONE,
        BW,
        PLA
    }

    override fun shouldEnable(): Boolean {
        return ALLConfig.CLIENT_CONIFG.blueAxolotlPingSFX.get() != PingSoundEffect.NONE
    }

    override fun enable(modBus: IEventBus) {

    }

    override fun disable(modBus: IEventBus) {

    }
}