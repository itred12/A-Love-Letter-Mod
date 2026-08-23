package com.itred.aloveletter.mixin;

import com.itred.aloveletter.event.configurable.serverloadedworldevents.CrossbowTweaks;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(CrossbowItem.class)
public class CrossbowItemMixin {

    @ModifyReturnValue(method = "getArrow", at = @At("RETURN"))
    private static AbstractArrow aloveletter$storeArrowMultishot(AbstractArrow original, @Local(name = "pCrossbowStack") ItemStack pCrossbowStack) {
        if (CrossbowTweaks.INSTANCE.getMultishotTweaks() && pCrossbowStack.getEnchantmentLevel(Enchantments.MULTISHOT) > 0) {
            original.addTag(CrossbowTweaks.TAG_ID);
        }
        return original;
    }
}
