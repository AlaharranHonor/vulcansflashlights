package com.alaharranhonor.vfl.recipe;

import com.alaharranhonor.vfl.capability.IFlashlight;
import com.alaharranhonor.vfl.registry.CapabilitySetup;
import com.alaharranhonor.vfl.registry.RecipeSetup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

public class ChargeRecipe extends CustomRecipe {

    public ChargeRecipe(ResourceLocation pId, CraftingBookCategory pCategory) {
        super(pId, pCategory);
    }

    @Override
    public boolean matches(CraftingContainer pContainer, Level pLevel) {
        ItemStack flashlightStack = ItemStack.EMPTY;
        ItemStack repairStack = ItemStack.EMPTY;
        for (int i = 0; i < pContainer.getContainerSize(); ++i) {
            ItemStack slotStack = pContainer.getItem(i);
            IFlashlight flashlight = slotStack.getCapability(CapabilitySetup.FLASHLIGHT).orElse(null);
            if (flashlight != null) {
                if (flashlight.getBattery() == 1) return false;
                if (!flashlightStack.isEmpty()) return false;
                flashlightStack = slotStack;
                continue;
            }

            if (!repairStack.isEmpty()) return false;
            repairStack = slotStack;
        }

        ItemStack finalRepairStack = repairStack;
        return flashlightStack.getCapability(CapabilitySetup.FLASHLIGHT).map(flashlight -> {
            return flashlight.repairItem().test(finalRepairStack);
        }).orElse(false);
    }

    @Override
    public ItemStack assemble(CraftingContainer pContainer, RegistryAccess pRegistryAccess) {
        ItemStack flashlightStack = ItemStack.EMPTY;
        ItemStack redstoneStack = ItemStack.EMPTY;
        for (int i = 0; i < pContainer.getContainerSize(); ++i) {
            ItemStack slotStack = pContainer.getItem(i);
            if (slotStack.is(Items.REDSTONE) || slotStack.is(Items.REDSTONE_BLOCK)) {
                if (!redstoneStack.isEmpty()) return ItemStack.EMPTY;
                redstoneStack = slotStack;
                continue;
            }

            IFlashlight flashlight = slotStack.getCapability(CapabilitySetup.FLASHLIGHT).orElse(null);
            if (flashlight != null) {
                if (!flashlightStack.isEmpty()) return ItemStack.EMPTY;
                flashlightStack = slotStack;
            }
        }

        if (flashlightStack.isEmpty() || redstoneStack.isEmpty()) {
            return ItemStack.EMPTY;
        }

        ItemStack chargedFlashlight = flashlightStack.copy();
        chargedFlashlight.getCapability(CapabilitySetup.FLASHLIGHT).ifPresent(flashlight -> {
            flashlight.setCharge(flashlight.getMaxCharge());
        });
        return chargedFlashlight;
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return pWidth * pHeight >= 2;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeSetup.CHARGE_RECIPE_SERIALIZER.get();
    }
}
