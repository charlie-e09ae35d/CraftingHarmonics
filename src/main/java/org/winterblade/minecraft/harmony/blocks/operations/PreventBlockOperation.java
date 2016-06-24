package org.winterblade.minecraft.harmony.blocks.operations;

import org.winterblade.minecraft.harmony.api.BasicOperation;
import org.winterblade.minecraft.harmony.api.Operation;
import org.winterblade.minecraft.harmony.api.OperationException;
import org.winterblade.minecraft.harmony.blocks.BlockRegistry;
import org.winterblade.minecraft.harmony.common.blocks.BlockMatcher;

/**
 * Created by Matt on 6/23/2016.
 */
@Operation(name = "preventBlock")
public class PreventBlockOperation extends BasicOperation {
    /*
     * Serialized properties
     */
    BlockMatcher[] what;

    /**
     * Called to initialize the set
     *
     * @throws OperationException If something went wrong
     */
    @Override
    public void init() throws OperationException {
        if(what.length <= 0) throw new OperationException("preventBlock must have at least one block to prevent ('what').");
    }

    /**
     * Called to apply the set (if not player-specific)
     */
    @Override
    public void apply() {
        for (BlockMatcher blockMatcher : what) {
            BlockRegistry.instance.preventPlacement(blockMatcher.getStates());
        }
    }

    /**
     * Called to remove the set (if not player-specific)
     */
    @Override
    public void undo() {
        for (BlockMatcher blockMatcher : what) {
            BlockRegistry.instance.allowPlacement(blockMatcher.getStates());
        }
    }
}
