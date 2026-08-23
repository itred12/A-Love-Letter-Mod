package com.itred.aloveletter.mixin;

import com.itred.aloveletter.event.configurable.serverloadedworldevents.BlueAxolotlTweaks;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Axolotl.class)
public class AxolotlMixin {

    @WrapMethod(method = "useRareVariant")
    private static boolean aloveletter$modifyBlueAxolotlOdds(RandomSource pRandom, Operation<Boolean> original) {


        if (BlueAxolotlTweaks.INSTANCE.shouldModifyBredAxolotlOdds()) {
            return BlueAxolotlTweaks.INSTANCE.modifyBredAxolotlRoll(pRandom);
        }

        return original.call(pRandom);
    }

    @WrapOperation(method = "finalizeSpawn", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/animal/axolotl/Axolotl$Variant;getCommonSpawnVariant(Lnet/minecraft/util/RandomSource;)Lnet/minecraft/world/entity/animal/axolotl/Axolotl$Variant;"))
    private static Axolotl.Variant aloveletter$removeBlueAxolotlSpawnLimitation(RandomSource pRandom, Operation<Axolotl.Variant> original) {

        if (BlueAxolotlTweaks.INSTANCE.disableMyBlueAxolotlInhibitors(pRandom)) {
            return Axolotl.Variant.getRareSpawnVariant(pRandom);
        }

        return original.call(pRandom);
    }

}
