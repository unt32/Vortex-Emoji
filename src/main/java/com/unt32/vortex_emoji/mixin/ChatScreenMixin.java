package com.unt32.vortex_emoji.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import com.unt32.vortex_emoji.client.VortexEmojiPanel;

@Mixin(ChatScreen.class)
public abstract class ChatScreenMixin extends Screen {

    @Shadow
    private EditBox input;

    private VortexEmojiPanel emojiPanel;

    protected ChatScreenMixin(Component component) {
        super(component);
    }

    protected <T extends AbstractWidget & Renderable & NarratableEntry> T addRenderableWrapper(T widget) {
        return this.addRenderableWidget(widget);
    }

    @Inject(method = "init", at = @At("TAIL"))
    protected void OnInit(CallbackInfo ci) {
        emojiPanel = new VortexEmojiPanel(text -> {
            if (this.input != null) {
                this.input.insertText(text);
            }
        });
        emojiPanel.init(this.width, this.height, this::addRenderableWrapper);
    }

    @Override
    public void setFocused(GuiEventListener listener) {
        if (this.input != null) {
            super.setFocused(this.input);
        } else {
            super.setFocused(listener);
        }
    }
}
