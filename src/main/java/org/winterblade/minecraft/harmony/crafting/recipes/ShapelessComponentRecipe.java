package org.winterblade.minecraft.harmony.crafting.recipes;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import org.winterblade.minecraft.harmony.api.crafting.RecipeInput;

import java.util.*;

/**
 * Created by Matt on 4/12/2016.
 */
public class ShapelessComponentRecipe extends ShapelessOreRecipe {
    private final RecipeInput[] recipe;

    public ShapelessComponentRecipe(ItemStack result, RecipeInput[] recipe) {
        super(result, RecipeInput.getFacsimileItems(recipe));
        this.recipe = recipe;
    }

    /**
     * Used to check if a recipe matches current crafting inventory
     *
     * @param inv   The inventory to query
     * @param world The world it's happening in.
     */
    @Override
    public boolean matches(InventoryCrafting inv, World world) {
        return recipe.length > 0 && getInputMap(inv) != null;
    }

    @Override
    public ItemStack[] getRemainingItems(InventoryCrafting inv) {
        // Get our input map; fall back to base functionality if it fails (somehow)
        Map<Integer, RecipeInput> inputMap = getInputMap(inv);
        if(inputMap == null) return super.getRemainingItems(inv);

        ItemStack[] ret = new ItemStack[inv.getSizeInventory()];

        for(Map.Entry<Integer, RecipeInput> input : inputMap.entrySet()) {
            // Get the stack in the given slot; also bypass if this is somehow
            // now null.  Seriously?
            ItemStack slot = inv.getStackInSlot(input.getKey());
            if(slot == null) continue;

            // Duplicate and transform.  Makes me think Replicators...
            ItemStack transformed = input.getValue()
                    .applyTransformers(
                            slot.copy(),
                            ForgeHooks.getCraftingPlayer());

            // Check if we're destroying the item or not...
            ret[input.getKey()] = transformed != null && transformed.stackSize != 0
                    ? transformed
                    : ForgeHooks.getContainerItem(transformed);
        }

        return ret;
    }

    /**
     * Gets a map between the input items and our recipe inputs
     * @param inv   The inventory to query
     * @return      The map between the slot number and the input.
     */
    private Map<Integer, RecipeInput> getInputMap(InventoryCrafting inv) {
        Map<Integer, RecipeInput> inputMap = new HashMap<>();

        // Copy our list...
        @SuppressWarnings("unchecked")
        List<RecipeInput> openList = new ArrayList<>(Arrays.asList(recipe));

        // Go through the inventory
        for (int x = 0; x < inv.getSizeInventory(); x++)
        {
            // Do we have an item here?
            ItemStack slot = inv.getStackInSlot(x);
            if (slot == null) continue;

            boolean inRecipe = false;
            Iterator<RecipeInput> openIterator = openList.iterator();

            // Go through our open list...
            while (openIterator.hasNext())
            {
                // Get our input and check it...
                RecipeInput recipeInput = openIterator.next();
                if (!recipeInput.matches(slot,inv, output)) continue;

                inRecipe = true;
                openIterator.remove();
                inputMap.put(x, recipeInput);
                break;
            }

            if (!inRecipe) return null;
        }

        return openList.isEmpty() ? inputMap : null;

    }
}
