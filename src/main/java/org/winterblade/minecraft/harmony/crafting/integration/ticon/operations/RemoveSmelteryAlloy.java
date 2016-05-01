package org.winterblade.minecraft.harmony.crafting.integration.ticon.operations;

import net.minecraftforge.fluids.FluidStack;
import org.winterblade.minecraft.harmony.CraftingHarmonicsMod;
import org.winterblade.minecraft.harmony.api.BaseRecipeOperation;
import org.winterblade.minecraft.harmony.api.RecipeOperation;
import org.winterblade.minecraft.harmony.crafting.ItemMissingException;
import org.winterblade.minecraft.harmony.crafting.integration.ticon.ReflectedTinkerRegistry;
import org.winterblade.minecraft.harmony.utility.LogHelper;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.smeltery.AlloyRecipe;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matt on 4/21/2016.
 */
@RecipeOperation(name = "removeSmelteryAlloy", dependsOn = "tconstruct")
public class RemoveSmelteryAlloy extends BaseRecipeOperation {
    private FluidStack what;
    private FluidStack[] with;

    private transient List<AlloyRecipe> recipes = new ArrayList<>();

    @Override
    public void Init() throws ItemMissingException {

    }

    @Override
    public void Apply() {
        recipes.clear();

        LogHelper.info("Removing '" + what.getFluid().getName() + "' alloys from the smeltery.");
        for(AlloyRecipe recipe : TinkerRegistry.getAlloys()) {
            if(!matches(recipe)) continue;
            ReflectedTinkerRegistry.removeAlloy(recipe);
            recipes.add(recipe);
        }
    }

    @Override
    public void Undo() {
        for(AlloyRecipe recipe : recipes) {
            ReflectedTinkerRegistry.addAlloy(recipe);
        }
    }

    private boolean matches(AlloyRecipe recipe) {
        if(!recipe.getResult().isFluidEqual(what)) return false;

        // If that's all we're checking...
        if(with == null || with.length <= 0) return true;

        for (FluidStack fluid : recipe.getFluids()) {
            boolean hasMatch = false;

            for(FluidStack inputFluid : with) {
                if (!fluid.isFluidEqual(inputFluid)) continue;
                hasMatch = true;
                break;
            }

            if(!hasMatch) {
                return false;
            }
        }

        return true;
    }
}
