package com.alaharranhonor.vfl.item;

import com.alaharranhonor.vfl.client.model.HelmetMinersModel;
import com.alaharranhonor.vfl.registry.ArmorMaterialSetup;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class HelmetMinersItem extends ArmorItem {

    private final boolean isUltra;
    public HelmetMinersItem(Properties pProperties, boolean isUltra) {
        super(isUltra ? ArmorMaterialSetup.MINERS_ULTRA : ArmorMaterialSetup.MINERS, Type.HELMET, pProperties);
        this.isUltra = isUltra;
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private HelmetMinersModel<LivingEntity> model;

            @Override
            public @NotNull HumanoidModel<?> getHumanoidArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<?> original) {
                if (this.model == null) {
                    this.model = new HelmetMinersModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(HelmetMinersModel.LAYER_LOCATION));
                }

                return this.model;
            }
        });
    }
}
