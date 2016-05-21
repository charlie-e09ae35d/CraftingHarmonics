package org.winterblade.minecraft.harmony.integration.techreborn.operations;

import org.winterblade.minecraft.harmony.api.Operation;
import org.winterblade.minecraft.harmony.api.OperationException;
import org.winterblade.minecraft.harmony.integration.techreborn.RebornRecipeUtils;
import reborncore.api.recipe.IBaseRecipeType;
import techreborn.api.recipe.machines.AssemblingMachineRecipe;

/**
 * Created by Matt on 5/8/2016.
 */
@Operation(name = "TechReborn.addAssemblingMachine", dependsOn = RebornRecipeUtils.TechRebornModId)
public class AddAssemblingMachineOperation extends BaseTechRebornAddOperation {
    public AddAssemblingMachineOperation() {super("Assembling Machine");}

    @Override
    protected IBaseRecipeType getRecipe() throws OperationException {
        return new AssemblingMachineRecipe(getInput(0), getInput(1), what[0], ticks, euPerTick);
    }
}
