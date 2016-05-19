package org.winterblade.minecraft.harmony.integration.techreborn.operations;

import org.winterblade.minecraft.harmony.api.RecipeOperation;
import org.winterblade.minecraft.harmony.integration.techreborn.RebornRecipeUtils;
import org.winterblade.minecraft.harmony.crafting.operations.AddShapelessOperation;
import org.winterblade.minecraft.harmony.api.utility.LogHelper;
import techreborn.api.RollingMachineRecipe;

/**
 * Created by Matt on 5/9/2016.
 */
@RecipeOperation(name = "TechReborn.addShapelessRollingMachine", dependsOn = RebornRecipeUtils.TechRebornModId)
public class AddShapelessRollingMachineOperation extends AddShapelessOperation {
    @Override
    public void Apply() {
        LogHelper.info("Adding a shapeless TechReborn Rolling Machine recipe for " + output.toString());
        RollingMachineRecipe.instance.getRecipeList().add(recipe);
    }

    @Override
    public void Undo() {
        RollingMachineRecipe.instance.getRecipeList().remove(recipe);
    }

}
