package org.winterblade.minecraft.harmony.integration.botania.operations;

import net.minecraft.item.ItemStack;
import org.winterblade.minecraft.harmony.api.BasicOperation;
import org.winterblade.minecraft.harmony.api.Operation;
import org.winterblade.minecraft.harmony.api.OperationException;
import org.winterblade.minecraft.harmony.api.crafting.RecipeInput;
import org.winterblade.minecraft.harmony.common.ItemUtility;
import org.winterblade.minecraft.harmony.common.utility.LogHelper;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.recipe.RecipeRuneAltar;

/**
 * Created by Matt on 6/5/2016.
 */
@Operation(name = "Botania.addRunicAltar", dependsOn = "Botania")
public class AddRunicAltarRecipeOperation extends BasicOperation {
    /*
     * Serialized properties
     */
    private ItemStack output;
    private RecipeInput[] with;
    private int mana;

    /*
     * Computed properties
     */
    private transient RecipeRuneAltar recipe;


    /**
     * Called to initialize the set
     *
     * @throws OperationException If something went wrong
     */
    @Override
    public void init() throws OperationException {
        if(output == null) throw new OperationException("Botania.addRunicAltar must have a valid 'output'.");
        if(with == null || with.length <= 0) throw new OperationException("Botania.addRunicAltar must have at least one valid intput ('with').");

        recipe = new RecipeRuneAltar(output, mana, RecipeInput.getFacsimileItems(with));
    }

    /**
     * Called to apply the set (if not player-specific)
     */
    @Override
    public void apply() {
        LogHelper.info("Adding recipe for {} to the Botania runic altar.", ItemUtility.outputItemName(output));
        BotaniaAPI.runeAltarRecipes.add(recipe);
    }

    /**
     * Called to remove the set (if not player-specific)
     */
    @Override
    public void undo() {
        BotaniaAPI.runeAltarRecipes.remove(recipe);
    }
}
