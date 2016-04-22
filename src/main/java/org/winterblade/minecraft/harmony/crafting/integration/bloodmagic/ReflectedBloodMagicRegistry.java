package org.winterblade.minecraft.harmony.crafting.integration.bloodmagic;

import WayofTime.bloodmagic.api.ItemStackWrapper;
import WayofTime.bloodmagic.api.recipe.TartaricForgeRecipe;
import WayofTime.bloodmagic.api.registry.AlchemyArrayRecipeRegistry;
import WayofTime.bloodmagic.api.registry.AltarRecipeRegistry;
import WayofTime.bloodmagic.api.registry.TartaricForgeRecipeRegistry;
import com.google.common.collect.BiMap;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.util.List;

/**
 * Created by Matt on 4/21/2016.
 */
public class ReflectedBloodMagicRegistry {
    private static BiMap<ItemStack, AltarRecipeRegistry.AltarRecipe> altarRecipes;
    private static List<TartaricForgeRecipe> tartaricForgeRecipes;
    private static BiMap<ItemStackWrapper, AlchemyArrayRecipeRegistry.AlchemyArrayRecipe> alchemyArrayRecipes;

    static {
        // I really wish mod authors would better support removing recipes from their mods...
        altarRecipes = ObfuscationReflectionHelper.getPrivateValue(AltarRecipeRegistry.class, null, "recipes");
        tartaricForgeRecipes = ObfuscationReflectionHelper.getPrivateValue(TartaricForgeRecipeRegistry.class, null, "recipeList");
        alchemyArrayRecipes = ObfuscationReflectionHelper.getPrivateValue(AlchemyArrayRecipeRegistry.class, null, "recipes");
    }

    private ReflectedBloodMagicRegistry() {}

    public static void addAltarRecipe(AltarRecipeRegistry.AltarRecipe recipe) {
        AltarRecipeRegistry.registerRecipe(recipe);
    }

    public static void removeAltarRecipe(AltarRecipeRegistry.AltarRecipe recipe) {
        altarRecipes.remove(recipe.getInput());
    }
}
