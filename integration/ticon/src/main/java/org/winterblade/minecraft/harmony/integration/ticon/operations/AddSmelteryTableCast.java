package org.winterblade.minecraft.harmony.integration.ticon.operations;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import org.winterblade.minecraft.harmony.api.BaseRecipeOperation;
import org.winterblade.minecraft.harmony.api.RecipeOperation;
import org.winterblade.minecraft.harmony.crafting.ItemMissingException;
import org.winterblade.minecraft.harmony.crafting.ItemRegistry;
import org.winterblade.minecraft.harmony.integration.ticon.ReflectedTinkerRegistry;
import org.winterblade.minecraft.harmony.utility.LogHelper;
import slimeknights.mantle.util.RecipeMatch;
import slimeknights.tconstruct.library.smeltery.CastingRecipe;

/**
 * Created by Matt on 4/25/2016.
 */
@RecipeOperation(name = "addSmelteryTableCast", dependsOn = "tconstruct")
public class AddSmelteryTableCast extends BaseRecipeOperation {
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
        recipe = new CastingRecipe(what, RecipeMatch.ofNBT(cast), with, consumeCast, switchOutput);
    }

    @Override
    public void Apply() {
        LogHelper.info("Adding Tinker's table cast for '" + ItemRegistry.outputItemName(what) + "'.");
        ReflectedTinkerRegistry.addTableCast(recipe);
    }

    @Override
    public void Undo() {
        ReflectedTinkerRegistry.removeTableCast(recipe);
    }
}
