package org.winterblade.minecraft.harmony.integration.techreborn;

import reborncore.api.recipe.IBaseRecipeType;
import reborncore.api.recipe.RecipeHandler;

/**
 * Created by Matt on 4/28/2016.
 */
//
public class RebornRecipeUtils {
    public static final String TechRebornModId = "techreborn";
    private RebornRecipeUtils() { }

    public static void addRecipe(IBaseRecipeType recipe) {
        RecipeHandler.addRecipe(recipe);
    }



    public static void removeRecipe(IBaseRecipeType recipe) {
        RecipeHandler.recipeList.remove(recipe);
    }
}
