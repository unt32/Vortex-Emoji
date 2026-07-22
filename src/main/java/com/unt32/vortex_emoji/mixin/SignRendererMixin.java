package com.unt32.vortex_emoji.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.SignRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.SignText;

@Mixin(SignRenderer.class)
public class SignRendererMixin {
    @Inject(method = "renderSignText", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/blockentity/SignRenderer;translateSignText(Lcom/mojang/blaze3d/vertex/PoseStack;ZLnet/minecraft/world/phys/Vec3;)V", shift = At.Shift.AFTER))
    private void moveSignTextLower(BlockPos pPos, SignText pText, PoseStack pPoseStack, MultiBufferSource pBufferSource,
            int pPackedLight, int pLineHeight, int pMaxLineWidth, boolean pIsFrontText, CallbackInfo ci) {
        pPoseStack.translate(0.0F, 2.5F, 0.0F);
    }
}