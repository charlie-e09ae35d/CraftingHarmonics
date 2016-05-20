package org.winterblade.minecraft.harmony.integration.ticon.operations;

import net.minecraftforge.fluids.FluidStack;
import org.winterblade.minecraft.harmony.api.BaseRecipeOperation;
import org.winterblade.minecraft.harmony.api.RecipeOperation;
import org.winterblade.minecraft.harmony.api.ItemMissingException;
import org.winterblade.minecraft.harmony.integration.ticon.ReflectedTinkerRegistry;
import org.winterblade.minecraft.harmony.common.utility.LogHelper;

import java.util.Map;

/**
 * Created by Matt on 4/24/2016.
 */
@RecipeOperation(name = "removeSmelteryFuel", dependsOn = "tconstruct")
public class RemoveSmelteryFuel extends BaseRecipeOperation {
    /*
     * Serialized properties
     */
    private FluidStack what;

    /*
     * Computed properties
     */
    private transient Map.Entry<FluidStack, Integer> removedFuel;

    @Override
    public void Init() throws ItemMissingException {

    }

    @Override
    public void Apply() {
        LogHelper.info("Removing Tinker's smeltery fuel using '" + what.getFluid().getName() + "'.");
        removedFuel = ReflectedTinkerRegistry.removeFuel(what);
    }

    @Override
    public void Undo() {
        if(removedFuel == null) return;
        ReflectedTinkerRegistry.addFuel(removedFuel.getKey(), removedFuel.getValue());
    }
}
