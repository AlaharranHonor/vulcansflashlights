package com.alaharranhonor.vfl.datagen.client;

import com.alaharranhonor.vfl.ModRef;
import com.alaharranhonor.vfl.registry.ItemSetup;
import net.minecraft.data.PackOutput;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ItemModelGen extends ItemModelProvider {

    public ItemModelGen(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, ModRef.ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        this.basicItem(ItemSetup.FLASHLIGHT.get());
        this.basicItem(ItemSetup.FLASHLIGHT_ULTRA.get());
        this.basicItem(ItemSetup.HELMET_MINERS.get());
        this.basicItem(ItemSetup.HELMET_MINERS_ULTRA.get());

        /*this.withExistingParent(ItemSetup.HELMET_MINERS.getId().getPath(), this.modLoc("item/miner_helmet"))
            .texture("helmet", ItemSetup.HELMET_MINERS.getId().withPrefix("item/"));

        this.withExistingParent(ItemSetup.HELMET_MINERS_ULTRA.getId().getPath(), this.modLoc("item/miner_helmet"))
            .texture("helmet", ItemSetup.HELMET_MINERS_ULTRA.getId().withPrefix("item/"));*/
    }
}
