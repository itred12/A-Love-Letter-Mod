package com.itred.aloveletter.mixin;

import com.itred.aloveletter.event.persistent.common.WhirlpoolEnchantmentImplEvents;
import com.itred.aloveletter.registrar.ALLEnchantments;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ThrownTrident.class)
public class ThrownTridentMixin extends AbstractArrowMixin {

    @Shadow
    private ItemStack tridentItem;

    @Override
    protected void aloveletter$onTridentShoot(double pX, double pY, double pZ, float pVelocity, float pInaccuracy, Operation<Void> original) {
        super.aloveletter$onTridentShoot(pX, pY, pZ, pVelocity, pInaccuracy, original);

        ThrownTrident trident = (ThrownTrident) (Object) this;

        if (tridentItem.getEnchantmentLevel(ALLEnchantments.INSTANCE.getWHIRLPOOL().get()) > 0) {
            WhirlpoolEnchantmentImplEvents.INSTANCE.onTridentThrow(trident, tridentItem);
        }


    }

}


