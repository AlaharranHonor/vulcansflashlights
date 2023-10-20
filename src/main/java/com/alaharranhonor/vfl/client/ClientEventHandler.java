package com.alaharranhonor.vfl.client;


import com.alaharranhonor.vfl.ModConfigs;
import com.alaharranhonor.vfl.ModRef;
import com.alaharranhonor.vfl.client.registry.KeybindSetup;
import com.alaharranhonor.vfl.network.PacketHandler;
import com.alaharranhonor.vfl.network.ServerboundToggleMinersFlashlight;
import com.alaharranhonor.vfl.registry.CapabilitySetup;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = ModRef.ID)
public class ClientEventHandler {

    @SubscribeEvent
    public static void triggerFlashlight(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.START) return;
        if (event.side != LogicalSide.CLIENT) return;
        Minecraft.getInstance().gameRenderer.shutdownEffect();

        Player player = event.player;
        Level level = player.level();
        ItemStack heldItem = player.getMainHandItem();
        ItemStack offItem = player.getOffhandItem();
        ItemStack helmet = player.getItemBySlot(EquipmentSlot.HEAD);

        helmet.getCapability(CapabilitySetup.FLASHLIGHT).ifPresent(flashlight -> {
            while (KeybindSetup.TOGGLE_HELMET.consumeClick()) {
                PacketHandler.INSTANCE.sendToServer(new ServerboundToggleMinersFlashlight());
                flashlight.toggle();
            }
        });

        // TODO get the current active flashlight and use that for the battery and loading effect.
        heldItem.getCapability(CapabilitySetup.FLASHLIGHT).resolve()
            .or(offItem.getCapability(CapabilitySetup.FLASHLIGHT)::resolve)
            .or(helmet.getCapability(CapabilitySetup.FLASHLIGHT)::resolve)
            .ifPresent(flashlight -> {
                if (flashlight.isActive()) {
                    float battery = flashlight.getBattery();
                    // TODO use quadratic interpolation
                    if (!ModConfigs.enableFlicker.get() || battery > 0.15 || level.random.nextFloat() < (0.85 + battery)) { // Flicker
                        Minecraft.getInstance().gameRenderer.loadEffect(ModRef.res("shaders/post/flash.json"));
                    }
                }
            });
    }
}
