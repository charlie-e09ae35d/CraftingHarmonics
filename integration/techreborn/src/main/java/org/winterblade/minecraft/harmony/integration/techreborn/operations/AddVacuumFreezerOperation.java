package org.winterblade.minecraft.harmony.integration.techreborn.operations;

import org.winterblade.minecraft.harmony.api.Operation;
import org.winterblade.minecraft.harmony.api.OperationException;
import org.winterblade.minecraft.harmony.integration.techreborn.RebornRecipeUtils;
import reborncore.api.recipe.IBaseRecipeType;
import techreborn.api.recipe.machines.VacuumFreezerRecipe;

/**
 * Created by Matt on 5/8/2016.
 */
@Operation(name = "TechReborn.addVacuumFreezer", dependsOn = RebornRecipeUtils.TechRebornModId)
public class AddVacuumFreezerOperation extends BaseTechRebornAddOperation {
    public AddVacuumFreezerOperation() {super("Vacuum Freezer");}

    @Override
    protected IBaseRecipeType getRecipe() throws OperationException {
        return new VacuumFreezerRecipe(getInput(0), getOutput(0), ticks, euPerTick);
    }
}
