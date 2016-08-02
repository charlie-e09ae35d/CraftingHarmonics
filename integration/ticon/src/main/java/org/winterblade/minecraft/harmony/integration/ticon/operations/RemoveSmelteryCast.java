package org.winterblade.minecraft.harmony.integration.ticon.operations;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import org.winterblade.minecraft.harmony.api.BasicOperation;
import org.winterblade.minecraft.harmony.common.ItemUtility;
import slimeknights.tconstruct.library.smeltery.BucketCastingRecipe;
import slimeknights.tconstruct.library.smeltery.CastingRecipe;
import slimeknights.tconstruct.library.smeltery.ICastingRecipe;
import slimeknights.tconstruct.library.smeltery.OreCastingRecipe;

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
    protected transient List<ICastingRecipe> recipes = new ArrayList<>();

    protected boolean matches(ICastingRecipe recipe) {
        // If we're matching the cast, check that...
        if(CastingRecipe.class.isAssignableFrom(recipe.getClass())) {
            CastingRecipe castingRecipe = (CastingRecipe)recipe;

            // Be a little over-paranoid with this:
            ItemStack result = castingRecipe.getResult();
            if(what == null || result == null || !result.isItemEqualIgnoreDurability(what)) return false;

            // If that's all we needed to check
            if(with == null && cast == null) return true;

            if (cast != null &&
                    (castingRecipe.cast.getInputs().size() != 1 || // We have one cast input, and that it matches
                            !castingRecipe.cast.getInputs().get(0).isItemEqualIgnoreDurability(cast))) return false;

            // Check our fluids as well...
            return !(with != null && !castingRecipe.getFluid().isFluidEqual(with));
        }
        else if(BucketCastingRecipe.class.isAssignableFrom(recipe.getClass())) {
            // Make sure we're checking a bucket...
            if(cast != null && !ItemUtility.areItemsEquivalent(cast.getItem(), Items.BUCKET)) return false;

            // If we have no fluid to match, return true here...
            if(with == null) return true;

            // Otherwise, match on everything
            return recipe.matches(new ItemStack(Items.BUCKET), with.getFluid());
        }

        // If we made it here... give up all hope:
        return false;
    }
}
