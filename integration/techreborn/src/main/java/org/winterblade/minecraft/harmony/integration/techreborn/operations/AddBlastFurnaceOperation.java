package org.winterblade.minecraft.harmony.integration.techreborn.operations;

import org.winterblade.minecraft.harmony.api.Operation;
import org.winterblade.minecraft.harmony.api.OperationException;
import org.winterblade.minecraft.harmony.integration.techreborn.RebornRecipeUtils;
import reborncore.api.recipe.IBaseRecipeType;
import techreborn.api.recipe.machines.BlastFurnaceRecipe;

/**
 * Created by Matt on 5/8/2016.
 */
@Operation(name = "TechReborn.addBlastFurnace", dependsOn = RebornRecipeUtils.TechRebornModId)
public class AddBlastFurnaceOperation extends BaseTechRebornAddOperation {
    /*
     * Serialized properties
     */
    private int neededHeat;

    public AddBlastFurnaceOperation() { super("Blast Furnace");}

    @Override
    public IBaseRecipeType getRecipe() throws OperationException {
        return new BlastFurnaceRecipe(getInput(0), getInput(1), getOutput(0), getOutput(1), ticks, euPerTick, neededHeat);
    }
}
