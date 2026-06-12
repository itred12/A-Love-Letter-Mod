package com.itred.aloveletter.mixin;

import com.itred.aloveletter.mobeffect.CustomMobEffect;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalFloatRef;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    // TODO: Make this not overwrite Jump Boost if the Jump Boost bonus is greater, and/or make this combine with Jump Boost's bonus
    @ModifyReturnValue(method = "getJumpBoostPower", at = @At("TAIL"))
    private float aloveletter$flexibleJumpBoostFromStatusEffect(float original) {

        LivingEntity entity = (LivingEntity) (Object) this;
        return original + CustomMobEffect.Companion.jumpBoostPowerHandler(entity);
    }




    // Only do this after the second return– the entity isn't taking fall damage in the case of the first one anyways
    @Inject(method = "calculateFallDamage", at = @At(value = "RETURN", ordinal = 1))
    private void aloveletter$modifyFallDamageForJumpBoostLike(float pFallDistance, float pDamageMultiplier, CallbackInfoReturnable<Integer> cir, @Local(name = "f") LocalFloatRef f) {

        LivingEntity entity = (LivingEntity) (Object) this;

        // "f" is the extra blocks of safe fall distance that Jump Boost adds to
        f.set(f.get() + CustomMobEffect.Companion.jumpBoostFallResistanceHandler(entity));
    }
}
