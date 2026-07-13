package com.unt32.vortex_emoji.mixin;

import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(targets = "net.minecraft.client.gui.Font$StringRenderOutput")
public abstract class StringRenderOutputMixin {

    private static final TextColor EMOJI_COLOR = TextColor.fromRgb(0xFFFFFF);

    @Redirect(method = "accept", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/chat/Style;getColor()Lnet/minecraft/network/chat/TextColor;"))
    private TextColor overrideEmojiColor(Style instance, int index, Style style, int codePoint) {
        if (codePoint >= '\uE000' && codePoint <= '\uE040') {
            return EMOJI_COLOR;
        }
        return instance.getColor();
    }
}