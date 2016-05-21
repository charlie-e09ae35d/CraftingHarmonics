package org.winterblade.minecraft.harmony.integration.techreborn.operations;

import com.google.common.base.Joiner;
import net.minecraft.item.ItemStack;
import org.winterblade.minecraft.harmony.api.BasicOperation;
import org.winterblade.minecraft.harmony.api.OperationException;
import org.winterblade.minecraft.harmony.common.utility.LogHelper;
import org.winterblade.minecraft.harmony.integration.techreborn.RebornRecipeUtils;
import reborncore.api.recipe.IBaseRecipeType;

/**
 * Created by Matt on 5/8/2016.
 */
public abstract class BaseTechRebornAddOperation extends BasicOperation {
    /*
     * Serialized properties
     */
    protected ItemStack what[];
    protected int ticks;
    protected int euPerTick;
    protected ItemStack[] with;

    /*
     * Computed properties
     */
    protected transient IBaseRecipeType recipe;

    /*
     * Constructor properties
     */
    private final int minInputs;
    private final int minOutputs;
    private final String recipeType;

    public BaseTechRebornAddOperation(String recipeType) {
        this(recipeType, 1, 1);
    }

    public BaseTechRebornAddOperation(String recipeType, int minInputs) {
        this(recipeType, minInputs, 1);
    }

    public BaseTechRebornAddOperation(String recipeType, int minInputs, int minOutputs) {
        this.recipeType = recipeType;
        this.minInputs = minInputs;
        this.minOutputs = minOutputs;
    }

    @Override
    public final void init() throws OperationException {
        if(what.length < minOutputs) throw new OperationException("TechReborn's " + recipeType + " recipes require at least " + minOutputs + " output(s)");
        if(with.length < minInputs) throw new OperationException("TechReborn's " + recipeType + " recipes require at least " + minOutputs + " input(s)");
        recipe = getRecipe();
    }

    protected abstract IBaseRecipeType getRecipe() throws OperationException;

    @Override
    public void apply() {
        LogHelper.info("Adding TechReborn "  + recipe.getUserFreindlyName() + " recipe for: " + Joiner.on(", ").join(what) + ".");
        RebornRecipeUtils.addRecipe(recipe);
    }

    @Override
    public void undo() {
        RebornRecipeUtils.removeRecipe(recipe);
    }

    /**
     * Gets the input at the given ID, or null if none exists
     * @param id    The ID to check
     * @return      The ItemStack at that position, null if it doesn't exist
     */
    protected ItemStack getInput(int id) {
        return (id < with.length ? with[id] : null);
    }

    /**
     * Gets the output at the given ID, or null if none exists
     * @param id    The ID to check
     * @return      The ItemStack at that position, null if it doesn't exist
     */
    protected ItemStack getOutput(int id) {
        return (id < what.length ? what[id] : null);
    }
}
