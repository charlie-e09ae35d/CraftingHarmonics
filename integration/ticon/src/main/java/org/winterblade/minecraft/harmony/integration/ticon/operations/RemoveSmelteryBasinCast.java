package org.winterblade.minecraft.harmony.integration.ticon.operations;

import org.winterblade.minecraft.harmony.api.RecipeOperation;
import org.winterblade.minecraft.harmony.api.ItemMissingException;
import org.winterblade.minecraft.harmony.common.ItemUtility;
import org.winterblade.minecraft.harmony.integration.ticon.ReflectedTinkerRegistry;
import org.winterblade.minecraft.harmony.common.utility.LogHelper;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.smeltery.CastingRecipe;

import java.util.List;

/**
 * Created by Matt on 4/25/2016.
 */
@RecipeOperation(name = "removeSmelteryBasinCast", dependsOn = "tconstruct")
public class RemoveSmelteryBasinCast extends RemoveSmelteryCast {

    @Override
    public void Init() throws ItemMissingException {
        List<CastingRecipe> recipeList = TinkerRegistry.getAllBasinCastingRecipes();

        for(CastingRecipe recipe : recipeList) {
            if(!matches(recipe)) continue;
            recipes.add(recipe);
        }
    }

    @Override
    public void Apply() {
        LogHelper.info("Removing Tinker's basin cast for '" + ItemUtility.outputItemName(what) + "'.");
        for(CastingRecipe recipe : recipes) {
            ReflectedTinkerRegistry.removeBasinCast(recipe);
        }
    }

    @Override
    public void Undo() {
        for(CastingRecipe recipe : recipes) {
            ReflectedTinkerRegistry.addBasinCast(recipe);
        }
    }
}
