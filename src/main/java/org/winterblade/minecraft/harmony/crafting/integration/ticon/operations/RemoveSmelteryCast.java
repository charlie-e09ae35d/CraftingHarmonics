package org.winterblade.minecraft.harmony.crafting.integration.ticon.operations;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import org.winterblade.minecraft.harmony.api.BaseRecipeOperation;
import slimeknights.tconstruct.library.smeltery.CastingRecipe;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matt on 4/25/2016.
 */
public abstract class RemoveSmelteryCast extends BaseRecipeOperation {
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
        if(!recipe.getResult().isItemEqualIgnoreDurability(what)) return false;

        // If that's all we needed to check
        if(with == null && cast == null) return true;

        // If we're matching the cast, check that...
        if(cast != null &&
                (recipe.cast.getInputs().size() != 1 || // We have one cast input, and that it matches
                !recipe.cast.getInputs().get(0).isItemEqualIgnoreDurability(cast))) return false;

        // Check our fluids as well...
        if(with != null && !recipe.getFluid().isFluidEqual(with)) return false;

        return true;
    }
}
