package com.itred.aloveletter.mixin;

import com.itred.aloveletter.registrar.ALLItems;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.nbt.ListTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin {

    // Return after the first enchantment is applied, before any further enchantments are applied, if the item is wax paper
    @Inject(method = "selectEnchantment", at = @At(value = "INVOKE", target = "Ljava/util/Optional;ifPresent(Ljava/util/function/Consumer;)V", shift = At.Shift.AFTER), cancellable = true)
    private static void aloveletter$waxPaperOnlyOneEnchantment(RandomSource pRandom, ItemStack pItemStack, int pLevel, boolean pAllowTreasure, CallbackInfoReturnable<List<EnchantmentInstance>> cir, @Local(name = "list") List<EnchantmentInstance> list) {
        if (pItemStack.is(ALLItems.INSTANCE.getWAX_PAPER())) {
            cir.setReturnValue(list);
        }
    }

    // Ensure that wax paper uses the same StoredEnchantments component instead of the usual Enchantments component
    @WrapOperation(method = "getEnchantments", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;getEnchantmentTags()Lnet/minecraft/nbt/ListTag;"))
    private static ListTag aloveletter$waxPaperStoreEnchantments(ItemStack instance, Operation<ListTag> original) {

        if (instance.is(ALLItems.INSTANCE.getENCHANTED_PARCHMENT())) {
            return EnchantedBookItem.getEnchantments(instance);
        }

        return original.call(instance);
    }


    // Modify the checks for enchanted books for setting and handling the enchantment NBT tag to also include enchanted parchment
    @ModifyExpressionValue(method = "setEnchantments", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z", ordinal = 0))
    private static boolean aloveletter$waxPaperSetEnchantments(boolean original, @Local(argsOnly = true) ItemStack pStack) {
        return original || pStack.is(ALLItems.INSTANCE.getENCHANTED_PARCHMENT());
    }

    @ModifyExpressionValue(method = "setEnchantments", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z", ordinal = 1))
    private static boolean aloveletter$waxPaperProperNBTHandling(boolean original, @Local(argsOnly = true) ItemStack pStack) {
        return original || pStack.is(ALLItems.INSTANCE.getENCHANTED_PARCHMENT());
    }

}
