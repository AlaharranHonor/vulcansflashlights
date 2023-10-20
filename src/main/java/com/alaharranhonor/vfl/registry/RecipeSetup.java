package com.alaharranhonor.vfl.registry;

import com.alaharranhonor.vfl.ModRef;
import com.alaharranhonor.vfl.recipe.ChargeRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RecipeSetup {

    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, ModRef.ID);
    public static final DeferredRegister<RecipeType<?>> TYPES = DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, ModRef.ID);

    public static final RegistryObject<RecipeType<ChargeRecipe>> CHARGE_RECIPE_TYPE = TYPES.register("charge", () -> new RecipeType<>() {});
    public static final RegistryObject<SimpleCraftingRecipeSerializer<ChargeRecipe>> CHARGE_RECIPE_SERIALIZER = SERIALIZERS.register("charge", () -> new SimpleCraftingRecipeSerializer<>(ChargeRecipe::new));

    public static void init(IEventBus bus) {
        TYPES.register(bus);
        SERIALIZERS.register(bus);
    }

}
