package org.winterblade.minecraft.harmony.crafting.integration.techreborn.operations;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import org.winterblade.minecraft.harmony.api.BaseRecipeOperation;
import org.winterblade.minecraft.harmony.api.RecipeOperation;
import org.winterblade.minecraft.harmony.crafting.ItemMissingException;
import org.winterblade.minecraft.harmony.crafting.ItemRegistry;
import org.winterblade.minecraft.harmony.crafting.integration.techreborn.RebornRecipeUtils;
import org.winterblade.minecraft.harmony.utility.LogHelper;
import techreborn.api.RollingMachineRecipe;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matt on 5/9/2016.
 */
@RecipeOperation(name = "TechReborn.removeRollingMachine", dependsOn = RebornRecipeUtils.TechRebornModId)
public class RemoveRollingMachineOperation extends BaseRecipeOperation {
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
    public void Init() throws ItemMissingException {
        if(what == null) throw new ItemMissingException("Output item to remove from TechReborn's Rolling Machine was not found.");
        removedRecipes.clear();

        // Simulate an inventory so we can do matching...
        InventoryCrafting inv = ItemRegistry.simulateInventoryOf(with, 3, 3);

        for (IRecipe recipe : RollingMachineRecipe.instance.getRecipeList()) {
            // Figure out if we match...
            if (!what.isItemEqualIgnoreDurability(recipe.getRecipeOutput())) continue;
            if (with != null && with.length > 0 && !recipe.matches(inv, null)) continue;

            // We matched something:
            removedRecipes.add(recipe);
        }
    }

    @Override
    public void Apply() {
        LogHelper.info("Removing recipes from TechReborn's Rolling Machine that produce: " + ItemRegistry.outputItemName(what));
        for(IRecipe recipe : removedRecipes) {
            RollingMachineRecipe.instance.getRecipeList().remove(recipe);
        }
    }

    @Override
    public void Undo() {
        for(IRecipe recipe : removedRecipes) {
            RollingMachineRecipe.instance.getRecipeList().add(recipe);
        }
    }
}