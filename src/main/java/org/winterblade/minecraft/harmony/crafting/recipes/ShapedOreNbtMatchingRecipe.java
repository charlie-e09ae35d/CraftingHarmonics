package org.winterblade.minecraft.harmony.crafting.recipes;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.oredict.ShapedOreRecipe;
import org.winterblade.minecraft.harmony.crafting.ItemRegistry;

/**
 * Created by Matt on 4/7/2016.
 */
public class ShapedOreNbtMatchingRecipe extends ShapedOreRecipe {
    public ShapedOreNbtMatchingRecipe(ItemStack result, Object... recipe) {
        super(result, recipe);
    }

    /**
     * Used to check if a recipe matches current crafting inventory
     *
     * @param inv       The inventory to check against
     * @param world     The world to check against
     */
    @Override
    public boolean matches(InventoryCrafting inv, World world) {
        return super.matches(inv, world)
                && ItemRegistry.CompareRecipeListToCraftingInventory(input, inv);
    }

}
