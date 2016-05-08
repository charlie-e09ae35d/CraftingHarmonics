package org.winterblade.minecraft.harmony.crafting.integration.techreborn.operations;

import com.google.common.base.Joiner;
import net.minecraft.item.ItemStack;
import org.winterblade.minecraft.harmony.api.BaseRecipeOperation;
import org.winterblade.minecraft.harmony.crafting.integration.techreborn.RebornRecipeUtils;
import org.winterblade.minecraft.harmony.utility.LogHelper;
import reborncore.api.recipe.IBaseRecipeType;

/**
 * Created by Matt on 5/8/2016.
 */
public abstract class BaseTechRebornAddOperation extends BaseRecipeOperation {
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

    @Override
    public void Apply() {
        LogHelper.info("Adding TechReborn "  + recipe.getUserFreindlyName() + " recipe for: " + Joiner.on(", ").join(what) + ".");
        RebornRecipeUtils.addRecipe(recipe);
    }

    @Override
    public void Undo() {
        RebornRecipeUtils.removeRecipe(recipe);
    }
}
