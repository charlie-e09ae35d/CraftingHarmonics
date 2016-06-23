package org.winterblade.minecraft.harmony.blocks;

import net.minecraftforge.event.world.BlockEvent;

/**
 * Created by Matt on 6/22/2016.
 */
public class BlockRegistry {
    private BlockRegistry() {}

    public static final BlockRegistry instance = new BlockRegistry();

    /**
     * Called to determine if a block should be allowed to be placed.
     * @param evt    The event to check
     * @return       True if the block placement should be cancelled, false otherwise.
     */
    public boolean shouldCancelPlace(BlockEvent.PlaceEvent evt) {
        return false;
    }
}
