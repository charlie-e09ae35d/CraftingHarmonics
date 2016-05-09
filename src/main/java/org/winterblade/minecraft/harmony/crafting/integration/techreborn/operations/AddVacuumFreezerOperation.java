package org.winterblade.minecraft.harmony.crafting.integration.techreborn.operations;

import org.winterblade.minecraft.harmony.api.RecipeOperation;
import org.winterblade.minecraft.harmony.crafting.ItemMissingException;
import org.winterblade.minecraft.harmony.crafting.integration.techreborn.RebornRecipeUtils;
import reborncore.api.recipe.IBaseRecipeType;
import techreborn.api.recipe.machines.VacuumFreezerRecipe;

/**
 * Created by Matt on 5/8/2016.
 */
@RecipeOperation(name = "TechReborn.addVacuumFreezer", dependsOn = RebornRecipeUtils.TechRebornModId)
public class AddVacuumFreezerOperation extends BaseTechRebornAddOperation {
    public AddVacuumFreezerOperation() {super("Vacuum Freezer");}

    @Override
    protected IBaseRecipeType getRecipe() throws ItemMissingException {
        return new VacuumFreezerRecipe(getInput(0), getOutput(0), ticks, euPerTick);
    }
}
