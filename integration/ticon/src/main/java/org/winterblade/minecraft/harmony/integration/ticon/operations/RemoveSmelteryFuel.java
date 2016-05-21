package org.winterblade.minecraft.harmony.integration.ticon.operations;

import net.minecraftforge.fluids.FluidStack;
import org.winterblade.minecraft.harmony.api.BasicOperation;
import org.winterblade.minecraft.harmony.api.Operation;
import org.winterblade.minecraft.harmony.api.OperationException;
import org.winterblade.minecraft.harmony.integration.ticon.ReflectedTinkerRegistry;
import org.winterblade.minecraft.harmony.common.utility.LogHelper;

import java.util.Map;

/**
 * Created by Matt on 4/24/2016.
 */
@Operation(name = "removeSmelteryFuel", dependsOn = "tconstruct")
public class RemoveSmelteryFuel extends BasicOperation {
    /*
     * Serialized properties
     */
    private FluidStack what;

    /*
     * Computed properties
     */
    private transient Map.Entry<FluidStack, Integer> removedFuel;

    @Override
    public void init() throws OperationException {

    }

    @Override
    public void apply() {
        LogHelper.info("Removing Tinker's smeltery fuel using '" + what.getFluid().getName() + "'.");
        removedFuel = ReflectedTinkerRegistry.removeFuel(what);
    }

    @Override
    public void undo() {
        if(removedFuel == null) return;
        ReflectedTinkerRegistry.addFuel(removedFuel.getKey(), removedFuel.getValue());
    }
}
