package org.winterblade.minecraft.harmony.crafting.integration.techreborn.operations;

import org.winterblade.minecraft.harmony.api.RecipeOperation;
import org.winterblade.minecraft.harmony.crafting.ItemMissingException;
import org.winterblade.minecraft.harmony.crafting.integration.techreborn.RebornRecipeUtils;
import techreborn.api.recipe.machines.BlastFurnaceRecipe;
import techreborn.api.recipe.machines.PlateCuttingMachineRecipe;

/**
 * Created by Matt on 5/8/2016.
 */
@RecipeOperation(name = "TechReborn.addBlastFurnace", dependsOn = RebornRecipeUtils.TechRebornModId)
public class AddBlastFurnaceOperation extends BaseTechRebornAddOperation {
    /*
     * Serialized properties
     */
    private int neededHeat;

    @Override
    public void Init() throws ItemMissingException {
        if(what.length < 1) throw new ItemMissingException("Blast furnace recipes require at least 1 output item.");
        if(with.length < 1) throw new ItemMissingException("Blast furnace recipes require at least 1 input item.");
        recipe = new BlastFurnaceRecipe(getInput(0), getInput(1), getOutput(0), getOutput(1), ticks, euPerTick, neededHeat);
    }
}
