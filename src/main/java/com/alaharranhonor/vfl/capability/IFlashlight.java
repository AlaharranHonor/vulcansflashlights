package com.alaharranhonor.vfl.capability;

import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.capabilities.AutoRegisterCapability;

@AutoRegisterCapability
public interface IFlashlight {

    boolean isActive();
    void toggle();
    void setActive(boolean pActive);
    int getCharge();
    void setCharge(int pCharge);
    int getMaxCharge();
    float getBattery();
    Ingredient repairItem();
    float getMaxDistance();
    float getStrength();
}
