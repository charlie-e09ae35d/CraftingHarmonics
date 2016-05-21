package org.winterblade.minecraft.harmony.integration.techreborn.operations;

import org.winterblade.minecraft.harmony.api.Operation;
import org.winterblade.minecraft.harmony.common.ItemUtility;
import org.winterblade.minecraft.harmony.integration.techreborn.RebornRecipeUtils;
import org.winterblade.minecraft.harmony.common.crafting.operations.AddShapelessOperation;
import org.winterblade.minecraft.harmony.common.utility.LogHelper;
import techreborn.api.RollingMachineRecipe;

/**
 * Created by Matt on 5/9/2016.
 */
@Operation(name = "TechReborn.addShapelessRollingMachine", dependsOn = RebornRecipeUtils.TechRebornModId)
public class AddShapelessRollingMachineOperation extends AddShapelessOperation {
    @Override
    public void apply() {
        LogHelper.info("Adding a shapeless TechReborn Rolling Machine recipe for " + ItemUtility.outputItemName(output.getItemStack()));
        RollingMachineRecipe.instance.getRecipeList().add(recipe);
    }

    @Override
    public void undo() {
        RollingMachineRecipe.instance.getRecipeList().remove(recipe);
    }

}
