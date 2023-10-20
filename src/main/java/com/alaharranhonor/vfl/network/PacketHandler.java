package com.alaharranhonor.vfl.network;

import com.alaharranhonor.vfl.ModRef;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class PacketHandler {

    private static final String VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
        ModRef.res("main"),
        () -> VERSION,
        VERSION::equals,
        VERSION::equals
    );

    public static void init() {
        INSTANCE.registerMessage(0, ServerboundToggleMinersFlashlight.class, ServerboundToggleMinersFlashlight::encode, ServerboundToggleMinersFlashlight::decode, ServerboundToggleMinersFlashlight.Handler::handle);
    }

}
