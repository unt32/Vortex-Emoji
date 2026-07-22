package com.unt32.vortex_emoji.mixin;

import net.minecraft.network.chat.Component;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarrationElementOutput;
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

    private AbstractWidget focusStealer;

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

        this.focusStealer = new AbstractWidget(0, 0, 0, 0, Component.empty()) {
            @Override
            protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
            }

            @Override
            protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
            }

            @Override
            public boolean mouseClicked(double mouseX, double mouseY, int button) {
                return false;
            }

            @Override
            public boolean isMouseOver(double mouseX, double mouseY) {
                return false;
            }
        };
        this.addRenderableWidget(this.focusStealer);
    }

    @Override
    public void setFocused(GuiEventListener listener) {
        if (this.focusStealer != null) {
            super.setFocused(this.focusStealer);
        } else {
            super.setFocused(listener);
        }
    }

    @Inject(method = "renderSignText(Lnet/minecraft/client/gui/GuiGraphics;)V", at = @At("HEAD"))
    private void moveSignTextLower(GuiGraphics guiGraphics, CallbackInfo ci) {
        guiGraphics.pose().translate(0.0F, 2.5F, 0.0F);
    }
}