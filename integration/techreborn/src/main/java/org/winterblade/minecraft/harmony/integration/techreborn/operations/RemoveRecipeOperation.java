package org.winterblade.minecraft.harmony.integration.techreborn.operations;

import com.google.common.base.Joiner;
import net.minecraft.item.ItemStack;
import org.winterblade.minecraft.harmony.api.BasicOperation;
import org.winterblade.minecraft.harmony.api.Operation;
import org.winterblade.minecraft.harmony.api.OperationException;
import org.winterblade.minecraft.harmony.integration.techreborn.RebornRecipeUtils;
import org.winterblade.minecraft.harmony.common.utility.LogHelper;
import reborncore.api.recipe.IBaseRecipeType;
import reborncore.api.recipe.RecipeHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matt on 5/8/2016.
 */
@Operation(name = "TechReborn.removeRecipe", dependsOn = RebornRecipeUtils.TechRebornModId)
public class RemoveRecipeOperation extends BasicOperation {
    /*
     * Serialized properties
     */
    private ItemStack[] what;
    private String machine;

    /*
     * Computed properties
     */
    private transient List<IBaseRecipeType> recipes = new ArrayList<>();

    @Override
    public void init() throws OperationException {
        if(what == null || what.length <= 0) throw new OperationException("Removing a TechReborn recipe must have at least one output.");
        recipes.clear();

        for(IBaseRecipeType recipe : RecipeHandler.recipeList) {
            if(!matches(recipe)) continue;
            recipes.add(recipe);
        }
    }

    @Override
    public void apply() {
        LogHelper.info("Removing TechReborn recipes producing: " + Joiner.on(", ").join(what));
        for(IBaseRecipeType recipe : recipes) {
            RebornRecipeUtils.removeRecipe(recipe);
        }
    }

    @Override
    public void undo() {
        for(IBaseRecipeType recipe : recipes) {
            RebornRecipeUtils.addRecipe(recipe);
        }
    }

    private boolean matches(IBaseRecipeType recipe) {
        // If we have a machine set, check it:
        if(machine != null && !machine.equals(recipe.getUserFreindlyName())) return false;

        // Check lengths:
        if(what.length != recipe.getOutputsSize()) return false;

        // Match our outputs:
        for(int i = 0; i < what.length; i++) {
            if(!what[i].isItemEqualIgnoreDurability(recipe.getOutput(i))) return false;
        }

        return true;
    }
}
