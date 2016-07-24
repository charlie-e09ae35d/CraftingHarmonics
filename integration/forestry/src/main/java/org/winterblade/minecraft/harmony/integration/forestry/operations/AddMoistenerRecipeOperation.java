package org.winterblade.minecraft.harmony.integration.forestry.operations;

import forestry.api.recipes.IMoistenerRecipe;
import forestry.api.recipes.RecipeManagers;
import forestry.factory.recipes.MoistenerRecipe;
import net.minecraft.item.ItemStack;
import org.winterblade.minecraft.harmony.api.Operation;
import org.winterblade.minecraft.harmony.api.OperationException;

/**
 * Created by Matt on 7/21/2016.
 */

@Operation(name = "Forestry.addMoistenerRecipe", dependsOn = "forestry")
public class AddMoistenerRecipeOperation extends BaseForestryRecipeAdder<IMoistenerRecipe> {
    /*
     * Serialized properties
     */
    private ItemStack input;
    private ItemStack output;
    private int time;


    public AddMoistenerRecipeOperation() {
        super(RecipeManagers.moistenerManager, "Moistener");
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
    protected IMoistenerRecipe getRecipe() throws OperationException {
        if(output == null) throw new OperationException("addFermenterRecipe must have valid 'output'.");
        if(input == null) throw new OperationException("addFermenterRecipe must have valid 'input'.");

        return new MoistenerRecipe(input, output, time);
    }
}
