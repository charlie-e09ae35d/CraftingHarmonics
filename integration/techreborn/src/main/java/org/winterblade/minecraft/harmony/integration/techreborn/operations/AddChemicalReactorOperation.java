package org.winterblade.minecraft.harmony.integration.techreborn.operations;

import org.winterblade.minecraft.harmony.api.Operation;
import org.winterblade.minecraft.harmony.api.OperationException;
import org.winterblade.minecraft.harmony.integration.techreborn.RebornRecipeUtils;
import reborncore.api.recipe.IBaseRecipeType;
import techreborn.api.recipe.machines.ChemicalReactorRecipe;

/**
 * Created by Matt on 5/8/2016.
 */
@Operation(name = "TechReborn.addChemicalReactor", dependsOn = RebornRecipeUtils.TechRebornModId)
public class AddChemicalReactorOperation extends BaseTechRebornAddOperation {
    public AddChemicalReactorOperation() {super("Chemical Reactor",2);}

    @Override
    protected IBaseRecipeType getRecipe() throws OperationException {
        return new ChemicalReactorRecipe(getInput(0), getInput(1), getOutput(0), ticks, euPerTick);
    }
}
