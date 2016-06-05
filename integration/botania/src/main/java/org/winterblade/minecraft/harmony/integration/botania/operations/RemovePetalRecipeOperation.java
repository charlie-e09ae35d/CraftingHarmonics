package org.winterblade.minecraft.harmony.integration.botania.operations;

import com.google.common.base.Joiner;
import net.minecraft.item.ItemStack;
import org.winterblade.minecraft.harmony.api.BasicOperation;
import org.winterblade.minecraft.harmony.api.Operation;
import org.winterblade.minecraft.harmony.api.OperationException;
import org.winterblade.minecraft.harmony.common.ItemUtility;
import org.winterblade.minecraft.harmony.common.utility.LogHelper;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.recipe.RecipePetals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Matt on 6/5/2016.
 */
@Operation(name = "Botania.removePetalRecipe", dependsOn = "Botania")
public class RemovePetalRecipeOperation extends BasicOperation {
    /*
     * Serialized properties
     */
    private ItemStack what;
    private ItemStack[] with;

    /*
     * Computed properties
     */
    private transient List<RecipePetals> recipes = new ArrayList<>();

    /**
     * Called to initialize the set
     *
     * @throws OperationException If something went wrong
     */
    @Override
    public void init() throws OperationException {
        recipes.clear();

        for(RecipePetals recipe : BotaniaAPI.petalRecipes) {
            if(!ItemUtility.areRecipesEquivalent(what, recipe.getOutput(), with, recipe.getInputs())) continue;
            recipes.add(recipe);
        }
    }

    /**
     * Called to apply the set (if not player-specific)
     */
    @Override
    public void apply() {
        if(what != null) {
            LogHelper.info("Removing Botania petal apothecary recipes producing '{}'.", ItemUtility.outputItemName(what));
        } else if(with != null) {
            LogHelper.info("Removing Botania petal apothecary recipes using {}.",
                    Joiner.on(", ").join(Arrays.stream(with).map(ItemUtility::outputItemName).collect(Collectors.toList())));
        } else {
            LogHelper.info("Removing all Botania petal apothecary recipes.");
        }

        for(RecipePetals recipe : recipes) {
            BotaniaAPI.petalRecipes.remove(recipe);
        }
    }

    /**
     * Called to remove the set (if not player-specific)
     */
    @Override
    public void undo() {
        for(RecipePetals recipe : recipes) {
            BotaniaAPI.petalRecipes.add(recipe);
        }
    }
}
