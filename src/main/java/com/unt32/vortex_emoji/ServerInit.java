package com.unt32.vortex_emoji;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

public class ServerInit {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServerInit.class);

    public ServerInit(IEventBus modEventBus) {
        modEventBus.addListener(this::onCommonSetup);
    }

    private void onCommonSetup(FMLCommonSetupEvent event) {
        LOGGER.info("onCommonSetup called");
    }
}