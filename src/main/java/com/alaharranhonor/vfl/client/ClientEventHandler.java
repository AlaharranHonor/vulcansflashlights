package com.alaharranhonor.vfl.client;


import com.alaharranhonor.vfl.capability.Flashlight;
import com.alaharranhonor.vfl.capability.IFlashlight;
import com.alaharranhonor.vfl.config.ModConfigs;
import com.alaharranhonor.vfl.ModRef;
import com.alaharranhonor.vfl.client.registry.KeybindSetup;
import com.alaharranhonor.vfl.mixins.PostChainAccessor;
import com.alaharranhonor.vfl.network.PacketHandler;
import com.alaharranhonor.vfl.network.ServerboundToggleMinersFlashlight;
import com.alaharranhonor.vfl.registry.CapabilitySetup;
import com.alaharranhonor.vfl.registry.FlashlightRenderTypes;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.client.renderer.PostPass;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Tuple;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.*;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

import java.io.IOException;
import java.util.List;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = ModRef.ID)
public class ClientEventHandler {

    private static final float HALF_SQRT_3 = (float) (Math.sqrt(3.0D) / 2.0D);

    @SubscribeEvent
    public static void triggerFlashlight(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.START) return;
        if (event.side != LogicalSide.CLIENT) return;
        if (event.player != Minecraft.getInstance().player) return;
        Minecraft.getInstance().gameRenderer.shutdownEffect();

        Player player = event.player;
        Level level = player.level();
        ItemStack helmet = player.getItemBySlot(EquipmentSlot.HEAD);

        helmet.getCapability(CapabilitySetup.FLASHLIGHT).ifPresent(flashlight -> {
            while (KeybindSetup.TOGGLE_HELMET.consumeClick()) {
                PacketHandler.INSTANCE.sendToServer(new ServerboundToggleMinersFlashlight());
                flashlight.toggle();
            }
        });

        if (!isFirstPerson()) {
            return;
        }

        Tuple<ItemStack, Integer> active = getActiveFlashlightStack(player);
        ItemStack flashlightStack = active.getA();
        int type = active.getB();

        if (flashlightStack.isEmpty()) {
            return;
        }

        // TODO get the current active flashlight and use that for the battery and loading effect.
        flashlightStack.getCapability(CapabilitySetup.FLASHLIGHT).ifPresent(flashlight -> {
            if (flashlight.isActive()) {
                float battery = flashlight.getBattery();
                double flicker = Flashlight.getFlicker(level, player);
                // TODO use quadratic interpolation
                if (!ModConfigs.enableFlicker.get()) {
                    flashShader();
                    return;
                }

                if (flicker == 0 && battery > 0.15) {
                    flashShader();
                    return;
                }

                double flashChance = 0.85f;
                if (battery <= 0.15) {
                    flashChance += battery;
                }

                if (flicker > 0) {
                    flashChance /= (1 + (flicker - 1) * 0.1);
                }

                if (level.random.nextFloat() < flashChance) {
                    flashShader();
                    return;
                }
            }
        });
    }

    public static PostChain flashlightEffect;

    @SubscribeEvent
    public static void renderShader(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_LEVEL) {
            return;
        }

        if (flashlightEffect == null) {
            try {
                PostChain postChain = new PostChain(Minecraft.getInstance().textureManager, Minecraft.getInstance().getResourceManager(), Minecraft.getInstance().getMainRenderTarget(), ModRef.res("shaders/post/flash.json"));
                postChain.resize(Minecraft.getInstance().getWindow().getWidth(), Minecraft.getInstance().getWindow().getHeight());
                flashlightEffect = postChain;
            } catch (IOException e) {
                ModRef.LOGGER.error("Could not load flashlight shader.", e);
            }
        }

        Tuple<ItemStack, Integer> active = getActiveFlashlightStack(Minecraft.getInstance().player);
        ItemStack flashlightStack = active.getA();
        int type = active.getB();

        if (flashlightStack.isEmpty()) {
            return;
        }

        IFlashlight flashlight = flashlightStack.getCapability(CapabilitySetup.FLASHLIGHT).orElse(null);
        float maxDistance = flashlight.getMaxDistance();
        float strength = flashlight.getStrength();

        if (isFirstPerson() && flashlightEffect != null) {
            List<PostPass> passes = ((PostChainAccessor) flashlightEffect).getPasses();
            for (PostPass pass : passes) {
                pass.getEffect().safeGetUniform("MaxDistance").set(maxDistance);
                pass.getEffect().safeGetUniform("Strength").set(strength);
                pass.getEffect().safeGetUniform("Offset").set(type == 2 ? 0.05f : 0f);
            }
            flashlightEffect.process(event.getPartialTick());
        }
    }

    @SubscribeEvent
    public static void renderLight(RenderPlayerEvent.Post event) {
        PoseStack pose = event.getPoseStack();
        Player entity = event.getEntity();
        Tuple<ItemStack, Integer> active = getActiveFlashlightStack(entity);
        ItemStack flashlight = active.getA();
        int type = active.getB();

        if (flashlight.isEmpty()) {
            return;
        }

        float handPos = 0;

        if (type == 0) handPos = -0.1f;
        if (type == 1) handPos = 0.1f;

        pose.pushPose();
        pose.translate(0.0D, 1D, 0.0D);
        pose.scale(-1.0F, -1.0F, 1.0F);
        pose.pushPose();
        float xRot = entity.getXRot();
        float yRot = entity.getYRot();
        float length = 10F;
        float width = 1.5F;
        pose.pushPose();
        pose.translate(0, type == 2 ? -1 + (entity.isShiftKeyDown() ? 0.25 : 0) : 0, 0);
        pose.mulPose(Axis.YP.rotationDegrees(yRot + 180));
        pose.mulPose(Axis.XN.rotationDegrees(90 - xRot));
        pose.scale(3, 1, 1);
        pose.translate(handPos, 0, 0);
        PoseStack.Pose posestack$pose = pose.last();
        Matrix4f matrix4f1 = posestack$pose.pose();
        Matrix3f matrix3f1 = posestack$pose.normal();
        VertexConsumer lightConsumer = Minecraft.getInstance().renderBuffers().bufferSource().getBuffer(FlashlightRenderTypes.getFlashlight());
        shineOriginVertex(lightConsumer, matrix4f1, matrix3f1, 0, 0);
        shineLeftCornerVertex(lightConsumer, matrix4f1, matrix3f1, length, width, 0, 0);
        shineRightCornerVertex(lightConsumer, matrix4f1, matrix3f1, length, width, 0, 0);
        shineLeftCornerVertex(lightConsumer, matrix4f1, matrix3f1, length, width, 0, 0);
        pose.popPose();
        pose.popPose();
        pose.popPose();
    }

    private static void shineOriginVertex(VertexConsumer builder, Matrix4f matrix, Matrix3f normal, float u, float v) {
        builder.vertex(matrix, 0.0F, 0.0F, 0.0F).color(255, 255, 255, 50).uv(u + 0.5F, v).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(240).normal(normal, 0.0F, 1.0F, 0.0F).endVertex();
    }

    private static void shineLeftCornerVertex(VertexConsumer builder, Matrix4f matrix, Matrix3f normal, float x, float y, float u, float v) {
        builder.vertex(matrix, -HALF_SQRT_3 * y, x, 0).color(200, 235, 255, 0).uv(u, v + 1).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(240).normal(normal, 0.0F, -1.0F, 0.0F).endVertex();
    }

    private static void shineRightCornerVertex(VertexConsumer builder, Matrix4f matrix, Matrix3f normal, float x, float y, float u, float v) {
        builder.vertex(matrix, HALF_SQRT_3 * y, x, 0).color(200, 235, 255, 0).uv(u + 1, v + 1).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(240).normal(normal, 0.0F, -1.0F, 0.0F).endVertex();
    }

    public static boolean isFirstPerson() {
        return Minecraft.getInstance().options.getCameraType().isFirstPerson();
    }

    private static Tuple<ItemStack, Integer> getActiveFlashlightStack(LivingEntity entity) {
        ItemStack mainItem = entity.getMainHandItem();
        ItemStack offItem = entity.getOffhandItem();
        ItemStack helmetItem = entity.getItemBySlot(EquipmentSlot.HEAD);
        if (mainItem.getCapability(CapabilitySetup.FLASHLIGHT).map(IFlashlight::isActive).orElse(false)) {
            return new Tuple<>(mainItem, entity.getMainArm() == HumanoidArm.RIGHT ? 0 : 1);
        }

        if (offItem.getCapability(CapabilitySetup.FLASHLIGHT).map(IFlashlight::isActive).orElse(false)) {
            return new Tuple<>(offItem, entity.getMainArm() == HumanoidArm.RIGHT ? 1 : 0);
        }

        if (helmetItem.getCapability(CapabilitySetup.FLASHLIGHT).map(IFlashlight::isActive).orElse(false)) {
            return new Tuple<>(helmetItem, 2);
        }

        return new Tuple<>(ItemStack.EMPTY, -1);
    }

    private static void flashShader() {
        GameRenderer gameRenderer = Minecraft.getInstance().gameRenderer;
        //gameRenderer.loadEffect(ModRef.res("shaders/post/flash.json"));
    }
}
