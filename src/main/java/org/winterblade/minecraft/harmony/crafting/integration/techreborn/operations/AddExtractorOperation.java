package org.winterblade.minecraft.harmony.crafting.integration.techreborn.operations;

import org.winterblade.minecraft.harmony.api.RecipeOperation;
import org.winterblade.minecraft.harmony.api.ItemMissingException;
import org.winterblade.minecraft.harmony.crafting.integration.techreborn.RebornRecipeUtils;
import reborncore.api.recipe.IBaseRecipeType;
import techreborn.api.recipe.machines.ExtractorRecipe;

/**
 * Created by Matt on 5/8/2016.
 */
@RecipeOperation(name = "TechReborn.addExtractor", dependsOn = RebornRecipeUtils.TechRebornModId)
public class AddExtractorOperation extends BaseTechRebornAddOperation {
    public AddExtractorOperation() {super("Extractor");}

    @Override
    protected IBaseRecipeType getRecipe() throws ItemMissingException {
        return new ExtractorRecipe(getInput(0), getOutput(0), ticks, euPerTick);
    }
}
