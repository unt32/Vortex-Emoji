package com.unt32.vortex_emoji;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.minecraft.client.Minecraft;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class ClientInit {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClientInit.class);

    public ClientInit(IEventBus modEventBus) {
        modEventBus.addListener(this::onClientSetup);
    }

    private void onClientSetup(FMLClientSetupEvent event) {
        LOGGER.info("Client setup called");
        
        event.enqueueWork(() -> Minecraft.getInstance().options.chatLineSpacing().set(0.7D));
    }
}