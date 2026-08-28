package com.itred.aloveletter.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.world.entity.projectile.AbstractArrow;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(AbstractArrow.class)
public class AbstractArrowMixin {

    // Overrode in ThrownTridentMixin, as it provides no implementation of `shoot` that I can wrap from
    @WrapMethod(method = "shoot")
    protected void aloveletter$onTridentShoot(double pX, double pY, double pZ, float pVelocity, float pInaccuracy, Operation<Void> original) {
        original.call(pX, pY, pZ, pVelocity, pInaccuracy);
    }
}
