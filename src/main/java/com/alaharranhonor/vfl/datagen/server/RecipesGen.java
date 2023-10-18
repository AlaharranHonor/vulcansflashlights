package com.alaharranhonor.vfl.datagen.server;

import com.alaharranhonor.vfl.registry.ItemSetup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.Tags;

import java.util.function.Consumer;

public class RecipesGen extends RecipeProvider {

    public RecipesGen(PackOutput pOutput) {
        super(pOutput);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> pWriter) {
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ItemSetup.FLASHLIGHT.get())
            .pattern("G")
            .pattern("S")
            .pattern("R")
            .define('G', Tags.Items.DUSTS_GLOWSTONE)
            .define('S', Tags.Items.RODS_WOODEN)
            .define('R', Tags.Items.DUSTS_REDSTONE)
            .group("vfl")
            .unlockedBy("has_glowstone_dust", has(Tags.Items.DUSTS_GLOWSTONE))
            .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ItemSetup.FLASHLIGHT_ULTRA.get())
            .pattern("G")
            .pattern("S")
            .pattern("R")
            .define('G', Items.GLOWSTONE)
            .define('S', Tags.Items.RODS_WOODEN)
            .define('R', Tags.Items.STORAGE_BLOCKS_REDSTONE)
            .group("vfl")
            .unlockedBy("has_glowstone", has(Items.GLOWSTONE))
            .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ItemSetup.HELMET_MINERS.get())
            .pattern(" F ")
            .pattern("SHS")
            .pattern(" S ")
            .define('F', ItemSetup.FLASHLIGHT.get())
            .define('S', Tags.Items.STRING)
            .define('H', Items.IRON_HELMET)
            .group("vfl")
            .unlockedBy("has_flashlight", has(ItemSetup.FLASHLIGHT.get()))
            .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ItemSetup.HELMET_MINERS_ULTRA.get())
            .pattern(" F ")
            .pattern("SHS")
            .pattern(" S ")
            .define('F', ItemSetup.FLASHLIGHT_ULTRA.get())
            .define('S', Tags.Items.STRING)
            .define('H', Items.IRON_HELMET)
            .group("vfl")
            .unlockedBy("has_flashlight_ultra", has(ItemSetup.FLASHLIGHT_ULTRA.get()))
            .save(pWriter);
    }
}
