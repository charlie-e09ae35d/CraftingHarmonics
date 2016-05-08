package org.winterblade.minecraft.harmony.crafting.integration.techreborn.operations;

import org.winterblade.minecraft.harmony.api.RecipeOperation;
import org.winterblade.minecraft.harmony.crafting.ItemMissingException;
import org.winterblade.minecraft.harmony.crafting.integration.techreborn.RebornRecipeUtils;
import reborncore.api.recipe.IBaseRecipeType;
import techreborn.api.recipe.machines.AssemblingMachineRecipe;

/**
 * Created by Matt on 5/8/2016.
 */
@RecipeOperation(name = "TechReborn.addAssemblingMachine", dependsOn = RebornRecipeUtils.TechRebornModId)
public class AddAssemblingMachineRecipe extends BaseTechRebornAddOperation {
    public AddAssemblingMachineRecipe() {super("Assembling Machine");}

    @Override
    protected IBaseRecipeType getRecipe() throws ItemMissingException {
        return new AssemblingMachineRecipe(getInput(0), getInput(1), what[0], ticks, euPerTick);
    }
}
