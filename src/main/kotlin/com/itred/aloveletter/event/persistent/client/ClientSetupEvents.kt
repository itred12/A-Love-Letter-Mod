package com.itred.aloveletter.event.persistent.client

import com.itred.aloveletter.ALoveLetter
import com.itred.aloveletter.mobeffect.HyperactiveStatusEffect
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.alchemy.PotionUtils
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = ALoveLetter.MODID, value = [Dist.CLIENT])
object ClientSetupEvents {


    @SubscribeEvent
    fun onClientSetup(event: FMLClientSetupEvent) {



        event.enqueueWork() {
            /* Removed for now
            ItemProperties.register(
                Items.POTION,
                // Used to have different textures for potions from the mod.
                // Possibly a bit invasive... I'll have to think of a better way to do this.
                ALoveLetter.modLoc("allpotioneffect"),
                ClientSetupEvents::getALLEffect
            )
            */
        }

    }


    // Use a custom texture for potions if the sole effect in the potion is Hyperactive (may expand later)
    fun getALLEffect(item: ItemStack, level: ClientLevel?, player: LivingEntity?, seed: Int): Float {
        val effects: List<MobEffectInstance> = PotionUtils.getMobEffects(item)
        return if (effects.size == 1 && effects[0].effect is HyperactiveStatusEffect) 1F else 0F
    }


}