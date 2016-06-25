package org.winterblade.minecraft.harmony.items.operations;

import org.winterblade.minecraft.harmony.api.BasicOperation;
import org.winterblade.minecraft.harmony.api.Operation;
import org.winterblade.minecraft.harmony.api.OperationException;
import org.winterblade.minecraft.harmony.api.crafting.RecipeInput;
import org.winterblade.minecraft.harmony.items.ItemRegistry;

/**
 * Created by Matt on 6/24/2016.
 */
@Operation(name = "preventItem")
public class PreventItemOperation extends BasicOperation {
    /*
     * Serialized properties
     */
    RecipeInput[] what;

    /**
     * Called to initialize the set
     *
     * @throws OperationException If something went wrong
     */
    @Override
    public void init() throws OperationException {
        if(what.length <= 0) throw new OperationException("preventItem must have at least one block to prevent ('what').");
    }

    /**
     * Called to apply the set (if not player-specific)
     */
    @Override
    public void apply() {
        for (RecipeInput matcher : what) {
            ItemRegistry.instance.preventUse(matcher);
        }
    }

    /**
     * Called to remove the set (if not player-specific)
     */
    @Override
    public void undo() {
        for (RecipeInput matcher : what) {
            ItemRegistry.instance.allowUse(matcher);
        }
    }
}
