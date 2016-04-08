package org.winterblade.minecraft.harmony.crafting.recipes;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import org.winterblade.minecraft.harmony.crafting.ItemRegistry;

/**
 * Created by Matt on 4/7/2016.
 */
public class ShapelessNbtMatchingRecipe extends ShapelessOreRecipe {
    public ShapelessNbtMatchingRecipe(ItemStack result, Object... recipe) {
        super(result, recipe);
    }

    /**
     * Used to check if a recipe matches current crafting inventory
     *
     * @param inv       The inventory grid
     * @param world     The world to check in
     */
    @Override
    public boolean matches(InventoryCrafting inv, World world) {
        return super.matches(inv, world) && ItemRegistry.CompareRecipeListToCraftingInventory(input.toArray(), inv);
    }
}
