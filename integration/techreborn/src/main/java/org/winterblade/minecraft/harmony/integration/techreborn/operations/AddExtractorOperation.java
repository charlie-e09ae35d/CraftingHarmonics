package org.winterblade.minecraft.harmony.integration.techreborn.operations;

import org.winterblade.minecraft.harmony.api.Operation;
import org.winterblade.minecraft.harmony.api.OperationException;
import org.winterblade.minecraft.harmony.integration.techreborn.RebornRecipeUtils;
import reborncore.api.recipe.IBaseRecipeType;
import techreborn.api.recipe.machines.ExtractorRecipe;

/**
 * Created by Matt on 5/8/2016.
 */
@Operation(name = "TechReborn.addExtractor", dependsOn = RebornRecipeUtils.TechRebornModId)
public class AddExtractorOperation extends BaseTechRebornAddOperation {
    public AddExtractorOperation() {super("Extractor");}

    @Override
    protected IBaseRecipeType getRecipe() throws OperationException {
        return new ExtractorRecipe(getInput(0), getOutput(0), ticks, euPerTick);
    }
}
