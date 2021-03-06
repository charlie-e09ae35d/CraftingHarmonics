package org.winterblade.minecraft.harmony.integration.techreborn.operations;

import org.winterblade.minecraft.harmony.api.Operation;
import org.winterblade.minecraft.harmony.common.ItemUtility;
import org.winterblade.minecraft.harmony.integration.techreborn.RebornRecipeUtils;
import org.winterblade.minecraft.harmony.common.crafting.operations.AddShapedOperation;
import org.winterblade.minecraft.harmony.common.utility.LogHelper;
import techreborn.api.RollingMachineRecipe;

/**
 * Created by Matt on 5/9/2016.
 */
@Operation(name = "TechReborn.addShapedRollingMachine", dependsOn = RebornRecipeUtils.TechRebornModId)
public class AddShapedRollingMachineOperation extends AddShapedOperation {
    @Override
    public void apply() {
        LogHelper.info("Adding a shaped TechReborn Rolling Machine recipe for " + ItemUtility.outputItemName(output.getItemStack()));
        RollingMachineRecipe.instance.getRecipeList().add(recipe);
    }

    @Override
    public void undo() {
        RollingMachineRecipe.instance.getRecipeList().remove(recipe);
    }
}
