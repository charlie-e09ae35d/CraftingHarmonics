package org.winterblade.minecraft.harmony.crafting.integration.ticon.operations;

import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import org.winterblade.minecraft.harmony.CraftingHarmonicsMod;
import org.winterblade.minecraft.harmony.api.BaseRecipeOperation;
import org.winterblade.minecraft.harmony.api.RecipeOperation;
import org.winterblade.minecraft.harmony.crafting.ItemMissingException;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.smeltery.AlloyRecipe;

import java.util.List;

/**
 * Operation for adding an alloy for the smeltery.
 */
@RecipeOperation(name = "addSmelteryAlloy", dependsOn = "tconstruct")
public class AddSmelteryAlloy extends BaseRecipeOperation {
    /*
     * Serialized properties
     */
    private FluidStack what;
    private FluidStack[] with;

    /*
     * Computed properties
     */
    private AlloyRecipe recipe;

    private static List<AlloyRecipe> alloyRegistry;

    static {
        // Hook directly into this because we can't remove an alloy after it's been added:
        alloyRegistry = ObfuscationReflectionHelper.getPrivateValue(TinkerRegistry.class, null, "alloyRegistry");
    }


    @Override
    public void Init() throws ItemMissingException {
        if(with == null || with.length < 2) {
            CraftingHarmonicsMod.logger.warn("Alloys must contain at least two inputs.");
            throw new ItemMissingException("Alloys must contain at least two inputs.");
        }

        recipe = new AlloyRecipe(what, with);
    }

    @Override
    public void Apply() {
        alloyRegistry.add(recipe);
    }

    @Override
    public void Undo() {
        alloyRegistry.remove(recipe);
    }
}
