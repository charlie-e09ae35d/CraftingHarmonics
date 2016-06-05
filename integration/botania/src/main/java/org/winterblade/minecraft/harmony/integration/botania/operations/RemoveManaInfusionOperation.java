package org.winterblade.minecraft.harmony.integration.botania.operations;

import net.minecraft.item.ItemStack;
import org.winterblade.minecraft.harmony.api.BasicOperation;
import org.winterblade.minecraft.harmony.api.Operation;
import org.winterblade.minecraft.harmony.api.OperationException;
import org.winterblade.minecraft.harmony.common.ItemUtility;
import org.winterblade.minecraft.harmony.common.utility.LogHelper;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.recipe.RecipeManaInfusion;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matt on 6/5/2016.
 */
@Operation(name = "Botania.removeManaInfusion", dependsOn = "Botania")
public class RemoveManaInfusionOperation extends BasicOperation {
    /*
     * Serialized properties
     */
    private ItemStack what;
    private ItemStack with;

    /*
     * Computed properties
     */
    private transient List<RecipeManaInfusion> recipes = new ArrayList<>();

    /**
     * Called to initialize the set
     *
     * @throws OperationException If something went wrong
     */
    @Override
    public void init() throws OperationException {
        recipes.clear();

        for(RecipeManaInfusion recipe : BotaniaAPI.manaInfusionRecipes) {
            if(!ItemUtility.areRecipesEquivalent(what, recipe.getOutput(), with, recipe.getInput())) continue;
            recipes.add(recipe);
        }
    }

    /**
     * Called to apply the set (if not player-specific)
     */
    @Override
    public void apply() {
        if(what != null) {
            LogHelper.info("Removing Botania mana pool recipes producing '{}'.", ItemUtility.outputItemName(what));
        } else if(with != null) {
            LogHelper.info("Removing Botania mana pool recipes using {}.", ItemUtility.outputItemName(with));
        } else {
            LogHelper.info("Removing all Botania mana pool recipes.");
        }

        for(RecipeManaInfusion recipe : recipes) {
            BotaniaAPI.manaInfusionRecipes.remove(recipe);
        }
    }

    /**
     * Called to remove the set (if not player-specific)
     */
    @Override
    public void undo() {
        for(RecipeManaInfusion recipe : recipes) {
            BotaniaAPI.manaInfusionRecipes.add(recipe);
        }
    }
}
