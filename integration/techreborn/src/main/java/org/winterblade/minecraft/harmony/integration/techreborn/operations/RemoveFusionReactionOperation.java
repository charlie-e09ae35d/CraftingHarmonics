package org.winterblade.minecraft.harmony.integration.techreborn.operations;

import net.minecraft.item.ItemStack;
import org.winterblade.minecraft.harmony.api.BasicOperation;
import org.winterblade.minecraft.harmony.api.Operation;
import org.winterblade.minecraft.harmony.api.OperationException;
import org.winterblade.minecraft.harmony.integration.techreborn.RebornRecipeUtils;
import org.winterblade.minecraft.harmony.common.utility.LogHelper;
import techreborn.api.reactor.FusionReactorRecipe;
import techreborn.api.reactor.FusionReactorRecipeHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matt on 5/8/2016.
 */
@Operation(name = "TechReborn.removeFusionReaction", dependsOn = RebornRecipeUtils.TechRebornModId)
public class RemoveFusionReactionOperation extends BasicOperation {
    /*
     * Serialized properties
     */
    private ItemStack what;

    /*
     * Computed properties
     */
    private List<FusionReactorRecipe> recipes = new ArrayList<>();

    @Override
    public void init() throws OperationException {
        if(what == null) throw new OperationException("Could not find output for removing TechReborn fusion reaction.");
        recipes.clear();

        for(FusionReactorRecipe recipe : FusionReactorRecipeHelper.reactorRecipes) {
            if(!matches(recipe)) continue;
            recipes.add(recipe);
        }
    }

    @Override
    public void apply() {
        LogHelper.info("Removing TechReborn fusion reaction for " + what.toString());
        for(FusionReactorRecipe recipe : recipes) {
            FusionReactorRecipeHelper.reactorRecipes.remove(recipe);
        }
    }

    @Override
    public void undo() {
        for(FusionReactorRecipe recipe : recipes) {
            FusionReactorRecipeHelper.registerRecipe(recipe);
        }
    }

    private boolean matches(FusionReactorRecipe recipe) {
        return recipe.getOutput().isItemEqualIgnoreDurability(what);
    }
}
