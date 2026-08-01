package com.unt32.vortex_emoji.client;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import com.unt32.vortex_emoji.EmojiFontConfig;
import com.unt32.vortex_emoji.VortexEmojiMod;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.layouts.GridLayout.RowHelper;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;

public class VortexEmojiPanel {

    private static final int BUTTON_WIDTH = 20;
    private static final int BUTTON_HEIGHT = BUTTON_WIDTH;
    private static final int TEXT_HEIGHT = 8;

    private static final int PADDING = 2;
    private static final int MARGIN = 2;

    private static final int COLUMN_COUNT = 7;

    private static final int PANEL_BACKGROUND_COLOR = 0xDD000000;
    private static final int BUTTON_HOVER_COLOR = 0x77FFFFFF;
    private static final int BUTTON_BACKGROUND_COLOR = 0x00000000;
    private static final int TEXT_COLOR = 0xFFFFFFFF;

    private static final EmojiFontConfig emojiFontConfig = VortexEmojiMod.emojiFontConfig;
    private static final int TOGGLE_KEY_INDEX = 0;

    private final Consumer<String> insertText;
    private final List<AbstractWidget> keyboardWidgets = new ArrayList<>();

    private PanelBackgroundWidget backgroundWidget;
    private GridLayout gridLayout;
    private CustomButton toggleButton;
    private boolean isVisible;

    public VortexEmojiPanel(Consumer<String> insertText, boolean forceVisible) {
        this.insertText = insertText;
        this.isVisible = forceVisible;
    }

    public VortexEmojiPanel(Consumer<String> insertText) {
        this(insertText, false);
    }

    public static int getMargin() {
        return MARGIN;
    }

    public void init(int screenWidth, int screenHeight, Consumer<AbstractWidget> widgetConsumer) {
        gridLayout = new GridLayout();
        gridLayout.defaultCellSetting().padding(0);

        RowHelper rowHelper = gridLayout.createRowHelper(COLUMN_COUNT);

        for (int i = 1; i < emojiFontConfig.tooltip_key_count(); i++) {
            if (emojiFontConfig.tooltip(i) == emojiFontConfig.NULL_TOOLTIP) {
                continue;
            }

            final int index = i;
            CustomButton button = new CustomButton(0, 0, BUTTON_WIDTH, BUTTON_HEIGHT, Component.literal(emojiFontConfig.keyStr(index)),
                    b -> {
                        insertText.accept(emojiFontConfig.keyStr(index));
                    }, BUTTON_BACKGROUND_COLOR, BUTTON_HOVER_COLOR);
            button.setTooltip(Tooltip.create(emojiFontConfig.tooltip(i)));
            rowHelper.addChild(button);
        }

        gridLayout.arrangeElements();
        gridLayout.setX(screenWidth - this.gridLayout.getWidth() - PADDING - MARGIN);
        gridLayout.setY(screenHeight - this.gridLayout.getHeight() - PADDING - MARGIN);

        backgroundWidget = new PanelBackgroundWidget(gridLayout.getX() - PADDING, gridLayout.getY() - PADDING,
                gridLayout.getWidth() + PADDING * 2, gridLayout.getHeight() + PADDING * 2);
        widgetConsumer.accept(backgroundWidget);

        keyboardWidgets.clear();
        gridLayout.visitWidgets(widget -> {
            widgetConsumer.accept(widget);
            keyboardWidgets.add(widget);
        });

        toggleButton = new CustomButton(screenWidth - BUTTON_WIDTH - MARGIN,
                screenHeight - BUTTON_HEIGHT - MARGIN, BUTTON_WIDTH,
                BUTTON_HEIGHT, Component.literal(emojiFontConfig.keyStr(TOGGLE_KEY_INDEX)), b -> toggleVisibility(!isVisible),
                PANEL_BACKGROUND_COLOR, BUTTON_HOVER_COLOR);
        widgetConsumer.accept(toggleButton);

        toggleVisibility(isVisible);
    }

    private void toggleVisibility(boolean visible) {
        isVisible = visible;

        if (backgroundWidget != null) {
            backgroundWidget.visible = visible;
            backgroundWidget.active = visible;
        }

        for (AbstractWidget widget : keyboardWidgets) {
            widget.visible = visible;
            widget.active = visible;
        }
    }

    private static class CustomButton extends Button {
        private int unhovered;
        private int hovered;

        public CustomButton(int x, int y, int width, int height, Component message, OnPress onPress, int unhovered,
                int hovered) {
            super(x, y, width, height, message, onPress, Button.DEFAULT_NARRATION);
            this.unhovered = unhovered;
            this.hovered = hovered;
        }

        @Override
        public boolean isFocused() {
            return false;
        }

        @Override
        protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
            int color = this.isHoveredOrFocused() ? hovered : unhovered;

            guiGraphics.fill(this.getX(), this.getY(), this.getX() + this.width, this.getY() + this.height, color);
            guiGraphics.drawCenteredString(Minecraft.getInstance().font, this.getMessage(),
                    this.getX() + this.width / 2, this.getY() + (this.height - TEXT_HEIGHT) / 2, TEXT_COLOR);
        }
    }

    private static class PanelBackgroundWidget extends AbstractWidget {
        public PanelBackgroundWidget(int x, int y, int width, int height) {
            super(x, y, width, height, Component.empty());
        }

        @Override
        protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
            guiGraphics.fill(this.getX(), this.getY(), this.getX() + this.width, this.getY() + this.height,
                    PANEL_BACKGROUND_COLOR);
        }

        @Override
        protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            return false;
        }

        @Override
        public boolean isMouseOver(double mouseX, double mouseY) {
            return false;
        }
    }
}