package org.winterblade.minecraft.harmony.integration.ticon.operations;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import org.winterblade.minecraft.harmony.api.BasicOperation;
import slimeknights.tconstruct.library.smeltery.CastingRecipe;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matt on 4/25/2016.
 */
public abstract class RemoveSmelteryCast extends BasicOperation {
    /*
     * Serialized Properties
     */
    protected ItemStack what;
    private FluidStack with;
    private ItemStack cast;

    /*
     * Computed properties
     */
    protected transient List<CastingRecipe> recipes = new ArrayList<>();

    protected boolean matches(CastingRecipe recipe) {
        // Be a little over-paranoid with this:
        if(what == null || recipe.getResult() == null
                || !recipe.getResult().isItemEqualIgnoreDurability(what)) return false;

        // If that's all we needed to check
        if(with == null && cast == null) return true;

        // If we're matching the cast, check that...
        if(cast != null &&
                (recipe.cast.getInputs().size() != 1 || // We have one cast input, and that it matches
                !recipe.cast.getInputs().get(0).isItemEqualIgnoreDurability(cast))) return false;

        // Check our fluids as well...
        return !(with != null && !recipe.getFluid().isFluidEqual(with));

    }
}
