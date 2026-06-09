package com.itred.aloveletter.event

import net.minecraftforge.eventbus.api.IEventBus

interface IConfigurableEventHandler {
    fun shouldEnable(): Boolean {
        return true
    }
    fun enable(modBus: IEventBus)
    fun disable(modBus: IEventBus)
}