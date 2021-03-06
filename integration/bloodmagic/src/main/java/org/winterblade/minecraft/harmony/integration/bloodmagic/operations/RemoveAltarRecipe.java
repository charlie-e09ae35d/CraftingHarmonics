package org.winterblade.minecraft.harmony.integration.bloodmagic.operations;

import WayofTime.bloodmagic.api.registry.AltarRecipeRegistry;
import net.minecraft.item.ItemStack;
import org.winterblade.minecraft.harmony.api.BasicOperation;
import org.winterblade.minecraft.harmony.api.Operation;
import org.winterblade.minecraft.harmony.api.OperationException;
import org.winterblade.minecraft.harmony.common.ItemUtility;
import org.winterblade.minecraft.harmony.integration.bloodmagic.ReflectedBloodMagicRegistry;
import org.winterblade.minecraft.harmony.common.utility.LogHelper;

import java.util.Set;

/**
 * Created by Matt on 4/21/2016.
 */
@Operation(name = "removeBloodAltarRecipe", dependsOn = "BloodMagic")
public class RemoveAltarRecipe extends BasicOperation {
    /*
     * Serialized properties
     */
    private ItemStack what;
    private ItemStack[] with;

    /*
     * Computed properties
     */
    private transient AltarRecipeRegistry.AltarRecipe recipe;

    @Override
    public void init() throws OperationException {
        Set<AltarRecipeRegistry.AltarRecipe> recipes = AltarRecipeRegistry.getRecipes().values();

        for(AltarRecipeRegistry.AltarRecipe r : recipes) {
            if(!matches(r)) continue;

            recipe = r;
            break;
        }
    }

    @Override
    public void apply() {
        if(recipe == null) return;
        LogHelper.info("Removing blood altar recipe for '" + ItemUtility.outputItemName(what) + "'.");
        ReflectedBloodMagicRegistry.removeAltarRecipe(recipe);
    }

    @Override
    public void undo() {
        if(recipe == null) return;
        ReflectedBloodMagicRegistry.addAltarRecipe(recipe);
    }

    /**
     * Check if the given recipe matches
     * @param r    The recipe to check
     * @return     True if it matches
     */
    private boolean matches(AltarRecipeRegistry.AltarRecipe r) {
        if(!r.getOutput().isItemEqualIgnoreDurability(what)) return false;

        // If that's all we need to check:
        if(with == null || with.length <= 0) return true;

        // If our lengths are different, this obviously isn't it
        if(with.length != r.getInput().size()) return false;

        // Go through our list and figure out if everything's the same:
        // (this should always be 1 item in the current version)
        for(ItemStack input : with) {
            boolean matched = false;

            for(ItemStack recipeInput : r.getInput()) {
                if(!recipeInput.isItemEqualIgnoreDurability(input)) continue;

                matched = true;
                break;
            }

            if(!matched) return false;
        }

        return true;
    }
}
