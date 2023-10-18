package com.alaharranhonor.vfl.datagen.client;

import com.alaharranhonor.vfl.ModRef;
import com.alaharranhonor.vfl.registry.ItemSetup;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.LanguageProvider;

public class EnUsLangGen extends LanguageProvider {

    public EnUsLangGen(PackOutput output) {
        super(output, ModRef.ID, "en_us");
    }

    @Override
    protected void addTranslations() {
        this.add(ItemSetup.FLASHLIGHT.get(), "Flashlight");
        this.add(ItemSetup.FLASHLIGHT_ULTRA.get(), "Ultra Flashlight");
        this.add(ItemSetup.HELMET_MINERS.get(), "Miner's Helmet");
        this.add(ItemSetup.HELMET_MINERS_ULTRA.get(), "Ultra Miner's Helmet");

        this.add("itemGroup.vfl.main", "Vulkan's Flashlights");
    }
}
