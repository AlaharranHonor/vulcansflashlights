package com.alaharranhonor.vfl.registry;

import com.alaharranhonor.vfl.capability.IFlashlight;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;

public class CapabilitySetup {

    public static final Capability<IFlashlight> FLASHLIGHT = CapabilityManager.get(new CapabilityToken<>() {});
}
