package org.winterblade.minecraft.harmony.integration.ticon.operations;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import org.winterblade.minecraft.harmony.api.BasicOperation;
import org.winterblade.minecraft.harmony.api.OperationException;
import org.winterblade.minecraft.harmony.api.Operation;
import org.winterblade.minecraft.harmony.common.ItemUtility;
import org.winterblade.minecraft.harmony.common.utility.LogHelper;
import org.winterblade.minecraft.harmony.integration.ticon.ReflectedTinkerRegistry;
import slimeknights.mantle.util.RecipeMatch;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.smeltery.MeltingRecipe;

/**
 * Operation for adding and removing a material from the smeltery.
 */
@Operation(name = "addSmelteryMelt", dependsOn = "tconstruct")
public class AddSmelteryMaterial extends BasicOperation {
    /*
     * Serialized properties
     */
    private ItemStack with;
    private String what;
    private int amount;

    /*
     * Computed properties
     */
    private transient MeltingRecipe recipe;


    @Override
    public void init() throws OperationException {
        if(!FluidRegistry.isFluidRegistered(what)) {
            LogHelper.warn(what + " is not a valid fluid.");
            throw new OperationException(what + " is not a valid fluid.");
        }

        recipe = new MeltingRecipe(new RecipeMatch.Item(with, with.stackSize, amount), FluidRegistry.getFluid(what));
    }

    @Override
    public void apply() {
        LogHelper.info("Adding Tinker's smeltery melt to turn '"
                + ItemUtility.outputItemName(with) + "' into '" + what + "'.");
        TinkerRegistry.registerMelting(recipe);
    }

    @Override
    public void undo() {
        ReflectedTinkerRegistry.removeMeltingRegistry(recipe);
    }
}
