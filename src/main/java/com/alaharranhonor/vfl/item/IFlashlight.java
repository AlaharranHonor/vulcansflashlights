package com.alaharranhonor.vfl.item;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public interface IFlashlight {

    String FLASHLIGHT_TAG = "Flashlight";
    String ACTIVE_TAG = "Active";

    default boolean isActive(ItemStack pStack) {
        CompoundTag tag = pStack.getOrCreateTagElement(FLASHLIGHT_TAG);
        return tag.contains(ACTIVE_TAG) && tag.getBoolean(ACTIVE_TAG);
    }

    default void toggle(Level pLevel, ItemStack pStack) {
        boolean isActive = this.isActive(pStack);
        CompoundTag tag = pStack.getOrCreateTagElement(FLASHLIGHT_TAG);
        tag.putBoolean(ACTIVE_TAG, !isActive);
    }

}
