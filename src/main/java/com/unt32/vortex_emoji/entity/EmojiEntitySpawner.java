package com.unt32.vortex_emoji.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

public class EmojiEntitySpawner {

    public static void spawnViaNbt(ServerLevel level, double x, double y, double z, float yaw, float pitch,
            char emojiChar) {
        CompoundTag nbt = new CompoundTag();
        nbt.putString("id", "minecraft:text_display");

        nbt.putString("text", "{\"text\":\"" + emojiChar + "\"}");
        nbt.putInt("background", 0);

        Entity entity = EntityType.loadEntityRecursive(nbt, level, e -> {
            e.moveTo(x, y, z, yaw, pitch);
            return e;
        });

        if (entity != null) {
            level.addFreshEntity(entity);
        }
    }
}