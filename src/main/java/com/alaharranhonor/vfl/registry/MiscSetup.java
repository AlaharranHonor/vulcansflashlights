package com.alaharranhonor.vfl.registry;

import com.alaharranhonor.vfl.ModRef;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class MiscSetup {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ModRef.ID);

    public static final RegistryObject<CreativeModeTab> MAIN_TAB = CREATIVE_TABS.register("main", () -> CreativeModeTab.builder()
        .title(Component.translatable("itemGroup.vfl.main"))
        .icon(() -> new ItemStack(ItemSetup.FLASHLIGHT_ULTRA.get()))
        .displayItems(((pParameters, pOutput) -> {
            ItemSetup.REGISTRY.getEntries().forEach(registeredItem -> {
                pOutput.accept(registeredItem.get());
            });
        }))
        .build());

    public static void init(IEventBus bus) {
        CREATIVE_TABS.register(bus);
    }

}
