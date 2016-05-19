package org.winterblade.minecraft.harmony.integration.techreborn.operations;

import org.winterblade.minecraft.harmony.api.RecipeOperation;
import org.winterblade.minecraft.harmony.integration.techreborn.RebornRecipeUtils;
import org.winterblade.minecraft.harmony.crafting.operations.AddShapedOperation;
import org.winterblade.minecraft.harmony.api.utility.LogHelper;
import techreborn.api.RollingMachineRecipe;

/**
 * Created by Matt on 5/9/2016.
 */
@RecipeOperation(name = "TechReborn.addShapedRollingMachine", dependsOn = RebornRecipeUtils.TechRebornModId)
public class AddShapedRollingMachineOperation extends AddShapedOperation {
    @Override
    public void Apply() {
        LogHelper.info("Adding a shaped TechReborn Rolling Machine recipe for " + output.toString());
        RollingMachineRecipe.instance.getRecipeList().add(recipe);
    }

    @Override
    public void Undo() {
        RollingMachineRecipe.instance.getRecipeList().remove(recipe);
    }
}
