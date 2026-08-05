package com.unt32.vortex_emoji.entity;

import com.unt32.vortex_emoji.ModSounds;
import com.unt32.vortex_emoji.VortexEmojiMod;
import com.unt32.vortex_emoji.item.ModItems;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class EmojiEntity extends Entity {
  private static final EntityDataAccessor<String> EMOJI_CHAR = SynchedEntityData.defineId(EmojiEntity.class,
      EntityDataSerializers.STRING);

  public EmojiEntity(EntityType<?> entityType, Level level) {
    super(entityType, level);
    this.noPhysics = true;
  }

  @Override
  protected void defineSynchedData() {
    this.entityData.define(EMOJI_CHAR, "");
  }

  public void setEmojiChar(char c) {
    this.entityData.set(EMOJI_CHAR, String.valueOf(c));
  }

  public String getEmojiChar() {
    return this.entityData.get(EMOJI_CHAR);
  }

  @Override
  public void tick() {
    super.tick();
    this.setDeltaMovement(0, 0, 0);

    BlockPos spaceBlockPos = BlockPos.containing(this.position().add(this.getLookAngle().scale(0.5)));
    BlockPos supportBlockPos = BlockPos.containing(this.position().add(this.getLookAngle().scale(-0.5)));

    if (!this.level().isClientSide()) {
      if (this.level().getBlockState(supportBlockPos).isAir() || !this.level().getBlockState(spaceBlockPos).isAir()
          || this.isInWater() || this.isOnFire()) {
        this.remove(RemovalReason.KILLED);
      }
    }
  }

  @Override
  public boolean isPickable() {
    return false;
  }

  @Override
  public boolean isPushable() {
    return false;
  }

  @Override
  public boolean canBeCollidedWith() {
    return false;
  }

  @Override
  protected void readAdditionalSaveData(CompoundTag tag) {
    if (tag.contains("EmojiChar")) {
      this.setEmojiChar(tag.getString("EmojiChar").charAt(0));
    }
  }

  @Override
  protected void addAdditionalSaveData(CompoundTag tag) {
    tag.putString("EmojiChar", this.getEmojiChar());
  }

  @Override
  public void remove(Entity.RemovalReason reason) {
    if (!this.level().isClientSide()) {
      Item itemToDrop = ModItems.STICKER_ITEMS.get(VortexEmojiMod.emojiFontConfig.indexOfKey(this.getEmojiChar()))
          .get();
      this.spawnAtLocation(new ItemStack(itemToDrop));

      this.level().playSound(null, this.getX(), this.getY(), this.getZ(),
          ModSounds.VORTEX_STICKER_TEAR.get(),
          SoundSource.PLAYERS, 1.0F, 1.0F);
    }
    super.remove(reason);
  }
}
