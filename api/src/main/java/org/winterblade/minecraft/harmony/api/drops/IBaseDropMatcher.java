package org.winterblade.minecraft.harmony.api.drops;

import net.minecraft.item.ItemStack;
import org.winterblade.minecraft.harmony.api.BaseMatchResult;
import org.winterblade.minecraft.harmony.api.entities.IMobMatcher;

/**
 * Created by Matt on 5/10/2016.
 */
public interface IBaseDropMatcher<TEvt> extends IMobMatcher<TEvt, ItemStack> {
    /**
     * Should return true if this matcher matches the given event
     * @param evt    The event to match
     * @param drop   The dropped item; this can be modified.
     * @return       True if it should match; false otherwise
     */
    BaseMatchResult isMatch(TEvt evt, ItemStack drop);
}

