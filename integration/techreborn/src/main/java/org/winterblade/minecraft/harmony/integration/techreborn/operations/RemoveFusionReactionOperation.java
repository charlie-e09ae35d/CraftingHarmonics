package org.winterblade.minecraft.harmony.integration.techreborn.operations;

import net.minecraft.item.ItemStack;
import org.winterblade.minecraft.harmony.api.BaseRecipeOperation;
import org.winterblade.minecraft.harmony.api.RecipeOperation;
import org.winterblade.minecraft.harmony.api.ItemMissingException;
import org.winterblade.minecraft.harmony.integration.techreborn.RebornRecipeUtils;
import org.winterblade.minecraft.harmony.api.utility.LogHelper;
import techreborn.api.reactor.FusionReactorRecipe;
import techreborn.api.reactor.FusionReactorRecipeHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matt on 5/8/2016.
 */
@RecipeOperation(name = "TechReborn.removeFusionReaction", dependsOn = RebornRecipeUtils.TechRebornModId)
public class RemoveFusionReactionOperation extends BaseRecipeOperation {
    /*
     * Serialized properties
     */
    private ItemStack what;

    /*
     * Computed properties
     */
    private List<FusionReactorRecipe> recipes = new ArrayList<>();

    @Override
    public void Init() throws ItemMissingException {
        if(what == null) throw new ItemMissingException("Could not find output for removing TechReborn fusion reaction.");
        recipes.clear();

        for(FusionReactorRecipe recipe : FusionReactorRecipeHelper.reactorRecipes) {
            if(!matches(recipe)) continue;
            recipes.add(recipe);
        }
    }

    @Override
    public void Apply() {
        LogHelper.info("Removing TechReborn fusion reaction for " + what.toString());
        for(FusionReactorRecipe recipe : recipes) {
            FusionReactorRecipeHelper.reactorRecipes.remove(recipe);
        }
    }

    @Override
    public void Undo() {
        for(FusionReactorRecipe recipe : recipes) {
            FusionReactorRecipeHelper.registerRecipe(recipe);
        }
    }

    private boolean matches(FusionReactorRecipe recipe) {
        return recipe.getOutput().isItemEqualIgnoreDurability(what);
    }
}
