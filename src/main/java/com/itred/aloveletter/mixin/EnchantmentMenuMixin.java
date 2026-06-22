package com.itred.aloveletter.mixin;

import com.itred.aloveletter.registrar.ALLItems;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.EnchantmentMenu;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EnchantmentMenu.class)
public abstract class EnchantmentMenuMixin {


    @Shadow
    @Final
    private Container enchantSlots;

    // Swap wax paper with enchanted parchment on enchant, identical to how its done with books/enchanted books
    @Inject(method = "lambda$clickMenuButton$1", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;onEnchantmentPerformed(Lnet/minecraft/world/item/ItemStack;I)V"))
    private void aloveletter$transmuteWaxPaperIntoParchmentOnEnchant(ItemStack itemstack, int pId, Player pPlayer, int i, ItemStack itemstack1, Level p_39481_, BlockPos p_39482_, CallbackInfo ci, @Local(name = "itemStack2", ordinal = 2) LocalRef<ItemStack> itemStack2) {

        if (itemstack.is(ALLItems.INSTANCE.getWAX_PAPER())) {

            ItemStack parchmentItemStack = new ItemStack(ALLItems.INSTANCE.getENCHANTED_PARCHMENT());
            CompoundTag nbt = itemstack.getTag();
            if (nbt != null) {
                parchmentItemStack.setTag(nbt);
            }

            itemStack2.set(parchmentItemStack);
            this.enchantSlots.setItem(0, itemStack2.get());

        }

    }

    // Use the EnchantedBookItem method for applying enchants for enchanted parchment, to store them in the item properly
    @WrapOperation(method = "lambda$clickMenuButton$1", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;enchant(Lnet/minecraft/world/item/enchantment/Enchantment;I)V"))
    private void aloveletter$applyParchmentEnchantsProper(ItemStack instance, Enchantment pEnchantment, int pLevel, Operation<Void> original, @Local EnchantmentInstance enchantmentInstance) {
        if (instance.is(ALLItems.INSTANCE.getENCHANTED_PARCHMENT())) {
            EnchantedBookItem.addEnchantment(instance, enchantmentInstance);
        }
        original.call(instance, pEnchantment, pLevel);

    }
}
