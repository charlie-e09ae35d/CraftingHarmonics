package org.winterblade.minecraft.harmony.integration.techreborn.operations;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import org.winterblade.minecraft.harmony.api.BasicOperation;
import org.winterblade.minecraft.harmony.api.Operation;
import org.winterblade.minecraft.harmony.api.OperationException;
import org.winterblade.minecraft.harmony.common.ItemUtility;
import org.winterblade.minecraft.harmony.integration.techreborn.RebornRecipeUtils;
import org.winterblade.minecraft.harmony.common.utility.LogHelper;
import techreborn.api.RollingMachineRecipe;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matt on 5/9/2016.
 */
@Operation(name = "TechReborn.removeRollingMachine", dependsOn = RebornRecipeUtils.TechRebornModId)
public class RemoveRollingMachineOperation extends BasicOperation {
    /**
     * Serialized properties
     */
    private ItemStack what;
    private ItemStack[] with;

    /*
     * Computed properties
     */
    private transient IRecipe recipe;
    private final List<IRecipe> removedRecipes = new ArrayList<>();

    @Override
    public void init() throws OperationException {
        if(what == null) throw new OperationException("Output item to remove from TechReborn's Rolling Machine was not found.");
        removedRecipes.clear();

        // Simulate an inventory so we can do matching...
        InventoryCrafting inv = ItemUtility.simulateInventoryOf(with, 3, 3);

        for (IRecipe recipe : RollingMachineRecipe.instance.getRecipeList()) {
            // Figure out if we match...
            if (!what.isItemEqualIgnoreDurability(recipe.getRecipeOutput())) continue;
            if (with != null && with.length > 0 && !recipe.matches(inv, null)) continue;

            // We matched something:
            removedRecipes.add(recipe);
        }
    }

    @Override
    public void apply() {
        LogHelper.info("Removing recipes from TechReborn's Rolling Machine that produce: " + ItemUtility.outputItemName(what));
        for(IRecipe recipe : removedRecipes) {
            RollingMachineRecipe.instance.getRecipeList().remove(recipe);
        }
    }

    @Override
    public void undo() {
        for(IRecipe recipe : removedRecipes) {
            RollingMachineRecipe.instance.getRecipeList().add(recipe);
        }
    }
}
