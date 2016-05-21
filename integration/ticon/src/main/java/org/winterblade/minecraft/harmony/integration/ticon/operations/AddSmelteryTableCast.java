package org.winterblade.minecraft.harmony.integration.ticon.operations;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import org.winterblade.minecraft.harmony.api.BasicOperation;
import org.winterblade.minecraft.harmony.api.Operation;
import org.winterblade.minecraft.harmony.api.OperationException;
import org.winterblade.minecraft.harmony.common.ItemUtility;
import org.winterblade.minecraft.harmony.integration.ticon.ReflectedTinkerRegistry;
import org.winterblade.minecraft.harmony.common.utility.LogHelper;
import slimeknights.mantle.util.RecipeMatch;
import slimeknights.tconstruct.library.smeltery.CastingRecipe;

/**
 * Created by Matt on 4/25/2016.
 */
@Operation(name = "addSmelteryTableCast", dependsOn = "tconstruct")
public class AddSmelteryTableCast extends BasicOperation {
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
    public void init() throws OperationException {
        recipe = new CastingRecipe(what, RecipeMatch.ofNBT(cast), with, consumeCast, switchOutput);
    }

    @Override
    public void apply() {
        LogHelper.info("Adding Tinker's table cast for '" + ItemUtility.outputItemName(what) + "'.");
        ReflectedTinkerRegistry.addTableCast(recipe);
    }

    @Override
    public void undo() {
        ReflectedTinkerRegistry.removeTableCast(recipe);
    }
}
