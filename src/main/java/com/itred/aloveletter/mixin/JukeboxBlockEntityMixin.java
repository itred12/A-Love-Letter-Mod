package com.itred.aloveletter.mixin;

import com.itred.aloveletter.item.DisceryItem;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.JukeboxBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(JukeboxBlockEntity.class)
public class JukeboxBlockEntityMixin {

    @ModifyReturnValue(method = "removeItem", at = @At("RETURN"))
    private ItemStack aloveletter$playDiscerySFXWhenRemoved(ItemStack original) {
        JukeboxBlockEntity block = (JukeboxBlockEntity) (Object) this;

        if (block.getLevel() != null && !original.isEmpty() && original.getItem() instanceof DisceryItem) {
            DisceryItem.Companion.playRandomSoundInListWithRandomPitchOnServer(
                    DisceryItem.Companion.getREMOVE_JUKEBOX_SOUNDS(),
                    block.getLevel(),
                    block.getBlockPos().getCenter(),
                    block.getLevel().random,
                    1f
            );
        }
        return original;
    }



}
