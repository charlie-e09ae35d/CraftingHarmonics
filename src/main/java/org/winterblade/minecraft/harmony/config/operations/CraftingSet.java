package org.winterblade.minecraft.harmony.config.operations;

import jdk.nashorn.internal.runtime.regexp.joni.Config;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Matt on 4/5/2016.
 */
public class CraftingSet {
    private final List<RemoveOperation> removals = new ArrayList<RemoveOperation>();

    /**
     * Creates a crafting set using the given set of operations
     * @param configOperations
     */
    public CraftingSet(ConfigOperation[] configOperations) {
        for(ConfigOperation op : configOperations) {
            if(op instanceof RemoveOperation) {
                removals.add((RemoveOperation) op);
            } else {
                System.err.println("An unknown ConfigOperation was added to a set somehow.");
            }
        }
    }

    public void Apply(CraftingManager manager) {
        // Apply removals first, which requires we have the recipe list:
        RemoveRecipes(manager);
    }

    private void RemoveRecipes(CraftingManager manager) {
        List<IRecipe> recipeList = manager.getRecipeList();

        for(RemoveOperation removal : removals) {
            int remove = -1;

            for(Iterator<IRecipe> recipeIterator = recipeList.iterator(); recipeIterator.hasNext(); ) {
                IRecipe recipe = recipeIterator.next();
                if(!removal.Matches(recipe.getRecipeOutput())) continue;

                // We matched something:
                System.out.println("Removing " + recipe.getRecipeOutput().getUnlocalizedName());
                recipeIterator.remove();
            }
        }
    }
}
