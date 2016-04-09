package org.winterblade.minecraft.harmony.crafting.operations;

import net.minecraftforge.fml.common.registry.GameRegistry;
import org.winterblade.minecraft.harmony.api.BaseRecipeOperation;
import org.winterblade.minecraft.harmony.api.IRecipeOperation;
import org.winterblade.minecraft.harmony.api.RecipeOperation;
import org.winterblade.minecraft.harmony.crafting.FuelRegistry;
import org.winterblade.minecraft.harmony.crafting.ItemMissingException;
import org.winterblade.minecraft.harmony.crafting.components.RecipeComponent;

/**
 * Created by Matt on 4/6/2016.
 */
@RecipeOperation(name = "addFurnaceFuel")
public class AddFurnaceFuel extends BaseRecipeOperation {
    private RecipeComponent what;
    private int burnTime;

    @Override
    public void Init() throws ItemMissingException {
        if (what == null) throw new RuntimeException("Unable to find requested fuel item.");
    }

    @Override
    public void Apply() {
        System.out.println("Registering fuel '" + what.getItemStack().getUnlocalizedName() + "' with burn time '" + burnTime + "'.");
        int curBurnTime = GameRegistry.getFuelValue(what.getItemStack());
        if (curBurnTime > burnTime) {
            System.out.println("Currently '" + what.getItemStack().getUnlocalizedName() + "' is registered at a higher burn time " +
                    "than you've requested; we can't override this at the moment.");
            return;
        }

        FuelRegistry.getInstance().AddFuel(what.getItemStack(), burnTime);
    }

    @Override
    public int compareTo(IRecipeOperation o) {
        int baseCompare = super.compareTo(o);
        if(baseCompare != 0) return baseCompare;

        // Keep classes together:
        if(!(o.getClass().equals(getClass())))
            return o.getClass().getSimpleName().compareTo(getClass().getSimpleName());

        // Otherwise, sort on name:
        return what.getItemStack().getUnlocalizedName().compareTo(
                ((AddFurnaceFuel) o).what.getItemStack().getUnlocalizedName());
    }
}
