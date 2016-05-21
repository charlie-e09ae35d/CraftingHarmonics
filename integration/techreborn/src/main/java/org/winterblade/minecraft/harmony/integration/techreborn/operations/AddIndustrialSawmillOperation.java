package org.winterblade.minecraft.harmony.integration.techreborn.operations;

import net.minecraftforge.fluids.FluidStack;
import org.winterblade.minecraft.harmony.api.Operation;
import org.winterblade.minecraft.harmony.api.OperationException;
import org.winterblade.minecraft.harmony.integration.techreborn.RebornRecipeUtils;
import reborncore.api.recipe.IBaseRecipeType;
import techreborn.api.recipe.machines.IndustrialSawmillRecipe;

/**
 * Created by Matt on 5/8/2016.
 */
@Operation(name = "TechReborn.addSawmill", dependsOn = RebornRecipeUtils.TechRebornModId)
public class AddIndustrialSawmillOperation extends BaseTechRebornAddOperation {
    /*
     * Serialized properties
     */
    private FluidStack fluid;
    private boolean useOreDict;

    public AddIndustrialSawmillOperation() {super("Sawmill");}

    @Override
    protected IBaseRecipeType getRecipe() throws OperationException {
        return new IndustrialSawmillRecipe(getInput(0), getInput(1), fluid, getOutput(0), getOutput(1),
                getOutput(2), ticks, euPerTick, useOreDict);
    }
}
