package org.winterblade.minecraft.harmony.integration.techreborn.operations;

import org.winterblade.minecraft.harmony.api.RecipeOperation;
import org.winterblade.minecraft.harmony.api.ItemMissingException;
import org.winterblade.minecraft.harmony.integration.techreborn.RebornRecipeUtils;
import reborncore.api.recipe.IBaseRecipeType;
import techreborn.api.recipe.machines.ImplosionCompressorRecipe;

/**
 * Created by Matt on 5/8/2016.
 */
@RecipeOperation(name = "TechReborn.addImplosionCompressor", dependsOn = RebornRecipeUtils.TechRebornModId)
public class AddImplosionCompressorOperation extends BaseTechRebornAddOperation {
    public AddImplosionCompressorOperation() {super("Implosion Compressor", 2);}

    @Override
    protected IBaseRecipeType getRecipe() throws ItemMissingException {
        return new ImplosionCompressorRecipe(getInput(0), getInput(1), getOutput(0), getOutput(1), ticks, euPerTick);
    }
}
