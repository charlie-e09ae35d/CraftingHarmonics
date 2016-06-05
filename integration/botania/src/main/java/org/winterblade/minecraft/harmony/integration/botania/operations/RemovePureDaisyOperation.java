package org.winterblade.minecraft.harmony.integration.botania.operations;

import org.winterblade.minecraft.harmony.api.BasicOperation;
import org.winterblade.minecraft.harmony.api.Operation;
import org.winterblade.minecraft.harmony.api.OperationException;
import org.winterblade.minecraft.harmony.common.blocks.BlockMatcher;
import org.winterblade.minecraft.harmony.common.utility.LogHelper;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.recipe.RecipePureDaisy;

/**
 * Created by Matt on 6/5/2016.
 */
@Operation(name = "Botania.removePureDaisy", dependsOn = "Botania")
public class RemovePureDaisyOperation extends BasicOperation {
    /*
     * Serialized properties
     */
    private BlockMatcher what;

    /*
     * Computed properties
     */
    private transient RecipePureDaisy recipe;

    /**
     * Called to initialize the set
     *
     * @throws OperationException If something went wrong
     */
    @Override
    public void init() throws OperationException {
        if(what == null) throw new OperationException("You must specify the output ('what') to remove for pure daisy recipes.");
        for (RecipePureDaisy daisyRecipe : BotaniaAPI.pureDaisyRecipes) {
            if(!what.matches(daisyRecipe.getOutputState())) continue;
            recipe = daisyRecipe;
            break;
        }
    }

    /**
     * Called to apply the set (if not player-specific)
     */
    @Override
    public void apply() {
        LogHelper.info("Removing recipe for {} to the Botania pure daisy.", what.getBlock().getLocalizedName());
        BotaniaAPI.pureDaisyRecipes.remove(recipe);
    }

    /**
     * Called to remove the set (if not player-specific)
     */
    @Override
    public void undo() {
        BotaniaAPI.pureDaisyRecipes.add(recipe);
    }
}
