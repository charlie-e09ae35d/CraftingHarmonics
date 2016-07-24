package org.winterblade.minecraft.harmony.integration.forestry.operations;

import forestry.api.recipes.ISqueezerRecipe;
import forestry.api.recipes.RecipeManagers;
import forestry.factory.recipes.SqueezerRecipe;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import org.winterblade.minecraft.harmony.api.Operation;
import org.winterblade.minecraft.harmony.api.OperationException;

/**
 * Created by Matt on 7/22/2016.
 */
@Operation(name = "Forestry.addSqueezerRecipe", dependsOn = "forestry")
public class AddSqueezerRecipeOperation extends BaseForestryRecipeAdder<ISqueezerRecipe> {
    /*
     * Serialized properties
     */
    private ItemStack[] input;
    private ItemStack output;
    private FluidStack fluid;
    private float chance;
    private int time;


    public AddSqueezerRecipeOperation() {
        super(RecipeManagers.squeezerManager, "Squeezer");
    }

    /**
     * Returns the name of the output for logging
     *
     * @return The name of the item
     */
    @Override
    protected String getOutputName() {
        return output.getUnlocalizedName();
    }

    /**
     * Get the recipe being added
     *
     * @return The recipe
     */
    @Override
    protected ISqueezerRecipe getRecipe() throws OperationException {
        if(fluid == null) throw new OperationException("addSqueezerRecipe must have valid 'fluid'.");
        if(input == null) throw new OperationException("addSqueezerRecipe must have valid 'input'.");

        return new SqueezerRecipe(time, input, fluid, output, chance);
    }
}
