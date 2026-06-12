package com.itred.aloveletter.datagen.data.registry

import com.itred.aloveletter.ALoveLetter
import net.minecraft.core.Registry
import net.minecraft.data.worldgen.BootstapContext
import net.minecraft.resources.ResourceKey

abstract class AbstractPackRegistrar<T>(val registryKey: ResourceKey<Registry<T>>) {

    abstract fun bootstrap(context: BootstapContext<T>)

    protected fun createKey(name: String): ResourceKey<T> {
        return ResourceKey.create(registryKey, ALoveLetter.modLoc(name))
    }

}