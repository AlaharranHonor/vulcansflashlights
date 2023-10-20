package com.alaharranhonor.vfl.item;

import com.alaharranhonor.vfl.capability.Flashlight;
import com.alaharranhonor.vfl.capability.IFlashlight;
import com.alaharranhonor.vfl.client.model.HelmetMinersModel;
import com.alaharranhonor.vfl.client.registry.KeybindSetup;
import com.alaharranhonor.vfl.registry.ArmorMaterialSetup;
import com.alaharranhonor.vfl.registry.CapabilitySetup;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

public class HelmetMinersItem extends ArmorItem {

    private final boolean isUltra;

    public HelmetMinersItem(Properties pProperties, boolean isUltra) {
        super(isUltra ? ArmorMaterialSetup.MINERS_ULTRA : ArmorMaterialSetup.MINERS, Type.HELMET, pProperties);
        this.isUltra = isUltra;
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(KeybindSetup.TOGGLE_HELMET.getKey().getDisplayName().copy().withStyle(ChatFormatting.AQUA).append(Component.literal( " to Toggle").withStyle(ChatFormatting.GRAY)));
        pStack.getCapability(CapabilitySetup.FLASHLIGHT).ifPresent(flashlight -> {
            pTooltipComponents.add(Component.literal(String.format("Battery: %d%%", (int) (flashlight.getBattery() * 100))).withStyle(ChatFormatting.GRAY));
        });
    }

    @Override
    public void onArmorTick(ItemStack pStack, Level pLevel, Player pPlayer) {
        pStack.getCapability(CapabilitySetup.FLASHLIGHT).ifPresent(flashlight -> {
            if (flashlight.isActive()) {
                int charge = flashlight.getCharge();
                charge--;
                flashlight.setCharge(charge);
                if (charge <= 0) {
                    flashlight.setActive(false);
                }
            }
        });
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

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return false;
    }

    @Override
    public boolean isBarVisible(ItemStack pStack) {
        return true;
    }

    public int getBarWidth(ItemStack pStack) {
        return pStack.getCapability(CapabilitySetup.FLASHLIGHT)
            .map(flashlight -> Math.round((float) flashlight.getBattery() * 13.0F))
            .orElse(0);
    }

    public int getBarColor(ItemStack pStack) {
        float color = pStack.getCapability(CapabilitySetup.FLASHLIGHT)
            .map(IFlashlight::getBattery)
            .orElse(0F);
        return Mth.hsvToRgb(color / 3.0F, 1.0F, 1.0F);
    }


    @Override
    public @Nullable ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        return new ICapabilityProvider() {
            final LazyOptional<IFlashlight> flashlight = LazyOptional.of(() -> new Flashlight(stack, HelmetMinersItem.this.isUltra));
            @Override
            public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
                return CapabilitySetup.FLASHLIGHT.orEmpty(cap, this.flashlight);
            }
        };
    }
}
