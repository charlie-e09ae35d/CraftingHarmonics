package org.winterblade.minecraft.harmony.crafting.integration.techreborn.operations;

import org.winterblade.minecraft.harmony.api.RecipeOperation;
import org.winterblade.minecraft.harmony.crafting.ItemMissingException;
import org.winterblade.minecraft.harmony.crafting.integration.techreborn.RebornRecipeUtils;
import reborncore.api.recipe.IBaseRecipeType;
import techreborn.api.recipe.machines.PlateCuttingMachineRecipe;

/**
 * Created by Matt on 5/8/2016.
 */
@RecipeOperation(name = "TechReborn.addPlateCutter", dependsOn = RebornRecipeUtils.TechRebornModId)
public class AddPlateCutterOperation extends BaseTechRebornAddOperation {
    public AddPlateCutterOperation() {super("Plate Cutter");}

    @Override
    protected IBaseRecipeType getRecipe() throws ItemMissingException {
        return new PlateCuttingMachineRecipe(with[0], what[0], ticks, euPerTick);
    }
}