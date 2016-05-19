package org.winterblade.minecraft.harmony.crafting.integration.techreborn.operations;

import org.winterblade.minecraft.harmony.api.RecipeOperation;
import org.winterblade.minecraft.harmony.api.ItemMissingException;
import org.winterblade.minecraft.harmony.crafting.integration.techreborn.RebornRecipeUtils;
import reborncore.api.recipe.IBaseRecipeType;
import techreborn.api.recipe.machines.GrinderRecipe;

/**
 * Created by Matt on 5/8/2016.
 */
@RecipeOperation(name = "TechReborn.addGrinder", dependsOn = RebornRecipeUtils.TechRebornModId)
public class AddGrinderOperation extends BaseTechRebornAddOperation {
    public AddGrinderOperation() {super("Grinder");}

    @Override
    protected IBaseRecipeType getRecipe() throws ItemMissingException {
        return new GrinderRecipe(getInput(0), getOutput(0), ticks, euPerTick);
    }
}
