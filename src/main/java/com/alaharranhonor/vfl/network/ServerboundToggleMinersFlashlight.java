package com.alaharranhonor.vfl.network;

import com.alaharranhonor.vfl.capability.IFlashlight;
import com.alaharranhonor.vfl.registry.CapabilitySetup;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ServerboundToggleMinersFlashlight {

    public ServerboundToggleMinersFlashlight() {
    }

    public static void encode(ServerboundToggleMinersFlashlight pMsg, FriendlyByteBuf pBuf) {

    }

    public static ServerboundToggleMinersFlashlight decode(FriendlyByteBuf pBuf) {
        return new ServerboundToggleMinersFlashlight();
    }

    public static class Handler {
        public static void handle(ServerboundToggleMinersFlashlight pMsg, Supplier<NetworkEvent.Context> pCtx) {
            pCtx.get().enqueueWork(() -> {
                ServerPlayer player = pCtx.get().getSender();
                player.getItemBySlot(EquipmentSlot.HEAD).getCapability(CapabilitySetup.FLASHLIGHT).ifPresent(IFlashlight::toggle);
            });
            pCtx.get().setPacketHandled(true);
        }
    }
}
