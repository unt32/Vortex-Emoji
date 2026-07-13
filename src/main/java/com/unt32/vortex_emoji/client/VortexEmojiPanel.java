package com.unt32.vortex_emoji.client;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.layouts.GridLayout.RowHelper;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;

public class VortexEmojiPanel {

    private static final int BUTTON_WIDTH = 14;
    private static final int BUTTON_HEIGHT = BUTTON_WIDTH;
    private static final int PADDING = 2;
    private static final int MARGIN = 2;
    private static final int INPUT_HEIGHT = 12;
    private static final int COLUMN_COUNT = 7;
    private static final int PANEL_BACKGROUND_COLOR = 0xDD000000;
    private static final int BUTTON_HOVER_COLOR = 0xAAFFFFFF;
    private static final int BUTTON_BACKGROUND_COLOR = 0x00000000;
    private static final int TOGGLE_OPEN_COLOR = 0xBB660000;
    private static final int TOGGLE_CLOSED_COLOR = 0xBB000066;
    private static final int TEXT_COLOR = 0xFFFFFFFF;
    private static final int TEXT_HEIGHT = 8;

    private static final int TOGGLE_KEY_INDEX = 0;

    private static final EmojiFontConfig emojiFontConfig = new EmojiFontConfig();

    private final Consumer<String> insertText;
    private final List<AbstractWidget> keyboardWidgets = new ArrayList<>();

    private PanelBackgroundWidget backgroundWidget;
    private GridLayout gridLayout;
    private CustomButton toggleButton;
    private boolean isVisible;

    public VortexEmojiPanel(Consumer<String> insertText) {
        this.insertText = insertText;
    }

    public void init(int screenWidth, int screenHeight, Consumer<AbstractWidget> widgetConsumer) {
        if (emojiFontConfig.tooltips().length != emojiFontConfig.emoji_key_count()) {
            throw new IllegalStateException(
                    "Emoji key/tooltips count mismatch (" + emojiFontConfig.emoji_key_count() + " keys, "
                            + emojiFontConfig.tooltips().length + " tooltips)");
        }

        isVisible = false;

        gridLayout = new GridLayout();
        gridLayout.defaultCellSetting().padding(0);

        RowHelper rowHelper = gridLayout.createRowHelper(COLUMN_COUNT);

        for (int i = 1; i < emojiFontConfig.tooltips().length; i++) {
            if (emojiFontConfig.tooltips()[i] == emojiFontConfig.NULL_TOOLTIP) {
                continue;
            }

            final int index = i;
            CustomButton button = new CustomButton(0, 0, BUTTON_WIDTH, BUTTON_HEIGHT, Component.literal(key(index)),
                    b -> {
                        insertText.accept(key(index));
                    }, BUTTON_BACKGROUND_COLOR, BUTTON_HOVER_COLOR);
            button.setTooltip(emojiFontConfig.tooltips()[index]);
            rowHelper.addChild(button);
        }

        gridLayout.arrangeElements();
        gridLayout.setX(screenWidth - this.gridLayout.getWidth() - PADDING - MARGIN);
        gridLayout.setY(screenHeight - this.gridLayout.getHeight() - PADDING - 2 * MARGIN - INPUT_HEIGHT);

        backgroundWidget = new PanelBackgroundWidget(gridLayout.getX() - PADDING, gridLayout.getY() - PADDING,
                gridLayout.getWidth() + PADDING * 2, gridLayout.getHeight() + PADDING * 2);
        widgetConsumer.accept(backgroundWidget);

        keyboardWidgets.clear();
        gridLayout.visitWidgets(widget -> {
            widgetConsumer.accept(widget);
            keyboardWidgets.add(widget);
        });

        toggleButton = new CustomButton(screenWidth - BUTTON_WIDTH - MARGIN,
                screenHeight - BUTTON_HEIGHT - 2 * MARGIN - INPUT_HEIGHT, BUTTON_WIDTH,
                BUTTON_HEIGHT, Component.literal(key(TOGGLE_KEY_INDEX)), b -> toggleVisibility(!isVisible),
                TOGGLE_CLOSED_COLOR, BUTTON_HOVER_COLOR);
        widgetConsumer.accept(toggleButton);

        toggleVisibility(false);
    }

    private void toggleVisibility(boolean visible) {
        isVisible = visible;
        toggleButton.setUnhoveredColor(visible ? TOGGLE_OPEN_COLOR : TOGGLE_CLOSED_COLOR);

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

        void setUnhoveredColor(int color) {
            this.unhovered = color;
        }

        void setHoveredColor(int color) {
            this.hovered = color;
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

    private static String key(int index) {
        if (index < 0 || index >= emojiFontConfig.emoji_key_count()) {
            throw new IllegalArgumentException("Invalid key index: " + index);
        }
        return String.valueOf((char) ('\uE000' + index));
    }
}