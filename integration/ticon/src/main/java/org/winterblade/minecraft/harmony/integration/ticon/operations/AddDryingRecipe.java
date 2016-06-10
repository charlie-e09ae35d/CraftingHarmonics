package org.winterblade.minecraft.harmony.integration.ticon.operations;

import net.minecraft.item.ItemStack;
import org.winterblade.minecraft.harmony.api.BasicOperation;
import org.winterblade.minecraft.harmony.api.Operation;
import org.winterblade.minecraft.harmony.api.OperationException;
import org.winterblade.minecraft.harmony.common.ItemUtility;
import org.winterblade.minecraft.harmony.common.utility.LogHelper;
import org.winterblade.minecraft.harmony.integration.ticon.ReflectedTinkerRegistry;
import slimeknights.tconstruct.library.DryingRecipe;

/**
 * Created by Matt on 6/6/2016.
 */
@Operation(name = "TConstruct.addDryingRecipe", dependsOn = "tconstruct")
public class AddDryingRecipe extends BasicOperation {
    /*
     * Serialized properties
     */
    private ItemStack output;
    private ItemStack with;
    private int time;

    /*
     * Computed properties
     */
    private transient DryingRecipe recipe;


    /**
     * Called to initialize the set
     *
     * @throws OperationException If something went wrong
     */
    @Override
    public void init() throws OperationException {
        if(output == null) throw new OperationException("TConstruct.addDryingRecipe must have a valid 'output'.");
        if(with == null) throw new OperationException("TConstruct.addDryingRecipe must have a valid intput ('with').");

        recipe = ReflectedTinkerRegistry.makeDryingRecipe(with, output, time);
    }

    /**
     * Called to apply the set (if not player-specific)
     */
    @Override
    public void apply() {
        LogHelper.info("Adding new Tinker's Construct drying rack recipe to produce '{}'.", ItemUtility.outputItemName(output));
        ReflectedTinkerRegistry.addDryingRecipe(recipe);
    }

    /**
     * Called to remove the set (if not player-specific)
     */
    @Override
    public void undo() {
        ReflectedTinkerRegistry.removeDryingRecipe(recipe);
    }
}
