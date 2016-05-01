package org.winterblade.minecraft.harmony.crafting.integration.ticon.operations;

import net.minecraftforge.fluids.FluidStack;
import org.winterblade.minecraft.harmony.api.BaseRecipeOperation;
import org.winterblade.minecraft.harmony.api.RecipeOperation;
import org.winterblade.minecraft.harmony.crafting.ItemMissingException;
import org.winterblade.minecraft.harmony.crafting.integration.ticon.ReflectedTinkerRegistry;
import org.winterblade.minecraft.harmony.utility.LogHelper;

/**
 * Created by Matt on 4/24/2016.
 */
@RecipeOperation(name = "addSmelteryFuel", dependsOn = "tconstruct")
public class AddSmelteryFuel extends BaseRecipeOperation {
    /*
     * Serialized properties
     */
    private FluidStack what;
    private int duration;

    @Override
    public void Init() throws ItemMissingException {

    }

    @Override
    public void Apply() {
        LogHelper.info("Adding Tinker's smeltery fuel using '" + what.getFluid().getName() + "'.");
        ReflectedTinkerRegistry.addFuel(what, duration);
    }

    @Override
    public void Undo() {
        ReflectedTinkerRegistry.removeFuel(what);
    }
}
