package com.alaharranhonor.vfl.client;

import com.alaharranhonor.vfl.ModRef;
import com.alaharranhonor.vfl.client.model.HelmetMinersModel;
import com.alaharranhonor.vfl.client.registry.KeybindSetup;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.event.RegisterShadersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = ModRef.ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientModEventHandler {

    @SubscribeEvent
    public static void onReload(RegisterClientReloadListenersEvent event) {
        event.registerReloadListener((ResourceManagerReloadListener) manager -> {
            if (ClientEventHandler.flashlightEffect != null) {
                ClientEventHandler.flashlightEffect.close();
                ClientEventHandler.flashlightEffect = null;
            }
        });
    }

    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(HelmetMinersModel.LAYER_LOCATION, HelmetMinersModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
        KeybindSetup.init(event::register);
    }
}
