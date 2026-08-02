package com.unt32.vortex_emoji.item;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import com.unt32.vortex_emoji.entity.EmojiEntity;

public class EraserItem extends Item {

    public EraserItem(Properties properties, char emojiChar) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        Vec3 eyePosition = player.getEyePosition();
        Vec3 lookVector = player.getLookAngle();
        double reachDistance = 4.0;
        Vec3 endPosition = eyePosition.add(lookVector.scale(reachDistance));

        BlockHitResult hitResult = level.clip(
                new ClipContext(eyePosition, endPosition, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player));

        if (hitResult.getType() != HitResult.Type.MISS) {
            endPosition = hitResult.getLocation();
        }

        AABB searchBox = player.getBoundingBox().expandTowards(lookVector.scale(reachDistance));

        EntityHitResult entityHitResult = ProjectileUtil.getEntityHitResult(level, player, eyePosition, endPosition,
                searchBox, entity -> entity instanceof EmojiEntity);

        if (entityHitResult == null) {
            return InteractionResultHolder.fail(stack);
        }

        EmojiEntity emojiEntity = (EmojiEntity) entityHitResult.getEntity();

        if (!level.isClientSide()) {

            emojiEntity.remove(Entity.RemovalReason.KILLED);

            level.playSound(null, emojiEntity.getX(), emojiEntity.getY(), emojiEntity.getZ(),
                    SoundEvents.SLIME_BLOCK_BREAK,
                    SoundSource.PLAYERS, 1.0F, 1.0F);
        }

        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }
}
