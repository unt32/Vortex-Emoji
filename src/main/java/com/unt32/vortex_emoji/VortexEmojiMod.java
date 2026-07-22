package com.unt32.vortex_emoji;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(VortexEmojiMod.MODID)
public class VortexEmojiMod {
    public static final String MODID = "vortex_emoji";

    public VortexEmojiMod(FMLJavaModLoadingContext context) {
        IEventBus modEventBus = context.getModEventBus();

        DistExecutor.safeRunForDist(
                () -> () -> new ClientInit(modEventBus),
                () -> () -> new ServerInit(modEventBus));
    }
}
