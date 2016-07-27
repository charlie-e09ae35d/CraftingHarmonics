package org.winterblade.minecraft.harmony.integration.forestry.operations;

import forestry.api.recipes.IFabricatorRecipe;
import forestry.api.recipes.RecipeManagers;
import forestry.core.recipes.ShapedRecipeCustom;
import forestry.factory.recipes.FabricatorRecipe;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import org.winterblade.minecraft.harmony.api.Operation;
import org.winterblade.minecraft.harmony.api.OperationException;
import org.winterblade.minecraft.harmony.api.crafting.RecipeInput;
import org.winterblade.minecraft.harmony.common.ItemUtility;

/**
 * Created by Matt on 7/20/2016.
 */
@Operation(name = "Forestry.addFabricatorRecipe", dependsOn = "forestry")
public class AddFabricatorRecipeOperation extends BaseForestryRecipeAdder<IFabricatorRecipe> {
    /*
     * Serialized properties
     */
    private FluidStack fluid;
    private ItemStack output;
    private ItemStack plan;
    private RecipeInput[] input;

    public AddFabricatorRecipeOperation() {
        super(RecipeManagers.fabricatorManager, "Fabricator");
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
    protected IFabricatorRecipe getRecipe() throws OperationException {
        if(output == null) throw new OperationException("addFabricatorRecipe must have valid 'output'.");
        if(fluid == null) throw new OperationException("addFabricatorRecipe must have valid 'fluid'.");
        if(input == null || input.length <= 0) throw new OperationException("addFabricatorRecipe must have valid 'input' items.");

        return new FabricatorRecipe(plan, fluid, new ShapedRecipeCustom(
                output,
                RecipeInput.toInputMap(3, 3, input).toArgs()));
    }
}
