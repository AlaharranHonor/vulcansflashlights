package com.alaharranhonor.vfl.capability;

import com.alaharranhonor.vfl.config.ConfigData;
import com.alaharranhonor.vfl.config.ModConfigs;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;

public class Flashlight implements IFlashlight {
    private static final String FLASHLIGHT_TAG = "Flashlight";
    private static final String ACTIVE_TAG = "Active";
    private static final String CHARGE_TAG = "Charge";

    private final ItemStack stack;
    private final int maxCharge;
    private final Ingredient repairItem;

    private boolean active;
    private int charge;

    public Flashlight(ItemStack stack, boolean ultra) {
        this.stack = stack;
        this.maxCharge = ultra ? ModConfigs.ultraBatteryLife.get() : ModConfigs.batteryLife.get();
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

    public static double getFlicker(Level pLevel, Entity pEntity) {
        double blockFlicker = pLevel.getBlockStatesIfLoaded(pEntity.getBoundingBox().inflate(ModConfigs.flickerRange.get()))
            .mapToDouble(state -> {
                for (ConfigData flicker : ModConfigs.FLICKERS) {
                    ResourceLocation name = ForgeRegistries.BLOCKS.getKey(state.getBlock());
                    if (flicker.isBlock() && flicker.id().equals(name)) {
                        return flicker.multiplier();
                    }
                }
                return 0;
            }).max().orElse(0);

        double entityFlicker = pLevel.getEntities(pEntity, pEntity.getBoundingBox().inflate(ModConfigs.flickerRange.get())).stream()
            .mapToDouble(e -> {
                for (ConfigData flicker : ModConfigs.FLICKERS) {
                    ResourceLocation name = ForgeRegistries.ENTITY_TYPES.getKey(e.getType());
                    if (!flicker.isBlock() && flicker.id().equals(name)) {
                        return (int) flicker.multiplier();
                    }
                }
                return 0;
            }).max().orElse(0);

        return Math.max(entityFlicker, blockFlicker);
    }

    public static int getChargeDrain(Level pLevel, Entity pEntity) {
        int blockDrain = pLevel.getBlockStatesIfLoaded(pEntity.getBoundingBox().inflate(ModConfigs.drainRange.get()))
            .mapToInt(state -> {
                for (ConfigData drain : ModConfigs.DRAINS) {
                    ResourceLocation name = ForgeRegistries.BLOCKS.getKey(state.getBlock());
                    if (drain.isBlock() && drain.id().equals(name)) {
                        return (int) drain.multiplier();
                    }
                }

                return 1;
            }).max().orElse(1);

        int entityDrain = pLevel.getEntities(pEntity, pEntity.getBoundingBox().inflate(ModConfigs.drainRange.get())).stream()
            .mapToInt(e -> {
                for (ConfigData drain : ModConfigs.DRAINS) {
                    ResourceLocation name = ForgeRegistries.ENTITY_TYPES.getKey(e.getType());
                    if (!drain.isBlock() && drain.id().equals(name)) {
                        return (int) drain.multiplier();
                    }
                }
                return 1;
            }).max().orElse(1);

        return Math.max(entityDrain, blockDrain);
    }
}
