package com.itred.aloveletter.mixin;

import com.itred.aloveletter.registrar.ALLItems;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.world.inventory.GrindstoneMenu;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(GrindstoneMenu.class)
public class GrindstoneMenuMixin {

    @ModifyReturnValue(method = "removeNonCurses", at = @At("TAIL"))
    private static ItemStack aloveletter$transmuteParchmentBackIntoPaper(ItemStack original) {

        // If the item is enchanted parchment,
        if (original.is(ALLItems.INSTANCE.getENCHANTED_PARCHMENT())) {

            // And the grindstone removed all enchantments from it,
            if (original.getAllEnchantments().isEmpty()) {

                // Then we make a new wax paper stack, with the same count (which should always be 1 anyways)
                ItemStack waxPaperStack = new ItemStack(ALLItems.INSTANCE.getWAX_PAPER(), original.getCount());

                // Copy over the custom name, if it has one
                if (original.hasCustomHoverName()) {
                    waxPaperStack.setHoverName(original.getHoverName());
                }

                // And return the new stack
                return waxPaperStack;
            }

        }

        return original;
    }
}
