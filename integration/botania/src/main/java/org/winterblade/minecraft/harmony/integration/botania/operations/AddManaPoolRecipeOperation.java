package org.winterblade.minecraft.harmony.integration.botania.operations;

import net.minecraft.item.ItemStack;
import org.winterblade.minecraft.harmony.api.BasicOperation;
import org.winterblade.minecraft.harmony.api.Operation;
import org.winterblade.minecraft.harmony.api.OperationException;
import org.winterblade.minecraft.harmony.api.crafting.RecipeInput;
import org.winterblade.minecraft.harmony.common.ItemUtility;
import org.winterblade.minecraft.harmony.common.utility.LogHelper;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.recipe.RecipeManaInfusion;

/**
 * Created by Matt on 6/5/2016.
 */
@Operation(name = "Botania.addManaInfusion", dependsOn = "Botania")
public class AddManaPoolRecipeOperation extends BasicOperation {
    /*
     * Serialized properties
     */
    private ItemStack output;
    private RecipeInput with;
    private int mana;
    private boolean isAlchemy;
    private boolean isConjuration;

    /*
     * Computed properties
     */
    private transient RecipeManaInfusion recipe;

    /**
     * Called to initialize the set
     *
     * @throws OperationException If something went wrong
     */
    @Override
    public void init() throws OperationException {
        if(output == null || with == null) throw new OperationException("Botania mana infusions must have both 'output' and 'with' specified.");

        recipe = new RecipeManaInfusion(output, with.getFacsimileItem(), mana);

        if(isAlchemy) {
            recipe.setCatalyst(RecipeManaInfusion.alchemyState);
        } else if(isConjuration) {
            recipe.setCatalyst(RecipeManaInfusion.conjurationState);
        }
    }

    /**
     * Called to apply the set (if not player-specific)
     */
    @Override
    public void apply() {
        LogHelper.info("Adding a {} recipe to the Botania mana pool to produce '{}'",
                isAlchemy ? "alchemy" : isConjuration ? "conjuration" : "normal",
                ItemUtility.outputItemName(output));
        BotaniaAPI.manaInfusionRecipes.add(recipe);
    }

    /**
     * Called to remove the set (if not player-specific)
     */
    @Override
    public void undo() {
        BotaniaAPI.manaInfusionRecipes.remove(recipe);
    }
}
