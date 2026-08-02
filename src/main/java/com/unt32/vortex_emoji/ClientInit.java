package com.unt32.vortex_emoji;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unt32.vortex_emoji.client.renderer.EmojiEntityRenderer;
import com.unt32.vortex_emoji.entity.ModEntities;

import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@OnlyIn(Dist.CLIENT)
public class ClientInit {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClientInit.class);

    public ClientInit(IEventBus modEventBus) {
        modEventBus.addListener(this::onClientSetup);

        modEventBus.addListener(this::onRegisterRenderers);
    }

    private void onClientSetup(FMLClientSetupEvent event) {
        LOGGER.info("Client setup called");

        event.enqueueWork(() -> Minecraft.getInstance().options.chatLineSpacing().set(0.7D));
    }

    private void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.EMOJI_ENTITY.get(), EmojiEntityRenderer::new);
    }
}