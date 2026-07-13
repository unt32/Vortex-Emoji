package com.unt32.vortex_emoji.mixin;

import net.minecraft.network.chat.Component;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractSignEditScreen;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.unt32.vortex_emoji.client.VortexEmojiPanel;

@Mixin(AbstractSignEditScreen.class)
public class AbstractSignEditScreenMixin extends Screen {

    private VortexEmojiPanel emojiPanel;

    protected AbstractSignEditScreenMixin(Component component) {
        super(component);
    }

    protected <T extends AbstractWidget & Renderable & NarratableEntry> T addRenderableWrapper(T widget) {
        return this.addRenderableWidget(widget);
    }

    @Inject(method = "init", at = @At("TAIL"))
    private void onInit(CallbackInfo ci) {
        this.emojiPanel = new VortexEmojiPanel(text -> {
            for (char c : text.toCharArray()) {
                this.charTyped(c, 0);
            }
        });
        this.emojiPanel.init(this.width, this.height, this::addRenderableWrapper);
    }
}