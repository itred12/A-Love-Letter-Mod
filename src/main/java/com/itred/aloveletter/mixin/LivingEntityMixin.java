package com.itred.aloveletter.mixin;

import com.itred.aloveletter.event.configurable.serverloadedworldevents.CrossbowTweaks;
import com.itred.aloveletter.mobeffect.CustomMobEffect;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalFloatRef;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
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

    @ModifyExpressionValue(method = "hurt", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/damagesource/DamageSource;is(Lnet/minecraft/tags/TagKey;)Z"))
    private boolean aloveletter$modifyInvulnerabilityOnMultishotArrowHit(boolean original, @Local(name = "pSource") DamageSource pSource) {

        if (!CrossbowTweaks.INSTANCE.getMultishotTweaks()) {
            return original;
        }

        Entity entity = pSource.getDirectEntity();
        if (entity instanceof AbstractArrow arrowEntity
                && arrowEntity.shotFromCrossbow()
        ) {
            // If either the original check (damage type is in ignoresInvulnerability tag) or the arrow is from a multishot crossbow
            return original || arrowEntity.getTags().contains(CrossbowTweaks.TAG_ID);
        }

        return original;
    }

}
