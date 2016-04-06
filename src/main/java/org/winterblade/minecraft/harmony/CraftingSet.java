package org.winterblade.minecraft.harmony;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import org.winterblade.minecraft.harmony.config.operations.IAddOperation;
import org.winterblade.minecraft.harmony.config.operations.IConfigOperation;
import org.winterblade.minecraft.harmony.config.operations.RemoveOperation;
import org.winterblade.minecraft.harmony.crafting.ItemMissingException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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

    /**
     * Initializes the operations
     */
    public void Init() {
        InitOperationList(removals);
        InitOperationList(adds);
    }

    /**
     * Apply recipe updates to the crafting manager.
     * @param manager   The crafting manager to update.
     */
    public void Apply(CraftingManager manager) {
        List<IRecipe> recipeList = manager.getRecipeList();

        RemoveRecipes(recipeList);
        AddRecipes(recipeList);
    }

    /**
     * Adds all recipes from this set.
     * @param recipeList    The recipe list to add to.
     */
    public void AddRecipes(List<IRecipe> recipeList) {
        for(IAddOperation add : adds) {
            IRecipe recipe = add.CreateRecipe();
            System.out.println("Adding recipe for " + recipe.getRecipeOutput().getUnlocalizedName());
            recipeList.add(recipe);
        }
    }

    /**
     * Remove all matching recipes from the given recipe list.
     * @param recipeList    The recipe list to remove from.
     */
    public void RemoveRecipes(List<IRecipe> recipeList) {
        for(RemoveOperation removal : removals) {
            for(Iterator<IRecipe> recipeIterator = recipeList.iterator(); recipeIterator.hasNext(); ) {
                IRecipe recipe = recipeIterator.next();
                if(!removal.Matches(recipe.getRecipeOutput())) continue;

                // We matched something:
                System.out.println("Removing " + recipe.getRecipeOutput().getUnlocalizedName());
                recipeIterator.remove();
            }
        }
    }

    /**
     * Removes recipes from the furnace recipe list.
     * @param smeltingList  The list of furnace recipes
     */
    public void RemoveFurnaceRecipes(Map<ItemStack, ItemStack> smeltingList) {
        for(RemoveOperation removal : removals) {
            for(Iterator<Map.Entry<ItemStack, ItemStack>> furnaceIterator = smeltingList.entrySet().iterator(); furnaceIterator.hasNext(); ) {
                Map.Entry<ItemStack, ItemStack> recipe = furnaceIterator.next();
                if(!removal.Matches(recipe.getValue())) continue;

                // We matched something:
                System.out.println("Removing " + recipe.getValue().getUnlocalizedName() + " from the furnace.");
                furnaceIterator.remove();
            }
        }
    }

    /**
     * Initialize a given operation list.
     * @param ops   The operations to initialize.
     */
    private void InitOperationList(List<? extends IConfigOperation> ops) {
        for(IConfigOperation op : ops) {
            try {
                op.Init();
            } catch (ItemMissingException ex) {
                System.err.println(ex.getMessage());
            }
        }
    }
}
