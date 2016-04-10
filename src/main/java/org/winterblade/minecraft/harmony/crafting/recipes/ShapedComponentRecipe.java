package org.winterblade.minecraft.harmony.crafting.recipes;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.oredict.OreDictionary;
import org.winterblade.minecraft.harmony.crafting.RecipeInput;
import org.winterblade.minecraft.harmony.crafting.components.RecipeComponent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matt on 4/9/2016.
 */
public class ShapedComponentRecipe implements IRecipe {
    public static final int MAX_CRAFT_GRID_WIDTH = 3;
    public static final int MAX_CRAFT_GRID_HEIGHT = 3;

    private final int width;
    private final int height;
    private final RecipeInput[] input;
    private final RecipeComponent output;

    public ShapedComponentRecipe(int width, int height, RecipeInput[] input, RecipeComponent output) {
        this.width = width;
        this.height = height;
        this.input = input;
        this.output = output;
    }

    @Override
    public boolean matches(InventoryCrafting inv, World worldIn) {
        for (int x = 0; x <= MAX_CRAFT_GRID_WIDTH - width; x++)
        {
            for (int y = 0; y <= MAX_CRAFT_GRID_HEIGHT - height; ++y)
            {
                if (checkMatch(inv, x, y, worldIn))
                {
                    return true;
                }
            }
        }

        return false;
    }

    protected boolean checkMatch(InventoryCrafting inv, int startX, int startY, World world)
    {
        boolean hasAtLeastOneMatcher = false;
        for (int x = 0; x < MAX_CRAFT_GRID_WIDTH; x++)
        {
            for (int y = 0; y < MAX_CRAFT_GRID_HEIGHT; y++)
            {
                int subX = x - startX;
                int subY = y - startY;
                int pos = subX + subY * width;
                RecipeInput target = null;

                if (subX >= 0 && subY >= 0 && subX < width && subY < height)
                {
                    target = input[pos];
                }

                ItemStack slot = inv.getStackInRowAndColumn(x, y);

                // If we're null...
                if(RecipeInput.isNullOrEmpty(target)) {
                    // .. and we need to not be, bail:
                    if(slot != null) return false;
                    // Otherwise, don't run matchers
                    continue;
                }

                // If the slot is null, and it's not supposed to be...
                if(slot == null) return false;

                // Run matchers here...
                hasAtLeastOneMatcher = true;
                if(!target.matches(slot,inv, x,y,world, pos, output.getItemStack())) return false;
            }
        }

        // Prevent bad recipes from being 'default':
        return hasAtLeastOneMatcher;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inv) {
        return output.getItemStack().copy();
    }

    @Override
    public int getRecipeSize() {
        return input.length;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return output.getItemStack();
    }

    @Override
    public ItemStack[] getRemainingItems(InventoryCrafting inv) {
        // TODO: Not this.
        ItemStack[] ret = new ItemStack[inv.getSizeInventory()];
        RecipeInput target;

        // Lower right
        int left = 2;
        int top = 2;

        // Find the left/top of our recipe
        for(int y = 0; y <= inv.getHeight() - height; y++) {
            // We have to iterate every column, because there might be a null item in the upper left...
            for(int x = 0; x <= inv.getWidth(); x++) {
                ItemStack slot = inv.getStackInRowAndColumn(x, y);
                if(slot == null) continue;

                if(x < left) left = x;
                if(y < top) {
                    top = y;
                    // however, once we find that top item, we can break.
                    break;
                }
            }
        }

        // Shift
        int invOffset = left + (top * inv.getWidth());

        for (int i = 0; i < ret.length; i++)
        {
            if(i >= input.length) {
                ret[i] = null;
                continue;
            }

            target = input[i];
            ItemStack slot = inv.getStackInSlot(i+invOffset);
            ret[i] = ForgeHooks.getContainerItem(inv.getStackInSlot(i));
        }

        return ret;
    }
}
