package org.winterblade.minecraft.harmony.crafting.recipes;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.oredict.OreDictionary;
import org.winterblade.minecraft.harmony.crafting.components.RecipeComponent;

/**
 * Created by Matt on 4/9/2016.
 */
public class ShapedComponentRecipe implements IRecipe {
    public static final int MAX_CRAFT_GRID_WIDTH = 3;
    public static final int MAX_CRAFT_GRID_HEIGHT = 3;

    private final int width;
    private final int height;
    private final RecipeComponent[] input;
    private final RecipeComponent output;

    public ShapedComponentRecipe(int width, int height, RecipeComponent[] input, RecipeComponent output) {
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
        for (int x = 0; x < MAX_CRAFT_GRID_WIDTH; x++)
        {
            for (int y = 0; y < MAX_CRAFT_GRID_HEIGHT; y++)
            {
                int subX = x - startX;
                int subY = y - startY;
                RecipeComponent target = null;

                if (subX >= 0 && subY >= 0 && subX < width && subY < height)
                {
                    target = input[width - subX - 1 + subY * width];
                }

                ItemStack slot = inv.getStackInRowAndColumn(x, y);

                if(target == null) {
                    if(slot != null) return false;
                    continue;
                }

                // TODO: Run matchers here...

            }
        }

        return true;
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
        return ForgeHooks.defaultRecipeGetRemainingItems(inv);
    }
}
