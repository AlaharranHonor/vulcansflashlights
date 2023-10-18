package com.alaharranhonor.vfl;

import com.alaharranhonor.vfl.registry.ItemSetup;
import com.alaharranhonor.vfl.registry.MiscSetup;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(ModRef.ID)
public class Flashlights {

    public Flashlights() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        ItemSetup.init(modBus);
        MiscSetup.init(modBus);
    }
}
