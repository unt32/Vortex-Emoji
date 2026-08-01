package com.unt32.vortex_emoji.item;

import com.unt32.vortex_emoji.entity.EmojiEntitySpawner;

import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class StickerItem extends Item {
    private char EMOJI_CHAR;

    public StickerItem(Properties properties, char emojiChar) {
        super(properties);
        EMOJI_CHAR = emojiChar;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        Vec3 eyePosition = player.getEyePosition();
        Vec3 endPosition = eyePosition.add(player.getLookAngle().scale(4.0f));

        BlockHitResult hitResult = level.clip(
                new ClipContext(eyePosition, endPosition, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player));

        if (hitResult.getType() == HitResult.Type.MISS) {
            return InteractionResultHolder.fail(stack);
        }

        if (!level.isClientSide()) {

            Vec3 hitLocation = hitResult.getLocation();
            Direction direction = hitResult.getDirection();

            float yaw, pitch;

            switch (direction) {
                case DOWN:
                    yaw = player.getYRot() - 180;
                    pitch = 90f;
                    break;
                case UP:
                    yaw = player.getYRot() - 180;
                    pitch = -90f;
                    break;
                case NORTH:
                    yaw = 180f;
                    pitch = 0f;
                    break;
                case SOUTH:
                    yaw = 0f;
                    pitch = 0f;
                    break;
                case WEST:
                    yaw = 90f;
                    pitch = 0f;
                    break;
                case EAST:
                    yaw = -90f;
                    pitch = 0f;
                    break;
                default:
                    yaw = player.getYRot() - 180;
                    pitch = -player.getXRot();
                    break;
            }

            EmojiEntitySpawner.spawnViaNbt((ServerLevel) level, hitLocation.x, hitLocation.y,
                    hitLocation.z, yaw, pitch, EMOJI_CHAR);

            level.playSound(null, hitLocation.x, hitLocation.y, hitLocation.z, SoundEvents.SLIME_BLOCK_PLACE,
                    SoundSource.PLAYERS, 1.0F, 1.0F);

            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }
        }

        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }
}
