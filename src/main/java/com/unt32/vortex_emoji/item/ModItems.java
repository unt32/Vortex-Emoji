package com.unt32.vortex_emoji.item;

import java.util.ArrayList;
import java.util.List;

import com.unt32.vortex_emoji.VortexEmojiMod;

import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS,
            VortexEmojiMod.MODID);

    public static List<RegistryObject<Item>> STICKER_ITEMS = new ArrayList<>();

    static {
        RegistryObject<Item> sticker = ITEMS.register("000", () -> new EraserItem(
                new Item.Properties(),
                VortexEmojiMod.emojiFontConfig.key(0)));
        STICKER_ITEMS.add(sticker);

        int count = VortexEmojiMod.emojiFontConfig.emoji_key_count();
        for (int i = 1; i < count; i++) {
            final int index = i;
            String name = String.format("%03d", index);

            sticker = ITEMS.register(name,
                    () -> new StickerItem(
                            new Item.Properties(),
                            VortexEmojiMod.emojiFontConfig.key(index)));

            STICKER_ITEMS.add(sticker);
        }
    }

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}