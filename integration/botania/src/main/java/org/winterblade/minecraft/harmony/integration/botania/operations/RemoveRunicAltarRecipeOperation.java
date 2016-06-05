package org.winterblade.minecraft.harmony.integration.botania.operations;

import com.google.common.base.Joiner;
import net.minecraft.item.ItemStack;
import org.winterblade.minecraft.harmony.api.BasicOperation;
import org.winterblade.minecraft.harmony.api.Operation;
import org.winterblade.minecraft.harmony.api.OperationException;
import org.winterblade.minecraft.harmony.common.ItemUtility;
import org.winterblade.minecraft.harmony.common.utility.LogHelper;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.recipe.RecipeRuneAltar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Matt on 6/5/2016.
 */
@Operation(name = "Botania.removeRunicAltar", dependsOn = "Botania")
public class RemoveRunicAltarRecipeOperation extends BasicOperation {
    /*
     * Serialized properties
     */
    private ItemStack what;
    private ItemStack[] with;

    /*
     * Computed properties
     */
    private transient List<RecipeRuneAltar> recipes = new ArrayList<>();

    /**
     * Called to initialize the set
     *
     * @throws OperationException If something went wrong
     */
    @Override
    public void init() throws OperationException {
        recipes.clear();

        for(RecipeRuneAltar recipe : BotaniaAPI.runeAltarRecipes) {
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
            LogHelper.info("Removing Botania runic altar recipes producing '{}'.", ItemUtility.outputItemName(what));
        } else if(with != null) {
            LogHelper.info("Removing Botania runic altar recipes using {}.",
                    Joiner.on(", ").join(Arrays.stream(with).map(ItemUtility::outputItemName).collect(Collectors.toList())));
        } else {
            LogHelper.info("Removing all Botania runic altar recipes.");
        }

        for(RecipeRuneAltar recipe : recipes) {
            BotaniaAPI.runeAltarRecipes.remove(recipe);
        }
    }

    /**
     * Called to remove the set (if not player-specific)
     */
    @Override
    public void undo() {
        for(RecipeRuneAltar recipe : recipes) {
            BotaniaAPI.runeAltarRecipes.add(recipe);
        }
    }
}
