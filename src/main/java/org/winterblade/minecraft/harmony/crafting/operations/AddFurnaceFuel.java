package org.winterblade.minecraft.harmony.crafting.operations;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.winterblade.minecraft.harmony.api.BaseRecipeOperation;
import org.winterblade.minecraft.harmony.api.IRecipeOperation;
import org.winterblade.minecraft.harmony.api.RecipeOperation;
import org.winterblade.minecraft.harmony.crafting.FuelRegistry;
import org.winterblade.minecraft.harmony.crafting.ItemMissingException;

/**
 * Created by Matt on 4/6/2016.
 */
@RecipeOperation(name = "addFurnaceFuel")
public class AddFurnaceFuel extends BaseRecipeOperation {
    private ItemStack what;
    private int burnTime;

    @Override
    public void Init() throws ItemMissingException {
        if (what == null) throw new RuntimeException("Unable to find requested fuel item.");
    }

    @Override
    public void Apply() {
        System.out.println("Registering fuel '" + what.getUnlocalizedName() + "' with burn time '" + burnTime + "'.");
        int curBurnTime = GameRegistry.getFuelValue(what);
        if (curBurnTime > burnTime) {
            System.out.println("Currently '" + what.getUnlocalizedName() + "' is registered at a higher burn time " +
                    "than you've requested; we can't override this at the moment.");
            return;
        }

        FuelRegistry.getInstance().AddFuel(what, burnTime);
    }

    @Override
    public int compareTo(IRecipeOperation o) {
        int baseCompare = super.compareTo(o);
        if(baseCompare != 0) return baseCompare;

        // Keep classes together:
        if(!(o.getClass().equals(getClass())))
            return o.getClass().getSimpleName().compareTo(getClass().getSimpleName());

        // Otherwise, sort on name:
        return what.getUnlocalizedName().compareTo(((AddFurnaceFuel) o).what.getUnlocalizedName());
    }
}
