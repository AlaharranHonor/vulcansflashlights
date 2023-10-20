package com.alaharranhonor.vfl.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.util.INBTSerializable;

public class Flashlight implements IFlashlight {
    private static final String FLASHLIGHT_TAG = "Flashlight";
    private static final String ACTIVE_TAG = "Active";
    private static final String CHARGE_TAG = "Charge";

    public static final int MAX_BATTERY = 8_400;
    public static final int MAX_BATTERY_ULTRA = 42_000;

    private final ItemStack stack;
    private final int maxCharge;
    private final Ingredient repairItem;

    private boolean active;
    private int charge;

    public Flashlight(ItemStack stack, boolean ultra) {
        this.stack = stack;
        this.maxCharge = ultra ? MAX_BATTERY_ULTRA : MAX_BATTERY;
        this.repairItem = Ingredient.of(ultra ? Items.REDSTONE_BLOCK : Items.REDSTONE);

        this.charge = this.maxCharge;
        this.load(this.stack.getOrCreateTagElement(FLASHLIGHT_TAG));
    }

    @Override
    public boolean isActive() {
        return this.active;
    }

    @Override
    public void toggle() {
        this.setActive(!this.active);
    }

    @Override
    public void setActive(boolean pActive) {
        this.active = pActive;
        this.save();
    }

    @Override
    public int getCharge() {
        return this.charge;
    }

    @Override
    public void setCharge(int pCharge) {
        this.charge = Mth.clamp(pCharge, 0, this.maxCharge);
        this.save();
    }

    @Override
    public int getMaxCharge() {
        return this.maxCharge;
    }

    @Override
    public float getBattery() {
        return this.charge / (float) this.maxCharge;
    }

    @Override
    public Ingredient repairItem() {
        return this.repairItem;
    }

    public CompoundTag save() {
        CompoundTag tag = this.stack.getOrCreateTagElement(FLASHLIGHT_TAG);
        tag.putBoolean(ACTIVE_TAG, this.active);
        tag.putInt(CHARGE_TAG, this.charge);
        return tag;
    }

    public void load(CompoundTag tag) {
        this.active = tag.getBoolean(ACTIVE_TAG);
        this.charge = tag.getInt(CHARGE_TAG);
    }
}
