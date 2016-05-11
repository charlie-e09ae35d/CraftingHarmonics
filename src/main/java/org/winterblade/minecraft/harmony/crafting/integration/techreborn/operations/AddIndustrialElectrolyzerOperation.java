package org.winterblade.minecraft.harmony.crafting.integration.techreborn.operations;

import org.winterblade.minecraft.harmony.api.RecipeOperation;
import org.winterblade.minecraft.harmony.crafting.ItemMissingException;
import org.winterblade.minecraft.harmony.crafting.integration.techreborn.RebornRecipeUtils;
import reborncore.api.recipe.IBaseRecipeType;
import techreborn.api.recipe.machines.IndustrialElectrolyzerRecipe;

/**
 * Created by Matt on 5/8/2016.
 */
@RecipeOperation(name = "TechReborn.addIndustrialElectrolyzer", dependsOn = RebornRecipeUtils.TechRebornModId)
public class AddIndustrialElectrolyzerOperation extends BaseTechRebornAddOperation {
    public AddIndustrialElectrolyzerOperation() {super("Industrial Electrolyzer");}

    @Override
    protected IBaseRecipeType getRecipe() throws ItemMissingException {
        return new IndustrialElectrolyzerRecipe(getInput(0), getInput(1), getOutput(0), getOutput(1),
                getOutput(2), getOutput(3), ticks, euPerTick);
    }
}
