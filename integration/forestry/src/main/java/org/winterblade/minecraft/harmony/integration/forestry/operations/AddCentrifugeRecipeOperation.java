package org.winterblade.minecraft.harmony.integration.forestry.operations;

import com.google.common.base.Joiner;
import forestry.api.recipes.ICentrifugeRecipe;
import forestry.api.recipes.RecipeManagers;
import forestry.factory.recipes.CentrifugeRecipe;
import net.minecraft.item.ItemStack;
import org.winterblade.minecraft.harmony.api.Operation;
import org.winterblade.minecraft.harmony.api.OperationException;
import org.winterblade.minecraft.harmony.common.ItemUtility;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Created by Matt on 7/18/2016.
 */
@Operation(name = "Forestry.addCentrifugeRecipe", dependsOn = "forestry")
public class AddCentrifugeRecipeOperation extends BaseForestryRecipeAdder<ICentrifugeRecipe> {
    /*
     * Serialized properties
     */
    private int processingTime;
    private ItemStack input;
    private CentrifugeOutput[] output;

    public AddCentrifugeRecipeOperation() {
        super(RecipeManagers.centrifugeManager, "Centrifuge");
    }

    /**
     * Returns the name of the output for logging
     *
     * @return The name of the item
     */
    @Override
    protected String getOutputName() {
        return Joiner.on(", ").join(
                Arrays.stream(output)
                        .map(item -> ItemUtility.outputItemName(item.what))
                        .collect(Collectors.toList()));
    }

    /**
     * Get the recipe being added
     *
     * @return The recipe
     */
    @Override
    protected ICentrifugeRecipe getRecipe() throws OperationException {
        if(output == null || output.length <= 0) throw new OperationException("addCentrifugeRecipe must have at least one valid 'output'.");
        if(input == null) throw new OperationException("addCentrifugeRecipe must have a valid 'input'.");

        return new CentrifugeRecipe(processingTime, input,
                Arrays.stream(output).collect(Collectors.toMap(i -> i.what, i -> i.chance)));
    }

    public static class CentrifugeOutput {
        private ItemStack what;
        private float chance;
    }
}
