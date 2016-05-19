package org.winterblade.minecraft.harmony.crafting.integration.techreborn.operations;

import net.minecraftforge.fluids.FluidStack;
import org.winterblade.minecraft.harmony.api.RecipeOperation;
import org.winterblade.minecraft.harmony.api.ItemMissingException;
import org.winterblade.minecraft.harmony.crafting.integration.techreborn.RebornRecipeUtils;
import reborncore.api.recipe.IBaseRecipeType;
import techreborn.api.recipe.machines.IndustrialGrinderRecipe;

/**
 * Created by Matt on 5/8/2016.
 */
@RecipeOperation(name = "TechReborn.addIndustrialGrinder", dependsOn = RebornRecipeUtils.TechRebornModId)
public class AddIndustrialGrinderOperation extends BaseTechRebornAddOperation {
    /*
     * Serialized properties
     */
    FluidStack fluid;

    public AddIndustrialGrinderOperation() {super("Industrial Grinder");}

    @Override
    protected IBaseRecipeType getRecipe() throws ItemMissingException {
        return new IndustrialGrinderRecipe(getInput(0), getInput(1), fluid, getOutput(0), getOutput(1), getOutput(2),
                getOutput(3), ticks, euPerTick);
    }
}
