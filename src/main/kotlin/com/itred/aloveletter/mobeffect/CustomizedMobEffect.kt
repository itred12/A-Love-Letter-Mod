package com.itred.aloveletter.mobeffect

import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffectCategory

open class CustomizedMobEffect(category: MobEffectCategory, color: Int) : MobEffect(category, color) {

    protected open val effectTickInterval = 1

    override fun isDurationEffectTick(duration: Int, amplifier: Int): Boolean {

        if (effectTickInterval == 1) {
            return true
        }
        // int.shr() (shift-right) is equivalent to the >> operator
        // Perhaps temporary, but for now, the tick interval is divided by powers of 2 according to the amplifier
        // I.e., 50 -> 25 -> 12.5 -> 6 for amplifiers 0, 1, 2, 3 from an interval of 50
        // This is how Minecraft does it, but I may swap it out for another system. (though this is done this way since. yknow. running every tick, so I should be careful)
        val shiftedInterval = effectTickInterval.shr(amplifier)
        if (shiftedInterval > 0) {
            return duration % shiftedInterval == 0
        } else {
            return true
        }

    }


}