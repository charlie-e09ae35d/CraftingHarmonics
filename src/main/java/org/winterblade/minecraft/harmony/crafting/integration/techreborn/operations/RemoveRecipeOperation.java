package org.winterblade.minecraft.harmony.crafting.integration.techreborn.operations;

import com.google.common.base.Joiner;
import net.minecraft.item.ItemStack;
import org.winterblade.minecraft.harmony.api.BaseRecipeOperation;
import org.winterblade.minecraft.harmony.api.RecipeOperation;
import org.winterblade.minecraft.harmony.api.ItemMissingException;
import org.winterblade.minecraft.harmony.crafting.integration.techreborn.RebornRecipeUtils;
import org.winterblade.minecraft.harmony.api.utility.LogHelper;
import reborncore.api.recipe.IBaseRecipeType;
import reborncore.api.recipe.RecipeHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matt on 5/8/2016.
 */
@RecipeOperation(name = "TechReborn.removeRecipe", dependsOn = RebornRecipeUtils.TechRebornModId)
public class RemoveRecipeOperation extends BaseRecipeOperation {
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
    public void Init() throws ItemMissingException {
        if(what == null || what.length <= 0) throw new ItemMissingException("Removing a TechReborn recipe must have at least one output.");
        recipes.clear();

        for(IBaseRecipeType recipe : RecipeHandler.recipeList) {
            if(!matches(recipe)) continue;
            recipes.add(recipe);
        }
    }

    @Override
    public void Apply() {
        LogHelper.info("Removing TechReborn recipes producing: " + Joiner.on(", ").join(what));
        for(IBaseRecipeType recipe : recipes) {
            RebornRecipeUtils.removeRecipe(recipe);
        }
    }

    @Override
    public void Undo() {
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
