package org.winterblade.minecraft.harmony.crafting.integration.techreborn.operations;

import org.winterblade.minecraft.harmony.api.RecipeOperation;
import org.winterblade.minecraft.harmony.crafting.ItemMissingException;
import org.winterblade.minecraft.harmony.crafting.integration.techreborn.RebornRecipeUtils;
import techreborn.api.recipe.machines.AssemblingMachineRecipe;

/**
 * Created by Matt on 5/8/2016.
 */
@RecipeOperation(name = "TechReborn.addAssemblingMachine", dependsOn = RebornRecipeUtils.TechRebornModId)
public class AddAssemblingMachineRecipe extends BaseTechRebornAddOperation {
    @Override
    public void Init() throws ItemMissingException {
        if(what.length != 1) throw new ItemMissingException("Assembling machine recipes require 1 output item.");
        if(with.length < 1) throw new ItemMissingException("Assembling machine recipes require at least 1 input item.");
        recipe = new AssemblingMachineRecipe(getInput(0), getInput(1), what[0], ticks, euPerTick);
    }
}
