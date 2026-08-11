package com.unt32.vortex_emoji.item;

import com.unt32.vortex_emoji.VortexEmojiMod;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeModTabs {
  public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister
      .create(Registries.CREATIVE_MODE_TAB, VortexEmojiMod.MODID);

  public static final RegistryObject<CreativeModeTab> VORTEX_EMOJI_TAB = CREATIVE_MODE_TABS.register("vortex_emoji_tab",
      () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.getStickerById(12)))
          .title(Component.translatable("creativetab.vortex_emoji_tab"))
          .displayItems((pParameters, pOutput) -> {
            for (var item : ModItems.ITEMS.getEntries()) {
              pOutput.accept(item.get());
            }
          })
          .build());

  public static void register(IEventBus eventBus) {
    CREATIVE_MODE_TABS.register(eventBus);
  }
}
