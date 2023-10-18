package com.alaharranhonor.vfl.registry;

import com.alaharranhonor.vfl.ModRef;
import com.alaharranhonor.vfl.item.FlashlightItem;
import com.alaharranhonor.vfl.item.HelmetMinersItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemSetup {

    public static final DeferredRegister<Item> REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, ModRef.ID);

    public static final RegistryObject<Item> FLASHLIGHT = REGISTRY.register("flashlight", () -> new FlashlightItem(new Item.Properties().durability(640), false));
    public static final RegistryObject<Item> FLASHLIGHT_ULTRA = REGISTRY.register("flashlight_ultra", () -> new FlashlightItem(new Item.Properties().durability(1280), true));
    public static final RegistryObject<Item> HELMET_MINERS = REGISTRY.register("helmet_miners", () -> new HelmetMinersItem(new Item.Properties(), false));
    public static final RegistryObject<Item> HELMET_MINERS_ULTRA = REGISTRY.register("helmet_miners_ultra", () -> new HelmetMinersItem(new Item.Properties(), true));

    public static void init(IEventBus bus) {
        REGISTRY.register(bus);
    }
}
