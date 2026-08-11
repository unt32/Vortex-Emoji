package com.unt32.vortex_emoji.entity;

import com.unt32.vortex_emoji.VortexEmojiMod;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {
  public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister
      .create(ForgeRegistries.ENTITY_TYPES, VortexEmojiMod.MODID);

  public static final RegistryObject<EntityType<EmojiEntity>> EMOJI_ENTITY = ENTITY_TYPES.register("emoji_entity",
      () -> EntityType.Builder.<EmojiEntity>of(EmojiEntity::new, MobCategory.MISC)
          .sized(0.0F, 0.0F)
          .clientTrackingRange(10)
          .updateInterval(20)
          .build("emoji_entity"));

  public static void register(IEventBus eventBus) {
    ENTITY_TYPES.register(eventBus);
  }
}
