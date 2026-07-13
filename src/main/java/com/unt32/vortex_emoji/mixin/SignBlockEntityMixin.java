package com.unt32.vortex_emoji.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import net.minecraft.world.level.block.entity.SignBlockEntity;

@Mixin(SignBlockEntity.class)
public class SignBlockEntityMixin {
    @Overwrite
    public int getTextLineHeight() {
        return 12;
    }
    @Overwrite
    public int getMaxTextLineWidth() {
        return 85;
    }
}
