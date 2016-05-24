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

/**
 * Created by Matt on 5/23/2016.
 */
@Operation(name = "BloodMagic.addAlchemyTable", dependsOn = "BloodMagic")
public class AddAlchemyTableRecipe extends BasicOperation {
    /*
     * Serialized properties
     */
    private ItemStack output;
    private int lpCost;
    private int minTier;
    private int time;
    private RecipeInput[] with;

    /*
     * Computed properties
     */
    private transient AlchemyTableRecipe recipe;

    /**
     * Called to initialize the set
     *
     * @throws OperationException If something went wrong
     */
    @Override
    public void init() throws OperationException {
        if(with == null || with.length <= 0) throw new OperationException("Alchemy table recipes require at least one input.");
        recipe = new AlchemyTableRecipe(output, lpCost, time, minTier, RecipeInput.getFacsimileItems(with));
    }

    /**
     * Called to apply the set (if not player-specific)
     */
    @Override
    public void apply() {
        LogHelper.info("Adding Blood Magic Alchemy Table recipe resulting in '" + ItemUtility.outputItemName(output) + "'.");
        ReflectedBloodMagicRegistry.addAlchemyTableRecipe(recipe);
    }

    /**
     * Called to remove the set (if not player-specific)
     */
    @Override
    public void undo() {
        ReflectedBloodMagicRegistry.removeAlchemyTableRecipe(recipe);
    }
}
