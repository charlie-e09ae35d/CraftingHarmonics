package org.winterblade.minecraft.harmony.integration.roots.operations;

import elucent.roots.component.ComponentManager;
import elucent.roots.component.ComponentRecipe;
import net.minecraft.item.ItemStack;
import org.winterblade.minecraft.harmony.api.BasicOperation;
import org.winterblade.minecraft.harmony.api.Component;
import org.winterblade.minecraft.harmony.api.Operation;
import org.winterblade.minecraft.harmony.api.OperationException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Matt on 7/31/2016.
 */
@Operation(name = "Roots.setReagents", dependsOn = "roots")
public class SetReagentsOperation extends BasicOperation {
    /*
     * Serialized properties
     */
    private String what;
    private ItemStack[] reagents;

    /*
     * Computed properties
     */
    private ComponentRecipe recipe;
    private List<ItemStack> originalMats = new ArrayList<>();

    /**
     * Called to initialize the set
     *
     * @throws OperationException If something went wrong
     */
    @Override
    public void init() throws OperationException {
        recipe = ComponentManager.getRecipe(what);
        if(recipe == null) throw new OperationException("Roots.setReagents cannot find dust matching '" + what + "'.");
        if(reagents == null || reagents.length < 3)
            throw new OperationException("Roots.setReagents for '" + what + "' must have at least 3 valid reagents.");
        originalMats.addAll(recipe.materials);
    }

    /**
     * Called to apply the set (if not player-specific)
     */
    @Override
    public void apply() {
        recipe.materials.clear();
        for (ItemStack reagent : reagents) {
            recipe.addIngredient(reagent);
        }
    }

    /**
     * Called to remove the set (if not player-specific)
     */
    @Override
    public void undo() {
        recipe.materials.clear();
        for (ItemStack reagent : originalMats) {
            recipe.addIngredient(reagent);
        }
    }
}
