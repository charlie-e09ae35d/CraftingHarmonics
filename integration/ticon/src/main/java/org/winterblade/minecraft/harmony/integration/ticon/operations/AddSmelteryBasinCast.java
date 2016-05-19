package org.winterblade.minecraft.harmony.integration.ticon.operations;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import org.winterblade.minecraft.harmony.api.BaseRecipeOperation;
import org.winterblade.minecraft.harmony.api.RecipeOperation;
import org.winterblade.minecraft.harmony.api.ItemMissingException;
import org.winterblade.minecraft.harmony.integration.ticon.ReflectedTinkerRegistry;
import org.winterblade.minecraft.harmony.api.utility.LogHelper;
import slimeknights.mantle.util.RecipeMatch;
import slimeknights.tconstruct.library.smeltery.CastingRecipe;

/**
 * Created by Matt on 4/25/2016.
 */
@RecipeOperation(name = "addSmelteryBasinCast", dependsOn = "tconstruct")
public class AddSmelteryBasinCast extends BaseRecipeOperation {
    /*
     * Serialized Properties
     */
    private ItemStack what;
    private FluidStack with;
    private ItemStack cast;
    private boolean consumeCast;
    private boolean switchOutput;

    /*
     * Computed properties
     */
    private transient CastingRecipe recipe;

    @Override
    public void Init() throws ItemMissingException {
        recipe = new CastingRecipe(what, cast != null ? RecipeMatch.ofNBT(cast) : null, with, consumeCast, switchOutput);
    }

    @Override
    public void Apply() {
        LogHelper.info("Adding Tinker's basin cast for '" + what.toString() + "'.");
        ReflectedTinkerRegistry.addBasinCast(recipe);
    }

    @Override
    public void Undo() {
        ReflectedTinkerRegistry.removeBasinCast(recipe);
    }
}
