package org.winterblade.minecraft.harmony.integration.ticon.operations;

import net.minecraftforge.fluids.FluidStack;
import org.winterblade.minecraft.harmony.api.BasicOperation;
import org.winterblade.minecraft.harmony.api.Operation;
import org.winterblade.minecraft.harmony.api.OperationException;
import org.winterblade.minecraft.harmony.integration.ticon.ReflectedTinkerRegistry;
import org.winterblade.minecraft.harmony.common.utility.LogHelper;

/**
 * Created by Matt on 4/24/2016.
 */
@Operation(name = "addSmelteryFuel", dependsOn = "tconstruct")
public class AddSmelteryFuel extends BasicOperation {
    /*
     * Serialized properties
     */
    private FluidStack what;
    private int duration;

    @Override
    public void init() throws OperationException {

    }

    @Override
    public void apply() {
        LogHelper.info("Adding Tinker's smeltery fuel using '" + what.getFluid().getName() + "'.");
        ReflectedTinkerRegistry.addFuel(what, duration);
    }

    @Override
    public void undo() {
        ReflectedTinkerRegistry.removeFuel(what);
    }
}
