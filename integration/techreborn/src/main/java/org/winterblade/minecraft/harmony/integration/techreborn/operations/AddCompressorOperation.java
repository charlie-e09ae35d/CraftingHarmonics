package org.winterblade.minecraft.harmony.integration.techreborn.operations;

import org.winterblade.minecraft.harmony.api.RecipeOperation;
import org.winterblade.minecraft.harmony.api.ItemMissingException;
import org.winterblade.minecraft.harmony.integration.techreborn.RebornRecipeUtils;
import reborncore.api.recipe.IBaseRecipeType;
import techreborn.api.recipe.machines.CompressorRecipe;

/**
 * Created by Matt on 5/8/2016.
 */
@RecipeOperation(name = "TechReborn.addCompressor", dependsOn = RebornRecipeUtils.TechRebornModId)
public class AddCompressorOperation extends BaseTechRebornAddOperation {
    public AddCompressorOperation() {super("Compressor");}
    @Override
    protected IBaseRecipeType getRecipe() throws ItemMissingException {
        return new CompressorRecipe(getInput(0), getOutput(0), ticks, euPerTick);
    }
}
