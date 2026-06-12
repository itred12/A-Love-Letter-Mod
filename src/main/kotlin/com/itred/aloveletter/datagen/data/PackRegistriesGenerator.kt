package com.itred.aloveletter.datagen.data

import com.itred.aloveletter.ALoveLetter
import com.itred.aloveletter.datagen.data.registry.DamageTypeRegistryProvider
import net.minecraft.core.HolderLookup
import net.minecraft.core.RegistrySetBuilder
import net.minecraft.core.registries.Registries
import net.minecraft.data.PackOutput
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider
import java.util.concurrent.CompletableFuture

class PackRegistriesGenerator(pack: PackOutput, lookupProvider: CompletableFuture<HolderLookup.Provider>):
    DatapackBuiltinEntriesProvider(pack, lookupProvider, BUILDER, setOf(ALoveLetter.MODID))  {

    companion object {
        val BUILDER = RegistrySetBuilder()
            .add(Registries.DAMAGE_TYPE, DamageTypeRegistryProvider::bootstrap)
    }

}