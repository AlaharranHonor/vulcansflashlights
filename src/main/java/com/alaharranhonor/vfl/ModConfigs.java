package com.alaharranhonor.vfl;

import net.minecraftforge.common.ForgeConfigSpec;

import java.util.ArrayList;
import java.util.List;

public class ModConfigs {

    public static ForgeConfigSpec.BooleanValue enableFlicker;
    public static ForgeConfigSpec.BooleanValue mildFlicker;
    public static ForgeConfigSpec.ConfigValue<List<?>> flickerMultipliers;
    public static ForgeConfigSpec.ConfigValue<List<?>> drainMultipliers;

    public static ForgeConfigSpec.IntValue batteryLife;
    public static ForgeConfigSpec.IntValue ultraBatteryLife;

    public static void register(ForgeConfigSpec.Builder builder) {
        enableFlicker = builder.comment(
            "Enable flicker effect when on low battery."
        ).define("flicker", true);

        mildFlicker = builder.comment(
            "[WIP] [Accessibility Setting] Turns the flicker effect into a constant color change."
        ).define("mild_flicker", false);

        flickerMultipliers = builder.comment(
            "[WIP] When these blocks/entities are near, the flicker effect will happen.",
            "For blocks, block:<mod>:<block>:<multiplier>",
            "Example, block:minecraft:jack_o_lantern:1",
            "For entities, entity:<mod>:<entity>:<multiplier>",
            "Example, entity:minecraft:enderman:1.5"
        ).defineList("flicker_multipliers", ArrayList::new, o -> true);

        drainMultipliers = builder.comment(
            "[WIP] When these blocks/entities are near, the battery will drain faster.",
            "Leave empty to remove drain effect.",
            "For blocks, block:<mod>:<block>:<multiplier>",
            "Example, block:minecraft:jack_o_lantern:2",
            "For entities, entity:<mod>:<entity>:<multiplier>",
            "Example, entity:minecraft:enderman:5"
        ).defineList("drain_multipliers", ArrayList::new, o -> true);

        batteryLife = builder.comment(
            "Battery life (in seconds) for the base flashlight and miner's helmet"
        ).defineInRange("battery_life", 8400, 1, Integer.MAX_VALUE);

        ultraBatteryLife = builder.comment(
            "Battery life (in seconds) for the ultra version of the flashlight and miner's helmet"
        ).defineInRange("ultra_battery_life", 42_000, 1, Integer.MAX_VALUE);

    }

}
