package com.itred.aloveletter.event.configurable.serverloadedworldevents

import com.itred.aloveletter.config.ALLConfig
import com.itred.aloveletter.event.configurable.IConfigurableEventHandler
import net.minecraft.util.RandomSource
import net.minecraftforge.eventbus.api.IEventBus
import kotlin.math.min

object BlueAxolotlTweaks : IConfigurableEventHandler {

    var nautralOdds: Int = 0
    var bredOdds: Int = 0

    var naturalSpawns: Boolean = false

    fun shouldModifyBredAxolotlOdds(): Boolean {
        return !(bredOdds == 1200 || bredOdds == 0)
    }


    fun gambleForBlueAxolotl(odds: Int, randomSource: RandomSource): Boolean {
        // random.nextInt includes 0,
        // so an odds of 1 means that it's actually a 1 in 2 chance, as its either 0 or 1
        // So I instead clamp it to one less than the odds, so it's truly 1 in N
        // Does it matter? ....Probably not, lol.
        return min(
            randomSource.nextInt(odds), odds - 1
        ) == 0
    }

    fun modifyBredAxolotlRoll(randomSource: RandomSource): Boolean {
        // Oooone last check that the odds arent 0 before passing it into a randomsource
        return if (bredOdds == 0) false else gambleForBlueAxolotl(bredOdds, randomSource)
    }

    fun disableMyBlueAxolotlInhibitors(randomSource: RandomSource): Boolean {
        if (!naturalSpawns || nautralOdds == 0) { // An error is thrown if the odds are 0, which they will be if its somehow uninitalized when this is called
            return false
        }

        return gambleForBlueAxolotl(nautralOdds, randomSource)
    }


    override var isEnabled: Boolean = false

    override fun shouldEnable(): Boolean {
        return ALLConfig.COMMON_CONFIG.blueAxolotNaturalSpawn.get()
                || ALLConfig.COMMON_CONFIG.blueAxolotBredSpawnChance.default != ALLConfig.COMMON_CONFIG.blueAxolotBredSpawnChance.get()
    }

    override fun enable(modBus: IEventBus) {
        isEnabled = true

        naturalSpawns = ALLConfig.COMMON_CONFIG.blueAxolotNaturalSpawn.get()
        nautralOdds = ALLConfig.COMMON_CONFIG.blueAxolotNaturalSpawnChance.get()
        bredOdds = ALLConfig.COMMON_CONFIG.blueAxolotBredSpawnChance.get()

    }

    override fun disable(modBus: IEventBus) {
        isEnabled = false
    }
}