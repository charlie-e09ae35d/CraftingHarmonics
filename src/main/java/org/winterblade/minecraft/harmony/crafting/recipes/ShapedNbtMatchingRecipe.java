package org.winterblade.minecraft.harmony.crafting.recipes;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.world.World;
import org.winterblade.minecraft.harmony.crafting.ItemRegistry;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matt on 4/7/2016.
 */
public class ShapedNbtMatchingRecipe extends ShapedRecipes {
    public ShapedNbtMatchingRecipe(int width, int height, ItemStack[] input, ItemStack output) {
        super(width, height, input, output);
    }

    @Override
    public boolean matches(InventoryCrafting inv, World worldIn) {
        // Check to see if we match on a general level first...
        return super.matches(inv, worldIn)
                && ItemRegistry.CompareRecipeListToCraftingInventory(recipeItems, inv);
    }
}
