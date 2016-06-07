package org.winterblade.minecraft.harmony.integration.ticon.operations;

import net.minecraft.item.ItemStack;
import org.winterblade.minecraft.harmony.api.BasicOperation;
import org.winterblade.minecraft.harmony.api.Operation;
import org.winterblade.minecraft.harmony.api.OperationException;
import org.winterblade.minecraft.harmony.common.ItemUtility;
import org.winterblade.minecraft.harmony.common.utility.LogHelper;
import org.winterblade.minecraft.harmony.integration.ticon.ReflectedTinkerRegistry;
import slimeknights.tconstruct.library.DryingRecipe;
import slimeknights.tconstruct.library.TinkerRegistry;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matt on 6/6/2016.
 */
@Operation(name = "TConstruct.removeDryingRecipe", dependsOn = "tconstruct")
public class RemoveDryingRecipe extends BasicOperation {
    /*
     * Serialized properties
     */
    private ItemStack what;
    private ItemStack with;

    /*
     * Computed properties
     */
    private final transient List<DryingRecipe> recipes = new ArrayList<>();

    /**
     * Called to initialize the set
     *
     * @throws OperationException If something went wrong
     */
    @Override
    public void init() throws OperationException {
        recipes.clear();

        for(DryingRecipe recipe : TinkerRegistry.getAllDryingRecipes()) {
            if(!ItemUtility.areRecipesEquivalent(what, recipe.output, with, recipe.input.getInputs())) continue;
            recipes.add(recipe);
        }
    }

    /**
     * Called to apply the set (if not player-specific)
     */
    @Override
    public void apply() {
        if(what != null) {
            LogHelper.info("Removing Tinker's Construct drying rack recipes producing '{}'.", ItemUtility.outputItemName(what));
        } else if(with != null) {
            LogHelper.info("Removing Tinker's Construct drying rack recipes using {}.", ItemUtility.outputItemName(with));
        } else {
            LogHelper.info("Removing all Tinker's Construct drying rack recipes.");
        }

        for(DryingRecipe recipe : recipes) {
            ReflectedTinkerRegistry.removeDryingRecipe(recipe);
        }
    }

    /**
     * Called to remove the set (if not player-specific)
     */
    @Override
    public void undo() {
        for(DryingRecipe recipe : recipes) {
            ReflectedTinkerRegistry.addDryingRecipe(recipe);
        }
    }
}
