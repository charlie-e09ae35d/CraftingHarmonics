package org.winterblade.minecraft.harmony.crafting.recipes;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.world.World;
import org.winterblade.minecraft.harmony.crafting.ItemRegistry;
import org.winterblade.minecraft.harmony.crafting.components.RecipeComponent;

/**
 * Created by Matt on 4/7/2016.
 */
public class ShapedNbtMatchingRecipe extends ShapedRecipes {
    private final RecipeComponent[] components;

    public ShapedNbtMatchingRecipe(int width, int height, RecipeComponent[] components, ItemStack output) {
        super(width, height, RecipeComponent.getItemStacks(components), output);
        this.components = components;
    }

    @Override
    public boolean matches(InventoryCrafting inv, World worldIn) {
        // Check to see if we match on a general level first...
        return super.matches(inv, worldIn)
                && ItemRegistry.CompareRecipeListToCraftingInventory(components, inv);
    }
}
