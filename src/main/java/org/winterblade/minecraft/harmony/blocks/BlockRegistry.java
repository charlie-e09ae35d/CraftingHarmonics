package org.winterblade.minecraft.harmony.blocks;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multiset;
import net.minecraft.block.state.IBlockState;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;

import javax.annotation.Nonnull;
import java.util.*;

/**
 * Created by Matt on 6/22/2016.
 */
public class BlockRegistry {
    private BlockRegistry() {}

    public static final BlockRegistry instance = new BlockRegistry();

    private final Multiset<IBlockState> preventedBlocks = HashMultiset.create();
    private final Multimap<IBlockState, BlockInteractionHandler> interactionHandlers = HashMultimap.create();

    /**
     * Called to determine if a block should be allowed to be placed.
     * @param evt    The event to check
     * @return       True if the block placement should be cancelled, false otherwise.
     */
    public boolean shouldCancelPlace(@Nonnull BlockEvent.PlaceEvent evt) {
        return preventedBlocks.contains(evt.getPlacedBlock());
    }

    /**
     * Adds the given states to the list of blocks to prevent placing.
     * @param states    The states to prevent.
     */
    public void preventPlacement(@Nonnull Set<IBlockState> states) {
        preventedBlocks.addAll(states);
    }

    /**
     * Removes the given states from the list of blocks to prevent placing
     * @param states    The states to allow again.
     */
    public void allowPlacement(@Nonnull Set<IBlockState> states) {
        preventedBlocks.removeAll(states);
    }

    /**
     * Handles checking for any interactions we need to run for the given block.
     * @param evt    The event to handle
     * @return       True if the interaction should continue, false otherwise.
     */
    public boolean handleInteraction(PlayerInteractEvent.RightClickBlock evt) {
        // Figure out what we clicked...
        IBlockState targetBlock = evt.getWorld().getBlockState(evt.getPos());

        // Get our handlers...
        Collection<BlockInteractionHandler> handlers = interactionHandlers.get(targetBlock);

        // ... and process them
        for (BlockInteractionHandler handler : handlers) {
            // Check if we should cancel:
            if(!handler.handle(evt)) return false;
        }

        // If we made it here, we can tell the parent handler it can continue:
        return true;
    }

    public static class BlockInteractionHandler {

        public boolean handle(PlayerInteractEvent.RightClickBlock evt) {
            return true;
        }
    }
}
