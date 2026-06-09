package com.itred.aloveletter.event.configurable.server

import com.itred.aloveletter.ALoveLetter
import com.itred.aloveletter.config.ALLConfig
import com.itred.aloveletter.event.configurable.IConfigurableEventHandler
import com.itred.aloveletter.registrar.ALLSounds
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundSource
import net.minecraft.util.ByIdMap
import net.minecraft.world.entity.animal.axolotl.Axolotl
import net.minecraftforge.event.TickEvent
import net.minecraftforge.event.entity.EntityJoinLevelEvent
import net.minecraftforge.event.entity.EntityLeaveLevelEvent
import net.minecraftforge.eventbus.api.IEventBus
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.LogicalSide
import thedarkcolour.kotlinforforge.forge.FORGE_BUS
import java.util.*
import java.util.function.IntFunction
import java.util.function.Supplier
import kotlin.math.max

object BlueAxolotlPing : IConfigurableEventHandler {

    private var storedAxolotls: MutableList<Axolotl> = mutableListOf()
    private var COUNTER: Int = 0

    // We only want to play the sound effect once the player gets close to a blue Axolotl– onEntityTracked allows us to do this purely from the client's side.
    // Buuuuut, when we first start tracking an entity, there's a real chance we do so *as* is spawns in, like if it spawns in range of the player.
    // If this happens, its variant wont be initalized yet, so we want to store every axolotl that spawns on the client's side,
        // and only when we go to check the player's distance can we verify their variant and toss out any non-blue ones

    @SubscribeEvent
    fun onEntityLoad(event: EntityJoinLevelEvent) {
        val entity = event.entity

        // On the client side, EntityJoinLevelEvent and its sibling are both fired when the entity starts and stops being tracked on the client
        if (event.level.isClientSide() && entity is Axolotl) {
            storedAxolotls.add(entity)
        }

    }

    @SubscribeEvent
    fun onEntityUnload(event: EntityLeaveLevelEvent) {
        val entity = event.entity


        if (event.level.isClientSide() && entity is Axolotl) {
            storedAxolotls.remove(entity)
        }
    }

    @SubscribeEvent
    fun onPlayerTick(event: TickEvent.PlayerTickEvent) {


        // Honestly I'd rather not hook up this event at all sever-side, buuut I'm not sure if that's how it works.
        if (event.phase != TickEvent.Phase.START || event.side != LogicalSide.CLIENT) {
            return
        }

        val player = event.player

        COUNTER++

        if (COUNTER > 5) {

            val blueAxolotlPing = ALLConfig.CLIENT_CONIFG.blueAxolotlPingSFX.get()

            if (blueAxolotlPing == PingSoundEffect.NONE || blueAxolotlPing.getSoundEffect().isEmpty) {
                return
            }



            COUNTER = 0

            val level = player.level()


            // Perform operations on a copy of the table, since using an iterator could cause a one-in-a-million Access Violation exception
            // when de-loading an axolotl the instant we start looping through the list.
            val clonedStoredAxolotls = storedAxolotls.toList()

            // TODO: Particle effect, perhaps fix the dissonance by manually setting the counter?

            for (axolotl: Axolotl in clonedStoredAxolotls) {


                if (axolotl.isAlive && (axolotl.variant != Axolotl.Variant.BLUE)) {
                    storedAxolotls.remove(axolotl)
                    continue
                }

                val range = ALLConfig.SERVER_CONFIG.blueAxolotlPingRange.get()

                if (axolotl.distanceTo(player) <= range) {
                    level.playSound(
                        player,
                        axolotl.blockPosition(),
                        blueAxolotlPing.getSoundEffect().get().get(),
                        SoundSource.MASTER,
                        (max(16, range) / 16).toFloat() + 1.0f,
                        player.random.nextInt(90, 110).toFloat() / 100

                    )
                    ALoveLetter.LOGGER.info("Blue axolotl spawned!")
                    storedAxolotls.remove(axolotl)
                }


            }


        }

    }

    override var isEnabled: Boolean = false

    override fun shouldEnable(): Boolean {
        return ALLConfig.SERVER_CONFIG.blueAxolotPingMasterSwitch.get()
    }

    override fun enable(modBus: IEventBus) {
        isEnabled = true
        FORGE_BUS.register(this)
    }

    override fun disable(modBus: IEventBus) {
        isEnabled = false
        FORGE_BUS.unregister(this)
    }

    enum class PingSoundEffect {

        NONE(0, null),
        BW(1, ALLSounds.BLUEAXOLOTL_BW),
        PLA(2, ALLSounds.BLUEAXOLOTL_PLA);

        companion object {
            val BY_ID: IntFunction<PingSoundEffect> = ByIdMap.continuous(
                { component -> component.id },
                PingSoundEffect.entries.toTypedArray(),
                ByIdMap.OutOfBoundsStrategy.ZERO)
        }

        private val id: Int
        private val soundEffect: Supplier<SoundEvent>?

        constructor(id: Int, soundEvent: Supplier<SoundEvent>?) {
            this.id = id
            this.soundEffect = soundEvent
        }

        fun getId(): Int {
            return this.id
        }

        fun getSoundEffect(): Optional<Supplier<SoundEvent>> {
            if (this.soundEffect != null) {
                return Optional.of(this.soundEffect)
            } else  {
                return Optional.empty()
            }
        }
    }


}