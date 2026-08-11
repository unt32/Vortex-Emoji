package com.unt32.vortex_emoji.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.unt32.vortex_emoji.VortexEmojiMod;
import com.unt32.vortex_emoji.entity.EmojiEntity;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class EmojiEntityRenderer extends EntityRenderer<EmojiEntity> {
  private static final ResourceLocation BLANK_TEXTURE = new ResourceLocation(VortexEmojiMod.MODID,
      "textures/entity/blank.png");
  private final Font font;

  public EmojiEntityRenderer(EntityRendererProvider.Context context) {
    super(context);
    this.font = context.getFont();
  }

  @Override
  public void render(EmojiEntity entity, float entityYaw, float partialTicks, PoseStack poseStack,
      MultiBufferSource buffer, int packedLight) {

    String text = entity.getEmojiChar();
    if (text.isEmpty())
      return;

    poseStack.pushPose();

    float interpolatedYaw = Mth.lerp(partialTicks, entity.yRotO, entity.getYRot());
    float interpolatedPitch = Mth.lerp(partialTicks, entity.xRotO, entity.getXRot());

    poseStack.mulPose(Axis.YP.rotationDegrees(180.0F - interpolatedYaw));
    poseStack.mulPose(Axis.XP.rotationDegrees(-interpolatedPitch));

    poseStack.scale(-0.025F, -0.025F, 0.025F);
    poseStack.translate(0.0D, -2.5D, -0.05D);

    float halfWidth = (float) (-this.font.width(text) / 2);

    this.font.drawInBatch(Component.literal(text), halfWidth, 0.0F, 0xFFFFFFFF, false, poseStack.last().pose(),
        buffer, Font.DisplayMode.NORMAL, 0x00000000, 0xF000F0);

    poseStack.popPose();
    super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
  }

  @Override
  public ResourceLocation getTextureLocation(EmojiEntity entity) {
    return BLANK_TEXTURE;
  }
}
