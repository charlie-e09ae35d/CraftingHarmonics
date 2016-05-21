package org.winterblade.minecraft.harmony.integration.ticon.operations;

import net.minecraftforge.fluids.FluidStack;
import org.winterblade.minecraft.harmony.api.BasicOperation;
import org.winterblade.minecraft.harmony.api.Operation;
import org.winterblade.minecraft.harmony.api.OperationException;
import org.winterblade.minecraft.harmony.integration.ticon.ReflectedTinkerRegistry;
import org.winterblade.minecraft.harmony.common.utility.LogHelper;
import slimeknights.tconstruct.library.smeltery.AlloyRecipe;

/**
 * Operation for adding an alloy for the smeltery.
 */
@Operation(name = "addSmelteryAlloy", dependsOn = "tconstruct")
public class AddSmelteryAlloy extends BasicOperation {
    /*
     * Serialized properties
     */
    private FluidStack what;
    private FluidStack[] with;

    /*
     * Computed properties
     */
    private AlloyRecipe recipe;

    @Override
    public void init() throws OperationException {
        if(with == null || with.length < 2) {
            LogHelper.warn("Alloys must contain at least two inputs.");
            throw new OperationException("Alloys must contain at least two inputs.");
        }

        recipe = new AlloyRecipe(what, with);
    }

    @Override
    public void apply() {
        LogHelper.info("Adding Tinker's smeltery alloy for '" + what.getFluid().getName() + "'.");
        ReflectedTinkerRegistry.addAlloy(recipe);
    }

    @Override
    public void undo() {
        ReflectedTinkerRegistry.removeAlloy(recipe);
    }
}
