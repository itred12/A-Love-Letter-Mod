package com.itred.aloveletter.event.configurable

import net.minecraftforge.eventbus.api.IEventBus

interface IConfigurableEventHandler {
    var isEnabled: Boolean
    fun shouldEnable(): Boolean {
        return true
    }
    fun enable(modBus: IEventBus)
    fun disable(modBus: IEventBus)
}