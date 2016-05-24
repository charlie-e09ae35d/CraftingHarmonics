package org.winterblade.minecraft.harmony.integration.bloodmagic.operations;

import WayofTime.bloodmagic.api.recipe.AlchemyTableRecipe;
import net.minecraft.item.ItemStack;
import org.winterblade.minecraft.harmony.api.BasicOperation;
import org.winterblade.minecraft.harmony.api.Operation;
import org.winterblade.minecraft.harmony.api.OperationException;
import org.winterblade.minecraft.harmony.api.crafting.RecipeInput;
import org.winterblade.minecraft.harmony.common.ItemUtility;
import org.winterblade.minecraft.harmony.common.utility.LogHelper;
import org.winterblade.minecraft.harmony.integration.bloodmagic.ReflectedBloodMagicRegistry;

import java.util.List;

/**
 * Created by Matt on 5/24/2016.
 */
@Operation(name = "BloodMagic.removeAlchemyTable", dependsOn = "BloodMagic")
public class RemoveAlchemyTableRecipe extends BasicOperation {
    /*
     * Serialized properties
     */
    private ItemStack what;
    private RecipeInput[] with;

    /*
     * Computed properties
     */
    private transient List<AlchemyTableRecipe> recipes;


    /**
     * Called to initialize the set
     *
     * @throws OperationException If something went wrong
     */
    @Override
    public void init() throws OperationException {
        recipes = ReflectedBloodMagicRegistry.findMatchingAlchemyTableRecipes(what, RecipeInput.getFacsimileItems(with));
    }

    /**
     * Called to apply the set (if not player-specific)
     */
    @Override
    public void apply() {
        LogHelper.info("Removing Blood Magic Alchemy Table recipes producing '" + ItemUtility.outputItemName(what) + "'.");
        for(AlchemyTableRecipe recipe: recipes) {
            ReflectedBloodMagicRegistry.removeAlchemyTableRecipe(recipe);
        }
    }

    /**
     * Called to remove the set (if not player-specific)
     */
    @Override
    public void undo() {
        for(AlchemyTableRecipe recipe : recipes) {
            ReflectedBloodMagicRegistry.addAlchemyTableRecipe(recipe);
        }
    }
}
