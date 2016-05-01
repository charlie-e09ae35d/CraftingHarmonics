package org.winterblade.minecraft.harmony.crafting.integration.ticon.operations;

import org.winterblade.minecraft.harmony.api.RecipeOperation;
import org.winterblade.minecraft.harmony.crafting.ItemMissingException;
import org.winterblade.minecraft.harmony.crafting.ItemRegistry;
import org.winterblade.minecraft.harmony.crafting.integration.ticon.ReflectedTinkerRegistry;
import org.winterblade.minecraft.harmony.utility.LogHelper;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.smeltery.CastingRecipe;

import java.util.List;

/**
 * Created by Matt on 4/25/2016.
 */
@RecipeOperation(name = "removeSmelteryTableCast", dependsOn = "tconstruct")
public class RemoveSmelteryTableCast extends RemoveSmelteryCast {

    @Override
    public void Init() throws ItemMissingException {
        List<CastingRecipe> recipeList = TinkerRegistry.getAllTableCastingRecipes();

        for(CastingRecipe recipe : recipeList) {
            if(!matches(recipe)) continue;
            recipes.add(recipe);
        }
    }

    @Override
    public void Apply() {
        LogHelper.info("Removing Tinker's table cast for '" + ItemRegistry.outputItemName(what) + "'.");
        for(CastingRecipe recipe : recipes) {
            ReflectedTinkerRegistry.removeTableCast(recipe);
        }
    }

    @Override
    public void Undo() {
        for(CastingRecipe recipe : recipes) {
            ReflectedTinkerRegistry.addTableCast(recipe);
        }
    }
}
