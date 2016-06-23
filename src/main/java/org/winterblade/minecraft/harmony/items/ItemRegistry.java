package org.winterblade.minecraft.harmony.items;

import net.minecraftforge.event.entity.player.PlayerInteractEvent;

/**
 * Created by Matt on 6/22/2016.
 */
public class ItemRegistry {
    private ItemRegistry() {}

    public static final ItemRegistry instance = new ItemRegistry();

    /**
     * Called to determine if an item should be allowed to be used
     * @param evt    The event to check
     * @return       True if it should be cancelled, false otherwise.
     */
    public boolean shouldCancelUse(PlayerInteractEvent evt) {
        return false;
    }
}
