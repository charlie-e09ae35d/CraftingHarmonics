package org.winterblade.minecraft.harmony.crafting.converters;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;

/**
 * Created by Matt on 4/5/2016.
 */
public class ShapedRecipeConverter {
    public ItemStack[] GetRecipeOutput(IRecipe recipe) {
        if(!(recipe instanceof ShapedRecipes)) return null;

        ShapedRecipes shaped = (ShapedRecipes) recipe;
        return shaped.recipeItems;
    }
}
