package org.winterblade.minecraft.harmony.integration.ticon.operations;

import org.winterblade.minecraft.harmony.api.OperationException;
import org.winterblade.minecraft.harmony.api.Operation;
import org.winterblade.minecraft.harmony.common.ItemUtility;
import org.winterblade.minecraft.harmony.integration.ticon.ReflectedTinkerRegistry;
import org.winterblade.minecraft.harmony.common.utility.LogHelper;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.smeltery.CastingRecipe;

import java.util.List;

/**
 * Created by Matt on 4/25/2016.
 */
@Operation(name = "removeSmelteryTableCast", dependsOn = "tconstruct")
public class RemoveSmelteryTableCast extends RemoveSmelteryCast {

    @Override
    public void init() throws OperationException {
        List<CastingRecipe> recipeList = TinkerRegistry.getAllTableCastingRecipes();

        for(CastingRecipe recipe : recipeList) {
            if(!matches(recipe)) continue;
            recipes.add(recipe);
        }
    }

    @Override
    public void apply() {
        LogHelper.info("Removing Tinker's table cast for '" + ItemUtility.outputItemName(what) + "'.");
        for(CastingRecipe recipe : recipes) {
            ReflectedTinkerRegistry.removeTableCast(recipe);
        }
    }

    @Override
    public void undo() {
        for(CastingRecipe recipe : recipes) {
            ReflectedTinkerRegistry.addTableCast(recipe);
        }
    }
}
