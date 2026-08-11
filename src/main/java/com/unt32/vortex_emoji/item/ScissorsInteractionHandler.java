package com.unt32.vortex_emoji.item;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.unt32.vortex_emoji.VortexEmojiMod;
import com.unt32.vortex_emoji.entity.EmojiEntity;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = VortexEmojiMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)

public class ScissorsInteractionHandler {
  static private EmojiEntity getEmojiHit(Level level, Vec3 eyePosition, Vec3 endPosition,
      AABB searchArea) {
    List<EmojiEntity> candidates = level.getEntitiesOfClass(EmojiEntity.class, searchArea);

    for (EmojiEntity candidate : candidates) {
      AABB hitbox = candidate.getBoundingBox();

      Optional<Vec3> intersection = hitbox.clip(eyePosition, endPosition);

      if (!intersection.isEmpty())
        return candidate;
    }

    return null;
  }

  private static final Set<ResourceLocation> EXTRA_SHEARS = Set.of(
      new ResourceLocation("botania", "manasteel_shears"),
      new ResourceLocation("botania", "elementium_shears"),
      new ResourceLocation("aiotbotania", "livingwood_shears"),
      new ResourceLocation("aiotbotania", "livingrock_shears"));

  private static boolean isShears(ItemStack stack) {
    if (stack.is(Items.SHEARS)) {
      return true;
    }

    Item item = stack.getItem();
    ResourceLocation key = ForgeRegistries.ITEMS.getKey(item);

    return key != null && EXTRA_SHEARS.contains(key);
  }

  @SubscribeEvent
  public static void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
    if (!isShears(event.getItemStack())) {
      return;
    }

    Player player = event.getEntity();
    Level level = event.getLevel();

    Vec3 eyePosition = player.getEyePosition();
    Vec3 lookVector = player.getLookAngle();
    Vec3 endPosition = eyePosition.add(lookVector.scale(player.getEntityReach()));

    BlockHitResult blockHitResult = level
        .clip(new ClipContext(eyePosition, endPosition, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player));
    if (blockHitResult.getType() != HitResult.Type.MISS) {
      endPosition = blockHitResult.getLocation();
    }

    AABB searchArea = new AABB(eyePosition, endPosition);
    EmojiEntity emojiEntity = getEmojiHit(level, eyePosition, endPosition, searchArea);

    if (emojiEntity == null) {
      return;
    }

    if (!level.isClientSide()) {
      emojiEntity.remove(Entity.RemovalReason.KILLED);
    }

    event.setCanceled(true);
    event.setCancellationResult(InteractionResult.sidedSuccess(level.isClientSide()));
  }
}
