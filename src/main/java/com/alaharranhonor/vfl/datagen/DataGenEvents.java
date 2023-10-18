package com.alaharranhonor.vfl.datagen;

import com.alaharranhonor.vfl.ModRef;
import com.alaharranhonor.vfl.datagen.client.EnUsLangGen;
import com.alaharranhonor.vfl.datagen.client.ItemModelGen;
import com.alaharranhonor.vfl.datagen.server.RecipesGen;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ModRef.ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenEvents {

    @SubscribeEvent
    public static void gatherDataGens(GatherDataEvent event) {
        DataGenerator data = event.getGenerator();
        PackOutput output = data.getPackOutput();
        ExistingFileHelper fileHelper = event.getExistingFileHelper();

        boolean includeClient = event.includeClient();
        boolean includeServer = event.includeServer();

        data.addProvider(includeClient, new ItemModelGen(output, fileHelper));
        data.addProvider(includeClient, new EnUsLangGen(output));

        data.addProvider(includeServer, new RecipesGen(output));

    }

}
