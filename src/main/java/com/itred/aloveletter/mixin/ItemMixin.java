package com.itred.aloveletter.mixin;

import com.itred.aloveletter.event.configurable.serverloadedworldevents.UnbreakableTiersItems;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Item.class)
public class ItemMixin {

    @ModifyReturnValue(method = "canBeDepleted", at = @At("RETURN"))
    private boolean aloveletter$preventUnbreakingAmongOtherThingsForUnbreakable(boolean original) {

        Item item = (Item) (Object) this;
        if (!UnbreakableTiersItems.INSTANCE.shouldDamage(item)) {
            return false;
        }
        return original;
    }

}
