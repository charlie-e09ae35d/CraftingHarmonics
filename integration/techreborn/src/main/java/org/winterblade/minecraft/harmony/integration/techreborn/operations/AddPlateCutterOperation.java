package org.winterblade.minecraft.harmony.integration.techreborn.operations;

import org.winterblade.minecraft.harmony.api.Operation;
import org.winterblade.minecraft.harmony.api.OperationException;
import org.winterblade.minecraft.harmony.integration.techreborn.RebornRecipeUtils;
import reborncore.api.recipe.IBaseRecipeType;
import techreborn.api.recipe.machines.PlateCuttingMachineRecipe;

/**
 * Created by Matt on 5/8/2016.
 */
@Operation(name = "TechReborn.addPlateCutter", dependsOn = RebornRecipeUtils.TechRebornModId)
public class AddPlateCutterOperation extends BaseTechRebornAddOperation {
    public AddPlateCutterOperation() {super("Plate Cutter");}

    @Override
    protected IBaseRecipeType getRecipe() throws OperationException {
        return new PlateCuttingMachineRecipe(with[0], what[0], ticks, euPerTick);
    }
}
