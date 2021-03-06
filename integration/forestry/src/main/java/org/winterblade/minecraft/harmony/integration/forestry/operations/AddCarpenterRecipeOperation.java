package org.winterblade.minecraft.harmony.integration.forestry.operations;

import forestry.api.recipes.ICarpenterRecipe;
import forestry.api.recipes.RecipeManagers;
import forestry.core.recipes.ShapedRecipeCustom;
import forestry.factory.recipes.CarpenterRecipe;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import org.winterblade.minecraft.harmony.api.Operation;
import org.winterblade.minecraft.harmony.api.OperationException;
import org.winterblade.minecraft.harmony.api.crafting.RecipeInput;
import org.winterblade.minecraft.harmony.common.ItemUtility;

/**
 * Created by Matt on 7/15/2016.
 */
@Operation(name = "Forestry.addCarpenterRecipe", dependsOn = "forestry")
public class AddCarpenterRecipeOperation extends BaseForestryRecipeAdder<ICarpenterRecipe> {
    /*
     * Serialized properties
     */
    private int time;
    private FluidStack fluid;
    private ItemStack box;
    private ItemStack output;
    private RecipeInput[] input;

    public AddCarpenterRecipeOperation() {
        super(RecipeManagers.carpenterManager, "Carpenter");
    }

    /**
     * Returns the name of the output for logging
     *
     * @return The name of the item
     */
    @Override
    protected String getOutputName() {
        return ItemUtility.outputItemName(output);
    }

    /**
     * Get the recipe being added
     *
     * @return The recipe
     */
    @Override
    protected ICarpenterRecipe getRecipe() throws OperationException {
        if(output == null) throw new OperationException("addCarpenterRecipe must have valid 'output'.");
        if(input == null || input.length <= 0) throw new OperationException("addCarpenterRecipe must have valid 'input' items.");

        return new CarpenterRecipe(time, fluid, box,
                new ShapedRecipeCustom(
                        output,
                        RecipeInput.toInputMap(3, 3, input).toArgs()));
    }
}
