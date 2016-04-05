package org.winterblade.minecraft.harmony;

import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import org.winterblade.minecraft.harmony.config.operations.AddShapedOperation;
import org.winterblade.minecraft.harmony.config.operations.IConfigOperation;
import org.winterblade.minecraft.harmony.config.operations.RemoveOperation;

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
    public CraftingSet(IConfigOperation[] configOperations) {
        for(IConfigOperation op : configOperations) {
            op.Init();

            if(op instanceof RemoveOperation) {
                removals.add((RemoveOperation) op);
            } else if(op instanceof AddShapedOperation) {

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
