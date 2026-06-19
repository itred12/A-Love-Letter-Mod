package com.itred.aloveletter.mixin;

import com.itred.aloveletter.mobeffect.CustomMobEffect;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Player.class)
public class PlayerMixin {

    @WrapOperation(method = "causeFoodExhaustion", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/food/FoodData;addExhaustion(F)V"))
    private void aloveletter$statusEffectFoodExhaustionHandler(FoodData instance, float pExhaustion, Operation<Void> original) {
        Player player = (Player) (Object) this;

        // In case other mods add behavior here, call the method but just with 0 exhaustion if we wish to prevent exhaustion
        if (CustomMobEffect.Companion.preventExhaustionHandler(player, pExhaustion)) {
            original.call(instance, 0.0F);
        } else {
            original.call(instance, pExhaustion);
        }
    }


}
