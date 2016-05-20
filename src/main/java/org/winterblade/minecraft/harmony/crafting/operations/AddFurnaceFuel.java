package org.winterblade.minecraft.harmony.crafting.operations;

import net.minecraftforge.fml.common.registry.GameRegistry;
import org.winterblade.minecraft.harmony.api.BasicOperation;
import org.winterblade.minecraft.harmony.api.IOperation;
import org.winterblade.minecraft.harmony.api.Operation;
import org.winterblade.minecraft.harmony.crafting.FuelRegistry;
import org.winterblade.minecraft.harmony.api.OperationException;
import org.winterblade.minecraft.harmony.api.crafting.components.RecipeComponent;
import org.winterblade.minecraft.harmony.common.utility.LogHelper;

/**
 * Created by Matt on 4/6/2016.
 */
@Operation(name = "addFurnaceFuel")
public class AddFurnaceFuel extends BasicOperation {
    private RecipeComponent what;
    private int burnTime;

    @Override
    public void init() throws OperationException {
        if (what == null) throw new OperationException("Unable to find requested fuel item.");
    }

    @Override
    public void apply() {
        LogHelper.info("Registering fuel '" + what.toString() + "' with burn time '" + burnTime + "'.");
        int curBurnTime = GameRegistry.getFuelValue(what.getItemStack());
        if (curBurnTime > burnTime) {
            LogHelper.warn("Currently '" + what.toString() + "' is registered at a higher burn time " +
                    "than you've requested; we can't override this at the moment.");
            return;
        }

        FuelRegistry.getInstance().AddFuel(what.getItemStack(), burnTime);
    }

    @Override
    public void undo() {
        FuelRegistry.getInstance().RemoveFuel(what.getItemStack());
    }

    @Override
    public int compareTo(IOperation o) {
        int baseCompare = super.compareTo(o);
        if(baseCompare != 0) return baseCompare;

        // Keep classes together:
        if(!(o.getClass().equals(getClass())))
            return o.getClass().getSimpleName().compareTo(getClass().getSimpleName());

        // Otherwise, sort on name:
        return what.toString().compareTo(
                ((AddFurnaceFuel) o).what.toString());
    }
}
