package com.alaharranhonor.vfl.item;

import com.alaharranhonor.vfl.ModRef;
import com.alaharranhonor.vfl.capability.Flashlight;
import com.alaharranhonor.vfl.capability.IFlashlight;
import com.alaharranhonor.vfl.registry.CapabilitySetup;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class FlashlightItem extends Item {

    private final boolean isUltra;

    public FlashlightItem(Properties pProperties, boolean isUltra) {
        super(pProperties);
        this.isUltra = isUltra;
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pStack.getCapability(CapabilitySetup.FLASHLIGHT).ifPresent(flashlight -> {
            pTooltipComponents.add(Component.literal(String.format("Battery: %d%%", (int) (flashlight.getBattery() * 100))).withStyle(ChatFormatting.GRAY));
        });
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack held = pPlayer.getItemInHand(pUsedHand);
        held.getCapability(CapabilitySetup.FLASHLIGHT).ifPresent(flashlight -> {
            if (flashlight.getBattery() > 0) {
                flashlight.toggle();
            }
        });
        return InteractionResultHolder.sidedSuccess(held, pLevel.isClientSide);
    }

    @Override
    public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
        if (!pIsSelected && pEntity instanceof Player player) {
            pIsSelected = player.getOffhandItem() == pStack;
        }
        boolean selected = pIsSelected;
        pStack.getCapability(CapabilitySetup.FLASHLIGHT).ifPresent(flashlight -> {
            if (flashlight.isActive() && (!selected)) {
                flashlight.setActive(false);
            }

            if (selected) {
                if (flashlight.isActive()) {
                    int charge = flashlight.getCharge();
                    int drain = Flashlight.getChargeDrain(pLevel, pEntity);
                    charge -= drain;
                    flashlight.setCharge(charge);
                    if (charge <= 0) {
                        flashlight.setActive(false);
                    }
                }
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
            final LazyOptional<IFlashlight> flashlight = LazyOptional.of(() -> new Flashlight(stack, FlashlightItem.this.isUltra));
            @Override
            public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
                return CapabilitySetup.FLASHLIGHT.orEmpty(cap, this.flashlight);
            }
        };
    }
}
