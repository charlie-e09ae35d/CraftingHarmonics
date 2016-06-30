package org.winterblade.minecraft.harmony.blocks;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import net.minecraft.block.state.IBlockState;
import net.minecraftforge.event.world.BlockEvent;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by Matt on 6/22/2016.
 */
public class BlockRegistry {
    private BlockRegistry() {}

    public static final BlockRegistry instance = new BlockRegistry();

    private final Multiset<IBlockState> preventedBlocks = HashMultiset.create();

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
}
