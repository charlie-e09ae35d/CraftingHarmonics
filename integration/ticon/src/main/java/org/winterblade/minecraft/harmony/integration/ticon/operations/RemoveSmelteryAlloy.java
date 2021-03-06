package org.winterblade.minecraft.harmony.integration.ticon.operations;

import net.minecraftforge.fluids.FluidStack;
import org.winterblade.minecraft.harmony.api.BasicOperation;
import org.winterblade.minecraft.harmony.api.Operation;
import org.winterblade.minecraft.harmony.api.OperationException;
import org.winterblade.minecraft.harmony.integration.ticon.ReflectedTinkerRegistry;
import org.winterblade.minecraft.harmony.common.utility.LogHelper;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.smeltery.AlloyRecipe;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matt on 4/21/2016.
 */
@Operation(name = "removeSmelteryAlloy", dependsOn = "tconstruct")
public class RemoveSmelteryAlloy extends BasicOperation {
    private FluidStack what;
    private FluidStack[] with;

    private transient List<AlloyRecipe> recipes = new ArrayList<>();

    @Override
    public void init() throws OperationException {

    }

    @Override
    public void apply() {
        recipes.clear();

        LogHelper.info("Removing '" + what.getFluid().getName() + "' alloys from the smeltery.");
        for(AlloyRecipe recipe : TinkerRegistry.getAlloys()) {
            if(!matches(recipe)) continue;
            ReflectedTinkerRegistry.removeAlloy(recipe);
            recipes.add(recipe);
        }
    }

    @Override
    public void undo() {
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
