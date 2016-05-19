package org.winterblade.minecraft.harmony.integration.ticon.operations;

import net.minecraftforge.fluids.FluidStack;
import org.winterblade.minecraft.harmony.api.BaseRecipeOperation;
import org.winterblade.minecraft.harmony.api.RecipeOperation;
import org.winterblade.minecraft.harmony.api.ItemMissingException;
import org.winterblade.minecraft.harmony.integration.ticon.ReflectedTinkerRegistry;
import org.winterblade.minecraft.harmony.api.utility.LogHelper;
import slimeknights.tconstruct.library.smeltery.AlloyRecipe;

/**
 * Operation for adding an alloy for the smeltery.
 */
@RecipeOperation(name = "addSmelteryAlloy", dependsOn = "tconstruct")
public class AddSmelteryAlloy extends BaseRecipeOperation {
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
    public void Init() throws ItemMissingException {
        if(with == null || with.length < 2) {
            LogHelper.warn("Alloys must contain at least two inputs.");
            throw new ItemMissingException("Alloys must contain at least two inputs.");
        }

        recipe = new AlloyRecipe(what, with);
    }

    @Override
    public void Apply() {
        LogHelper.info("Adding Tinker's smeltery alloy for '" + what.getFluid().getName() + "'.");
        ReflectedTinkerRegistry.addAlloy(recipe);
    }

    @Override
    public void Undo() {
        ReflectedTinkerRegistry.removeAlloy(recipe);
    }
}
