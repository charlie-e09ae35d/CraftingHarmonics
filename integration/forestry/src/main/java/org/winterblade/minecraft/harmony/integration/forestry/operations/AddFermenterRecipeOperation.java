package org.winterblade.minecraft.harmony.integration.forestry.operations;

import forestry.api.recipes.IFermenterRecipe;
import forestry.api.recipes.RecipeManagers;
import forestry.factory.recipes.FermenterRecipe;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import org.winterblade.minecraft.harmony.api.Operation;
import org.winterblade.minecraft.harmony.api.OperationException;

/**
 * Created by Matt on 7/21/2016.
 */
@Operation(name = "Forestry.addFermenterRecipe", dependsOn = "forestry")
public class AddFermenterRecipeOperation extends BaseForestryRecipeAdder<IFermenterRecipe> {
    /*
     * Serialized properties
     */
    private FluidStack input;
    private Fluid output;
    private ItemStack catalyst;
    private float multiplier;


    public AddFermenterRecipeOperation() {
        super(RecipeManagers.fermenterManager, "Fermenter");
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
    protected IFermenterRecipe getRecipe() throws OperationException {
        if(output == null) throw new OperationException("addFermenterRecipe must have valid fluid 'output'.");
        if(input == null) throw new OperationException("addFermenterRecipe must have valid fluid 'input'.");
        if(catalyst == null) throw new OperationException("addFermenterRecipe must have valid 'catalyst' item.");

        return new FermenterRecipe(catalyst, input.amount, multiplier, output, input);
    }
}

