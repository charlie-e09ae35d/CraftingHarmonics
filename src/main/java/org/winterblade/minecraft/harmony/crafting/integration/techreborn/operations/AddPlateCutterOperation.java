package org.winterblade.minecraft.harmony.crafting.integration.techreborn.operations;

import org.winterblade.minecraft.harmony.api.RecipeOperation;
import org.winterblade.minecraft.harmony.crafting.ItemMissingException;
import org.winterblade.minecraft.harmony.crafting.integration.techreborn.RebornRecipeUtils;
import techreborn.api.recipe.machines.PlateCuttingMachineRecipe;

/**
 * Created by Matt on 5/8/2016.
 */
@RecipeOperation(name = "TechReborn.addPlateCutter", dependsOn = RebornRecipeUtils.TechRebornModId)
public class AddPlateCutterOperation extends BaseTechRebornAddOperation {
    @Override
    public void Init() throws ItemMissingException {
        if(what.length != 1) throw new ItemMissingException("Plate cutter recipes require 1 output item.");
        if(with.length != 1) throw new ItemMissingException("Plate cutter recipes require 1 input item.");
        recipe = new PlateCuttingMachineRecipe(with[0], what[0], ticks, euPerTick);

    }
}
