package com.alaharranhonor.vfl.client.registry;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyConflictContext;

import java.util.function.Consumer;

public class KeybindSetup {

    public static final KeyMapping TOGGLE_HELMET = new KeyMapping("key.vfl.toggle_helmet", KeyConflictContext.IN_GAME, InputConstants.Type.MOUSE, InputConstants.MOUSE_BUTTON_MIDDLE, "key.vfl.category");

    public static void init(Consumer<KeyMapping> registrar) {
        registrar.accept(TOGGLE_HELMET);
    }

}
