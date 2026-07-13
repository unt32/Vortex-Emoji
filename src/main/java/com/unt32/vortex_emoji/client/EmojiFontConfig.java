package com.unt32.vortex_emoji.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;

public final class EmojiFontConfig {

    private static final String DEFAULT_FONT_PATH = "/assets/minecraft/font/default.json";
    private static final String TOOLTIPS_PATH = "/assets/vortex_emoji/tooltips.txt";


    public static final Tooltip NULL_TOOLTIP = Tooltip.create(Component.literal(""));

    private final int EMOJI_KEY_COUNT;
    private final Tooltip[] KEY_TOOLTIPS;

    public EmojiFontConfig() {
        this.KEY_TOOLTIPS = loadTooltips();
        this.EMOJI_KEY_COUNT = loadEmojiKeyCount();
    }

    public int emoji_key_count() {
        return EMOJI_KEY_COUNT;
    }

    public Tooltip[] tooltips() {
        return KEY_TOOLTIPS.clone();
    }

    private static int loadEmojiKeyCount() {
        int count = 0;
        try (InputStream is = EmojiFontConfig.class.getResourceAsStream(DEFAULT_FONT_PATH)) {
            if (is != null) {
                JsonElement root = JsonParser.parseReader(new InputStreamReader(is, StandardCharsets.UTF_8));
                JsonObject obj = root.getAsJsonObject();
                JsonArray providers = obj.getAsJsonArray("providers");
                if (providers != null && providers.size() > 0) {
                    JsonObject provider = providers.get(0).getAsJsonObject();
                    JsonArray chars = provider.getAsJsonArray("chars");
                    if (chars != null) {
                        for (JsonElement e : chars) {
                            String s = e.getAsString();
                            // count code points in the string (handles multiple codepoints per entry)
                            count += s.codePointCount(0, s.length());
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return count;
    }

    private static Tooltip[] loadTooltips() {
        List<Tooltip> tooltips = new ArrayList<>();

        try (InputStream inputStream = EmojiFontConfig.class.getResourceAsStream(TOOLTIPS_PATH)) {
            if (inputStream == null) {
                throw new IOException("Tooltip file not found: " + TOOLTIPS_PATH);
            }

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    line = line.trim();
                    if (!line.isEmpty()) {
                        if ("null".equals(line)) {
                            tooltips.add(NULL_TOOLTIP);
                        } else {
                            tooltips.add(Tooltip.create(Component.literal(line)));
                        }
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load tooltips from " + TOOLTIPS_PATH, e);
        }

        return tooltips.toArray(new Tooltip[0]);
    }
}
