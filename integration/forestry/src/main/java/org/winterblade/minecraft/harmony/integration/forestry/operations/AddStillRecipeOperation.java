package org.winterblade.minecraft.harmony.integration.forestry.operations;

import forestry.api.recipes.IFermenterRecipe;
import forestry.api.recipes.IStillRecipe;
import forestry.api.recipes.RecipeManagers;
import forestry.factory.recipes.FermenterRecipe;
import forestry.factory.recipes.StillRecipe;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import org.winterblade.minecraft.harmony.api.Operation;
import org.winterblade.minecraft.harmony.api.OperationException;

/**
 * Created by Matt on 7/23/2016.
 */
@Operation(name = "Forestry.addStillRecipe", dependsOn = "forestry")
public class AddStillRecipeOperation extends BaseForestryRecipeAdder<IStillRecipe> {
    /*
     * Serialized properties
     */
    private FluidStack input;
    private FluidStack output;
    private int time;


    public AddStillRecipeOperation() {
        super(RecipeManagers.stillManager, "Still");
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
    protected IStillRecipe getRecipe() throws OperationException {
        if(output == null) throw new OperationException("addStillRecipe must have valid fluid 'output'.");
        if(input == null) throw new OperationException("addStillRecipe must have valid fluid 'input'.");

        return new StillRecipe(time, input, output);
    }
}
