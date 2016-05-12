package org.winterblade.minecraft.harmony.api;

import net.minecraft.item.ItemStack;

/**
 * Created by Matt on 5/10/2016.
 */
public interface IBaseDropMatcher<TEvt> {
    /**
     * Should return true if this matcher matches the given event
     * @param evt    The event to match
     * @param drop   The dropped item; this can be modified.
     * @return       True if it should match; false otherwise
     */
    boolean isMatch(TEvt evt, ItemStack drop);
}
