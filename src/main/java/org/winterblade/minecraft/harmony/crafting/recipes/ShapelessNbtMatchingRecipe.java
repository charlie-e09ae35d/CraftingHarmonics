package org.winterblade.minecraft.harmony.crafting.recipes;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import org.winterblade.minecraft.harmony.crafting.ItemRegistry;
import org.winterblade.minecraft.harmony.crafting.components.RecipeComponent;

/**
 * Created by Matt on 4/7/2016.
 */
public class ShapelessNbtMatchingRecipe extends ShapelessOreRecipe {
    private final RecipeComponent[] components;

    public ShapelessNbtMatchingRecipe(ItemStack result, RecipeComponent[] components, Object... recipe) {
        super(result, recipe);
        this.components = components;
    }

    /**
     * Used to check if a recipe matches current crafting inventory
     *
     * @param inv       The inventory grid
     * @param world     The world to check in
     */
    @Override
    public boolean matches(InventoryCrafting inv, World world) {
        return super.matches(inv, world) && ItemRegistry.CompareRecipeListToCraftingInventory(components, inv);
    }
}
