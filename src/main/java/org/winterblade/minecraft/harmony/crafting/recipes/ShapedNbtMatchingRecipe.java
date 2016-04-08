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
        List<ItemStack> grid = getGridAsList(inv);

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

            // If we're not fuzzy, then...
            if (!isFuzzy) {
                // We need to check if the NBT matches exactly...
                if(!compound.toString().equals(gridItem.getTagCompound().toString())) return false;
                continue; // Skip the fuzzy check below otherwise
            }

            return checkIfAtLeastAllTagsArePresent(compound, gridItem.getTagCompound());
        }

        return true;
    }

    /**
     * Checks if at least all tags in the first parameter are in the second
     * @param source    The source to check
     * @param dest      The destination to confirm there are at least the same tags in
     * @return          True if they are present in the second.
     */
    private boolean checkIfAtLeastAllTagsArePresent(NBTTagCompound source, NBTTagCompound dest) {
        for(String s : source.getKeySet()) {
            // If we don't have it, bail...
            if(!dest.hasKey(s)) return false;

            NBTBase sourceTag = source.getTag(s);
            NBTBase destTag = dest.getTag(s);

            // If our tags aren't the same type
            if(sourceTag.getId() != destTag.getId()) return false;

            // If we have an NBTTagCompound
            if(sourceTag instanceof NBTTagCompound) {
                // Go deeper...
                if(!checkIfAtLeastAllTagsArePresent((NBTTagCompound)sourceTag, (NBTTagCompound)destTag)) return false;
            } else if(!sourceTag.equals(destTag)) return false; // Non-compound tag, so, we're fine.
        }

        return true;
    }

    /**
     * Turns the inventory grid into a List
     * @param inv   The inventory grid to convert
     * @return      The grid as a List, skipping nulls
     */
    private List<ItemStack> getGridAsList(InventoryCrafting inv) {
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

        return grid;
    }
}
