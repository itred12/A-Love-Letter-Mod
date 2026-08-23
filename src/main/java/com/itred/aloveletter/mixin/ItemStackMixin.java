package com.itred.aloveletter.mixin;

import com.itred.aloveletter.event.configurable.serverloadedworldevents.UnbreakableTiersItems;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ItemStack.class)
public class ItemStackMixin {

    @ModifyReturnValue(method = "isDamageableItem", at = @At(value = "RETURN", ordinal = 0))
    private boolean aloveletter$wrapIsDamagableItem(boolean original) {

        ItemStack itemStack = (ItemStack) (Object) this;

        if (UnbreakableTiersItems.INSTANCE.shouldDamage(itemStack)) {
            return original;
        } else {
            return false;
        }


    }
}
