package com.unt32.vortex_emoji.mixin;

import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.util.FormattedCharSequence;

import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Font.class)
public abstract class FontMixin {
    @Shadow
    public abstract int drawInBatch(FormattedCharSequence p_273262_, float p_273006_, float p_273254_, int p_273375_,
            boolean p_273674_, Matrix4f p_273525_, MultiBufferSource p_272624_, Font.DisplayMode p_273418_,
            int p_273330_, int p_272981_);

    @Overwrite
    public void drawInBatch8xOutline(FormattedCharSequence text, float x, float y, int color,
            int outlineColor, Matrix4f pose, MultiBufferSource bufferSource, int packedLight) {
        drawInBatch(text, x, y, color, false, pose, bufferSource,
                Font.DisplayMode.NORMAL, 0, packedLight);
    }
}