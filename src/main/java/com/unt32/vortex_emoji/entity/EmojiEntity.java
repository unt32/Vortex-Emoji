package com.unt32.vortex_emoji.entity;

import com.unt32.vortex_emoji.ModSounds;
import com.unt32.vortex_emoji.VortexEmojiMod;
import com.unt32.vortex_emoji.item.ModItems;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class EmojiEntity extends Entity {
  private static final EntityDataAccessor<String> EMOJI_CHAR = SynchedEntityData.defineId(EmojiEntity.class,
      EntityDataSerializers.STRING);

  private static final EntityDataAccessor<Direction> FACE = SynchedEntityData.defineId(
      EmojiEntity.class, EntityDataSerializers.DIRECTION);

  private static final float THICKNESS = 0.01F;
  private static final float SIZE = 0.3F;

  @Override
  public AABB makeBoundingBox() {
    Vec3 pos = this.position();
    Direction face = this.getFace();

    double halfSize = SIZE / 2.0;
    double halfThick = THICKNESS / 2.0;

    double xHalf = face.getAxis() == Direction.Axis.X ? halfThick : halfSize;
    double yHalf = face.getAxis() == Direction.Axis.Y ? halfThick : halfSize;
    double zHalf = face.getAxis() == Direction.Axis.Z ? halfThick : halfSize;

    return new AABB(
        pos.x - xHalf, pos.y - yHalf, pos.z - zHalf,
        pos.x + xHalf, pos.y + yHalf, pos.z + zHalf);
  }

  public EmojiEntity(EntityType<?> entityType, Level level) {
    super(entityType, level);
    this.noPhysics = true;
  }

  @Override
  protected void defineSynchedData() {
    this.entityData.define(EMOJI_CHAR, "");
    this.entityData.define(FACE, Direction.UP);
  }

  public void setEmojiChar(char c) {
    this.entityData.set(EMOJI_CHAR, String.valueOf(c));
  }

  public String getEmojiChar() {
    return this.entityData.get(EMOJI_CHAR);
  }

  public void setFace(Direction dir) {
    this.entityData.set(FACE, dir);
    this.setBoundingBox(this.makeBoundingBox());
  }

  public Direction getFace() {
    return this.entityData.get(FACE);
  }

  @Override
  public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
    super.onSyncedDataUpdated(key);
    if (FACE.equals(key)) {
      this.setBoundingBox(this.makeBoundingBox());
    }
  }

  @Override
  public void tick() {
    super.tick();
    this.setDeltaMovement(0, 0, 0);

    if (!this.level().isClientSide()) {
      BlockPos spaceBlockPos = BlockPos.containing(this.position().add(this.getLookAngle().scale(0.5)));
      BlockState spaceState = this.level().getBlockState(spaceBlockPos);

      BlockPos supportBlockPos = BlockPos.containing(this.position().add(this.getLookAngle().scale(-0.5)));
      BlockState supportState = this.level().getBlockState(supportBlockPos);

      if (supportState.isAir()
          || !(spaceState.isAir() || spaceState.is(Blocks.SNOW))
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
    if (tag.contains("Face")) {
      this.setFace(Direction.byName(tag.getString("Face")));
    }
  }

  @Override
  protected void addAdditionalSaveData(CompoundTag tag) {
    tag.putString("EmojiChar", this.getEmojiChar());
    tag.putString("Face", this.getFace().getName());
  }

  @Override
  public void remove(Entity.RemovalReason reason) {
    if (!this.level().isClientSide()) {
      Item itemToDrop = ModItems.getStickerById(VortexEmojiMod.emojiFontConfig.indexOfKey(this.getEmojiChar()));
      this.spawnAtLocation(new ItemStack(itemToDrop));

      this.level().playSound(null, this.getX(), this.getY(), this.getZ(),
          ModSounds.VORTEX_STICKER_TEAR.get(),
          SoundSource.PLAYERS, 1.0F, 1.0F);
    }
    super.remove(reason);
  }

  @Override
  public float getPickRadius() {
    return 0.0F;
  }
}
