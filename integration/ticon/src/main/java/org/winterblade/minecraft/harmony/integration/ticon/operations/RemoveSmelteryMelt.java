package org.winterblade.minecraft.harmony.integration.ticon.operations;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import org.winterblade.minecraft.harmony.api.BasicOperation;
import org.winterblade.minecraft.harmony.api.Operation;
import org.winterblade.minecraft.harmony.common.utility.LogHelper;
import org.winterblade.minecraft.harmony.api.OperationException;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.smeltery.MeltingRecipe;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Matt on 4/21/2016.
 */
@Operation(name = "removeSmelteryMelt", dependsOn = "tconstruct")
public class RemoveSmelteryMelt extends BasicOperation {
    /*
     * Serialized properties
     */
    private ItemStack with;
    private FluidStack what;

    /*
     * Computed properties
     */
    private transient List<MeltingRecipe> recipes = new ArrayList<>();

    @Override
    public void init() throws OperationException {

    }

    @Override
    public void apply() {
        recipes.clear();

        LogHelper.info("Removing '" + what.getFluid().getName() + "' melts from the smeltery.");
        List<MeltingRecipe> meltingRecipies = TinkerRegistry.getAllMeltingRecipies();

        for(Iterator<MeltingRecipe> recipeIterator = meltingRecipies.iterator(); recipeIterator.hasNext(); ) {
            MeltingRecipe recipe = recipeIterator.next();

            if(!matches(recipe)) continue;

            recipeIterator.remove();
            recipes.add(recipe);
        }
    }

    @Override
    public void undo() {
        for(MeltingRecipe recipe : recipes) {
            TinkerRegistry.registerMelting(recipe);
        }
    }

    /**
     * Check if the given recipe matches this operation
     * @param recipe    The recipe to check
     * @return          True if the recipe matches
     */
    private boolean matches(MeltingRecipe recipe) {
        if(!recipe.getResult().isFluidEqual(what)) return false;

        // If that's all we're checking
        if(with == null) return true;

        return recipe.matches(with);
    }
}
