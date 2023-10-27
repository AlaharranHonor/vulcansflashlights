package com.alaharranhonor.vfl.config;

import com.alaharranhonor.vfl.ModRef;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = ModRef.ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModConfigs {

    public static ForgeConfigSpec.BooleanValue enableFlicker;
    public static ForgeConfigSpec.BooleanValue mildFlicker;
    public static ForgeConfigSpec.IntValue flickerRange;
    public static ForgeConfigSpec.IntValue drainRange;
    private static ForgeConfigSpec.ConfigValue<List<?>> flickerMultipliers;
    private static ForgeConfigSpec.ConfigValue<List<?>> drainMultipliers;

    public static ForgeConfigSpec.IntValue batteryLife;
    public static ForgeConfigSpec.IntValue ultraBatteryLife;

    public static final List<ConfigData> FLICKERS = new ArrayList<>();
    public static final List<ConfigData> DRAINS = new ArrayList<>();

    public static void register(ForgeConfigSpec.Builder builder) {
        enableFlicker = builder.comment(
            "Enable flicker effect when on low battery."
        ).define("flicker", true);

        mildFlicker = builder.comment(
            "[WIP] [Accessibility Setting] Turns the flicker effect into a constant color change."
        ).define("mild_flicker", false);

        flickerRange = builder.comment(
            "Range to search for blocks/entities that affect the flashlight flicker."
        ).defineInRange("flicker_range", 10, 1, 100);

        flickerMultipliers = builder.comment(
            "When these blocks/entities are near, the flicker effect will happen.",
            "For blocks, block:<mod>:<block>:<multiplier>",
            "Example, block:minecraft:jack_o_lantern:1",
            "For entities, entity:<mod>:<entity>:<multiplier>",
            "Example, entity:minecraft:enderman:1.5"
        ).defineList("flicker_multipliers", ArrayList::new, o -> true);

        drainRange = builder.comment(
            "Range to search for blocks/entities that affect the flashlight drain."
        ).defineInRange("drain_range", 10, 1, 100);

        drainMultipliers = builder.comment(
            "When these blocks/entities are near, the battery will drain faster.",
            "Leave empty to remove drain effect.",
            "For blocks, block:<mod>:<block>:<drain>",
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

    public static void onReload() {
        drainMultipliers.get().forEach(s -> {
            String config = (String) s;
            int first = config.indexOf(':');
            int last = config.lastIndexOf(':');
            String type = config.substring(0, first);
            String id = config.substring(first + 1, last);
            String multiplier = config.substring(last + 1);
            if (!"block".equals(type) && !"entity".equals(type)) {
                ModRef.LOGGER.warn("Invalid config type for drain multiplier. Must be 'block' or 'entity");
                return;
            }

            DRAINS.add(new ConfigData(new ResourceLocation(id), type.equals("block"), Float.parseFloat(multiplier)));
        });

        flickerMultipliers.get().forEach(s -> {
            String config = (String) s;
            int first = config.indexOf(':');
            int last = config.lastIndexOf(':');
            String type = config.substring(0, first);
            String id = config.substring(first + 1, last);
            String multiplier = config.substring(last + 1);
            if (!"block".equals(type) && !"entity".equals(type)) {
                ModRef.LOGGER.warn("Invalid config type for flicker multiplier. Must be 'block' or 'entity");
                return;
            }

            FLICKERS.add(new ConfigData(new ResourceLocation(id), type.equals("block"), Float.parseFloat(multiplier)));
        });
    }

    @SubscribeEvent
    public static void reloadConfigs(ModConfigEvent.Reloading event) {
        onReload();
    }

    @SubscribeEvent
    public static void loadConfigs(ModConfigEvent.Loading event) {
        onReload();
    }

}
