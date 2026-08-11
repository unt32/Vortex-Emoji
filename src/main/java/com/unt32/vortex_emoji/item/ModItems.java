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

  private static List<RegistryObject<Item>> ITEMS_LIST;

  static {
    for (int i = 1; i < VortexEmojiMod.emojiFontConfig.emoji_key_count(); i++) {
      final int index = i;
      String name = String.format("%03d", index);

      ITEMS.register(name,
          () -> new StickerItem(
              new Item.Properties(),
              VortexEmojiMod.emojiFontConfig.key(index)));
    }
    ITEMS_LIST = List.copyOf(ITEMS.getEntries());
  }

  public static Item getStickerById(int id) {
    if (id < 1 || id > ITEMS_LIST.size())
      throw new IllegalArgumentException("Invalid sticker id: " + id + " (expected 1.." + ITEMS_LIST.size() + ")");
    return ITEMS_LIST.get(id - 1).get();
  }

  public static void register(IEventBus eventBus) {
    ITEMS.register(eventBus);
  }
}
