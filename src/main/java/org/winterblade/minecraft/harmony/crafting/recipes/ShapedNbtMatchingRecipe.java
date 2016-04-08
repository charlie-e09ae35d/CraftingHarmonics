package org.winterblade.minecraft.harmony.crafting.recipes;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.oredict.ShapedOreRecipe;

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
        boolean matches = super.matches(inv, worldIn);
        if(!matches) return false;

        // Now check more specific things...
        List<ItemStack> grid = new ArrayList<>();

        // Get a better list of grid inputs...
        for (int i = 0; i <= 3; ++i) {
            for (int j = 0; j <= 3; ++j) {
                // Get the item in that spot...
                ItemStack slot = inv.getStackInRowAndColumn(i, j);
                if (slot == null) continue;

                grid.add(slot);
            }
        }

        int offset = 0;
        int gridInputs = grid.size();

        // How did this even happen?
        if(recipeItems.length > grid.size()) return false;


        for(ItemStack recipeItem : recipeItems) {
            // Get the current offset item and then increase the offset...
            ItemStack gridItem = grid.get(offset++);

            // If our current item isn't one, or doesn't have a compound, skip it.
            if (gridItem == null || recipeItem == null
                    || !recipeItem.hasTagCompound()) continue;

            // If we were supposed to have a tag, nope it right out of here...
            if (!gridItem.hasTagCompound()) return false;

            NBTTagCompound compound = new NBTTagCompound();
            NBTTagCompound orig = recipeItem.getTagCompound();
            boolean isFuzzy = orig.getBoolean("CraftingHarmonicsIsFuzzyMatch");
            for (String s : orig.getKeySet()) {
                // Skip our tag...
                if (s.equals("CraftingHarmonicsIsFuzzyMatch")) continue;
                compound.setTag(s, orig.getTag(s).copy());
            }

            // Finally, check if the NBT matches exactly...
            // TODO: Fuzzy match
            if (!compound.toString().equals(gridItem.getTagCompound().toString())) return false;
        }

        return true;
    }
}
