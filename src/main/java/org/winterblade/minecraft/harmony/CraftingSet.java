package org.winterblade.minecraft.harmony;

import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import org.winterblade.minecraft.harmony.config.operations.IAddOperation;
import org.winterblade.minecraft.harmony.config.operations.IConfigOperation;
import org.winterblade.minecraft.harmony.config.operations.RemoveOperation;
import org.winterblade.minecraft.harmony.crafting.ItemMissingException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Matt on 4/5/2016.
 */
public class CraftingSet {
    private final List<RemoveOperation> removals = new ArrayList<RemoveOperation>();
    private final List<IAddOperation> adds = new ArrayList<IAddOperation>();

    /**
     * Creates a crafting set using the given set of operations
     * @param configOperations
     */
    public CraftingSet(IConfigOperation[] configOperations) {
        for(IConfigOperation op : configOperations) {
            if(op instanceof RemoveOperation) {
                removals.add((RemoveOperation) op);
            } else if(op instanceof IAddOperation) {
                adds.add((IAddOperation) op);
            } else {
                System.err.println("An unknown ConfigOperation was added to a set somehow.");
            }
        }
    }

    public void Apply(CraftingManager manager) {
        // Apply removals first, which requires we have the recipe list:
        List<IRecipe> recipeList = manager.getRecipeList();

        RemoveRecipes(recipeList);

        for(IAddOperation add : adds) {
            try {
                add.Init();
            }
            catch (ItemMissingException ex) {
                System.err.println(ex.getMessage());
                continue;
            }
            IRecipe recipe = add.CreateRecipe();
            System.out.println("Adding recipe for " + recipe.getRecipeOutput().getUnlocalizedName());
            recipeList.add(recipe);
        }
    }

    private void RemoveRecipes(List<IRecipe> recipeList) {
        for(RemoveOperation removal : removals) {
            removal.Init();
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
