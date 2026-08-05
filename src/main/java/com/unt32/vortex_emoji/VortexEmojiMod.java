package com.unt32.vortex_emoji;

import com.unt32.vortex_emoji.entity.ModEntities;
import com.unt32.vortex_emoji.item.ModCreativeModTabs;
import com.unt32.vortex_emoji.item.ModItems;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(VortexEmojiMod.MODID)
public class VortexEmojiMod {
  public static final String MODID = "vortex_emoji";

  public static final EmojiFontConfig emojiFontConfig = new EmojiFontConfig();

  public VortexEmojiMod(FMLJavaModLoadingContext context) {
    IEventBus modEventBus = context.getModEventBus();

    ModEntities.register(modEventBus);
    ModItems.register(modEventBus);
    ModSounds.register(modEventBus);
    ModCreativeModTabs.register(modEventBus);

    modEventBus.addListener(this::addCreative);

    DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> new ClientInit(modEventBus));
  }

  private void addCreative(BuildCreativeModeTabContentsEvent event) {
    if (event.getTabKey() == ModCreativeModTabs.VORTEX_EMOJI_TAB.getKey()) {
      for (var item : ModItems.ITEMS.getEntries()) {
        event.accept(item);
      }
    }
  }
}
