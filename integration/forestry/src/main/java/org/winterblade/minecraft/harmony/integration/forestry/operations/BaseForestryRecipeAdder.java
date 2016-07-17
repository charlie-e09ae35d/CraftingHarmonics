package org.winterblade.minecraft.harmony.integration.forestry.operations;

import forestry.api.recipes.ICraftingProvider;
import forestry.api.recipes.IForestryRecipe;
import org.winterblade.minecraft.harmony.api.BasicOperation;
import org.winterblade.minecraft.harmony.api.OperationException;
import org.winterblade.minecraft.harmony.common.utility.LogHelper;

/**
 * Created by Matt on 7/15/2016.
 */
public abstract class BaseForestryRecipeAdder <T extends IForestryRecipe> extends BasicOperation {
    /*
     * Computed properties
     */
    private transient T recipe;
    private final ICraftingProvider<T> provider;
    private final String target;

    public BaseForestryRecipeAdder(ICraftingProvider<T> provider, String target) {
        this.provider = provider;
        this.target = target;
    }

    /**
     * Called to initialize the set
     *
     * @throws OperationException If something went wrong
     */
    @Override
    public void init() throws OperationException {
        recipe = getRecipe();
    }

    /**
     * Called to apply the set (if not player-specific)
     */
    @Override
    public final void apply() {
        LogHelper.info("Adding recipe for {} to the {}", getOutputName(), target);
        provider.addRecipe(recipe);
    }

    /**
     * Called to remove the set (if not player-specific)
     */
    @Override
    public void undo() {
        provider.removeRecipe(recipe);
    }

    /**
     * Returns the name of the output for logging
     * @return The name of the item
     */
    protected abstract String getOutputName();

    /**
     * Get the recipe being added
     * @return  The recipe
     */
    protected abstract T getRecipe() throws OperationException;
}
