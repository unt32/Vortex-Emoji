package com.unt32.vortex_emoji;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(VortexEmojiMod.MODID)
public class VortexEmojiMod {
    public static final String MODID = "vortex_emoji";


    public VortexEmojiMod(FMLJavaModLoadingContext context) {
        context.getModEventBus().addListener(this::onClientSetup);
    }

    private void onClientSetup(FMLClientSetupEvent event) {
       event.enqueueWork(() -> Minecraft.getInstance().options.chatLineSpacing().set(0.7D));
    }
}
