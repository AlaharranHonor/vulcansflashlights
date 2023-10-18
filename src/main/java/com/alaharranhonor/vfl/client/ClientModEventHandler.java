package com.alaharranhonor.vfl.client;

import com.alaharranhonor.vfl.ModRef;
import com.alaharranhonor.vfl.client.model.HelmetMinersModel;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterShadersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.io.IOException;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = ModRef.ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientModEventHandler {

    public static ShaderInstance zoomShader;

    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(HelmetMinersModel.LAYER_LOCATION, HelmetMinersModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void registerShaders(RegisterShadersEvent event) {
        /*try {
            event.registerShader(new ShaderInstance(event.getResourceProvider(), ModRef.res("zoom"), DefaultVertexFormat.BLIT_SCREEN), shader -> zoomShader = shader);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }*/
    }

    public static ShaderInstance getZoomShader() {
        return zoomShader;
    }
}
