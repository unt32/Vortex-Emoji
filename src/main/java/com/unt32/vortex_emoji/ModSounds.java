package com.unt32.vortex_emoji;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModSounds {

  public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS,
      VortexEmojiMod.MODID);

  public static final RegistryObject<SoundEvent> VORTEX_STICKER_PLACE = registerSoundEvent("vortex_sticker_place");
  public static final RegistryObject<SoundEvent> VORTEX_STICKER_TEAR = registerSoundEvent("vortex_sticker_tear");

  private static RegistryObject<SoundEvent> registerSoundEvent(String name) {
    return SOUND_EVENTS.register(name,
        () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(VortexEmojiMod.MODID, name)));
  }

  public static void register(IEventBus eventBus) {
    SOUND_EVENTS.register(eventBus);
  }
}
