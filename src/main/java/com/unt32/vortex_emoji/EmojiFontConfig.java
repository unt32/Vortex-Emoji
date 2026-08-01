package com.unt32.vortex_emoji;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;

public final class EmojiFontConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmojiFontConfig.class);
    private static final String DEFAULT_FONT_PATH = "/assets/minecraft/font/default.json";
    private static final String TOOLTIPS_PATH = "/assets/vortex_emoji/tooltips.txt";

    public static final Component NULL_TOOLTIP = Component.literal("");

    private final char[] EMOJI_KEYS;
    private final Component[] KEY_TOOLTIPS;

    public EmojiFontConfig() {
        this.KEY_TOOLTIPS = loadTooltips();
        this.EMOJI_KEYS = loadEmojiKeys();

        if (this.tooltip_key_count() != this.emoji_key_count()) {
            throw new IllegalStateException(
                    "Emoji key/tooltips count mismatch (" + this.emoji_key_count() + " keys, "
                            + this.tooltip_key_count() + " tooltips)");
        }

        LOGGER.info("EmojiFontConfig initialized successfully. Emoji count: {}", this.emoji_key_count());
    }

    public int emoji_key_count() {
        return EMOJI_KEYS.length;
    }

    public int tooltip_key_count() {
        return KEY_TOOLTIPS.length;
    }

    public Component tooltip(int index) {
        if (index < 0 || index >= this.tooltip_key_count()) {
            throw new IllegalArgumentException("Invalid tooltip index: " + index);
        }
        return KEY_TOOLTIPS[index];
    }

    private char[] loadEmojiKeys() {
        List<Character> charList = new ArrayList<>();

        try (InputStream is = EmojiFontConfig.class.getResourceAsStream(DEFAULT_FONT_PATH)) {
            if (is != null) {
                JsonElement root = JsonParser.parseReader(new InputStreamReader(is, StandardCharsets.UTF_8));
                if (root != null && root.isJsonObject()) {
                    JsonObject obj = root.getAsJsonObject();
                    JsonArray providers = obj.getAsJsonArray("providers");

                    if (providers != null) {
                        for (JsonElement providerElement : providers) {
                            if (!providerElement.isJsonObject())
                                continue;

                            JsonObject provider = providerElement.getAsJsonObject();
                            JsonArray chars = provider.getAsJsonArray("chars");

                            if (chars != null) {
                                for (JsonElement e : chars) {
                                    String s = e.getAsString();
                                    for (char c : s.toCharArray()) {
                                        charList.add(c);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        char[] result = new char[charList.size()];
        for (int i = 0; i < charList.size(); i++) {
            result[i] = charList.get(i);
        }

        return result;
    }

    private static Component[] loadTooltips() {
        List<Component> tooltips = new ArrayList<>();

        try (InputStream inputStream = EmojiFontConfig.class.getResourceAsStream(TOOLTIPS_PATH)) {
            if (inputStream == null) {
                throw new IOException("Tooltip file not found: " + TOOLTIPS_PATH);
            }

            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    line = line.trim();
                    if (!line.isEmpty()) {
                        if ("null".equals(line)) {
                            tooltips.add(NULL_TOOLTIP);
                        } else {
                            tooltips.add(Component.literal(line));
                        }
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load tooltips from " + TOOLTIPS_PATH, e);
        }

        return tooltips.toArray(new Component[0]);
    }

    public char key(int index) {
        if (index < 0 || index >= this.emoji_key_count()) {
            throw new IllegalArgumentException("Invalid key index: " + index);
        }
        return EMOJI_KEYS[index];
    }

    public String keyStr(int index) {
        return String.valueOf(key(index));
    }
}
