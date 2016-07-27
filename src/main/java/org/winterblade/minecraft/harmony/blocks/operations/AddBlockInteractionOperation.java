package org.winterblade.minecraft.harmony.blocks.operations;

import net.minecraft.block.state.IBlockState;
import org.winterblade.minecraft.harmony.api.BasicOperation;
import org.winterblade.minecraft.harmony.api.Operation;
import org.winterblade.minecraft.harmony.api.OperationException;
import org.winterblade.minecraft.harmony.blocks.BlockInteractionHandler;
import org.winterblade.minecraft.harmony.blocks.BlockRegistry;
import org.winterblade.minecraft.harmony.common.blocks.BlockMatcher;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Matt on 7/12/2016.
 */
@Operation(name = "addBlockInteraction")
public class AddBlockInteractionOperation extends BasicOperation {
    /*
     * Serialized properties
     */
    private BlockMatcher[] what;
    private BlockInteractionHandler[] interactions;

    /*
     * Computed properties
     */
    private final Set<IBlockState> states = new HashSet<>();

    /**
     * Called to initialize the set
     *
     * @throws OperationException If something went wrong
     */
    @Override
    public void init() throws OperationException {
        if(what == null || what.length <= 0) throw new OperationException("'addBlockInteraction' requires at least one block ('what').");
        if(interactions == null || interactions.length <= 0) {
            throw new OperationException("'addBlockInteraction' requires at least one interaction ('interactions').");
        }

        // Add in all of our states:
        states.clear();
        for (BlockMatcher blockMatcher : what) {
            states.addAll(blockMatcher.getStates());
        }
    }

    /**
     * Called to apply the set (if not player-specific)
     */
    @Override
    public void apply() {
        for (IBlockState state : states) {
            BlockRegistry.instance.addInteractionHandler(state, interactions);
        }
    }

    /**
     * Called to remove the set (if not player-specific)
     */
    @Override
    public void undo() {
        for (IBlockState state : states) {
            BlockRegistry.instance.removeInteractionHandlers(state, interactions);
        }
    }
}
