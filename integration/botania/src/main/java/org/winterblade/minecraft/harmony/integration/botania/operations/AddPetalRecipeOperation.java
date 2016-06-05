package org.winterblade.minecraft.harmony.integration.botania.operations;

import net.minecraft.item.ItemStack;
import org.winterblade.minecraft.harmony.api.BasicOperation;
import org.winterblade.minecraft.harmony.api.Operation;
import org.winterblade.minecraft.harmony.api.OperationException;
import org.winterblade.minecraft.harmony.api.crafting.RecipeInput;
import org.winterblade.minecraft.harmony.common.ItemUtility;
import org.winterblade.minecraft.harmony.common.utility.LogHelper;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.recipe.RecipePetals;

/**
 * Created by Matt on 6/4/2016.
 */
@Operation(name = "Botania.addPetalRecipe", dependsOn = "Botania")
public class AddPetalRecipeOperation extends BasicOperation {
    /*
     * Serialized properties
     */
    private ItemStack output;
    private RecipeInput[] with;

    /*
     * Computed properties
     */
    private transient RecipePetals recipe;

    /**
     * Called to initialize the set
     *
     * @throws OperationException If something went wrong
     */
    @Override
    public void init() throws OperationException {
        if(output == null) throw new OperationException("Botania.addPetalRecipe must have a valid 'output'.");
        if(with == null || with.length <= 0) throw new OperationException("Botania.addPetalRecipe must have at least one valid intput ('with').");
        recipe = new RecipePetals(output, RecipeInput.getFacsimileItems(with));
    }

    /**
     * Called to apply the set (if not player-specific)
     */
    @Override
    public void apply() {
        LogHelper.info("Adding recipe for {} to the Botania petal apothecary.", ItemUtility.outputItemName(output));
        BotaniaAPI.petalRecipes.add(recipe);
    }

    /**
     * Called to remove the set (if not player-specific)
     */
    @Override
    public void undo() {
        BotaniaAPI.petalRecipes.remove(recipe);
    }
}
