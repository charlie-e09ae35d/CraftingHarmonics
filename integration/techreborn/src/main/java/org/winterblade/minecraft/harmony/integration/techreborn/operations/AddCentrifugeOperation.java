package org.winterblade.minecraft.harmony.integration.techreborn.operations;

import org.winterblade.minecraft.harmony.api.Operation;
import org.winterblade.minecraft.harmony.api.OperationException;
import org.winterblade.minecraft.harmony.integration.techreborn.RebornRecipeUtils;
import reborncore.api.recipe.IBaseRecipeType;
import techreborn.api.recipe.machines.CentrifugeRecipe;

/**
 * Created by Matt on 5/8/2016.
 */
@Operation(name = "TechReborn.addCentrifuge", dependsOn = RebornRecipeUtils.TechRebornModId)
public class AddCentrifugeOperation extends BaseTechRebornAddOperation {
    public AddCentrifugeOperation() {super("Centrifuge");}

    /*
     * Serialized properties
     */
    public boolean useOreDict;

    @Override
    protected IBaseRecipeType getRecipe() throws OperationException {
        return new CentrifugeRecipe(getInput(0), getInput(1), getOutput(0), getOutput(1), getOutput(2), getOutput(3),
                ticks, euPerTick, useOreDict);
    }
}
