package com.alaharranhonor.vfl;

import com.alaharranhonor.vfl.config.ModConfigs;
import com.alaharranhonor.vfl.network.PacketHandler;
import com.alaharranhonor.vfl.registry.ItemSetup;
import com.alaharranhonor.vfl.registry.MiscSetup;
import com.alaharranhonor.vfl.registry.RecipeSetup;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(ModRef.ID)
public class Flashlights {

    public Flashlights() {
        ForgeConfigSpec.Builder configBuilder = new ForgeConfigSpec.Builder();
        ModConfigs.register(configBuilder);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, configBuilder.build());

        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        ItemSetup.init(modBus);
        RecipeSetup.init(modBus);
        MiscSetup.init(modBus);

        PacketHandler.init();
    }
}
