package org.winterblade.minecraft.harmony.integration.techreborn.operations;

import org.winterblade.minecraft.harmony.api.Operation;
import org.winterblade.minecraft.harmony.api.OperationException;
import org.winterblade.minecraft.harmony.integration.techreborn.RebornRecipeUtils;
import reborncore.api.recipe.IBaseRecipeType;
import techreborn.api.recipe.machines.GrinderRecipe;

/**
 * Created by Matt on 5/8/2016.
 */
@Operation(name = "TechReborn.addGrinder", dependsOn = RebornRecipeUtils.TechRebornModId)
public class AddGrinderOperation extends BaseTechRebornAddOperation {
    public AddGrinderOperation() {super("Grinder");}

    @Override
    protected IBaseRecipeType getRecipe() throws OperationException {
        return new GrinderRecipe(getInput(0), getOutput(0), ticks, euPerTick);
    }
}
