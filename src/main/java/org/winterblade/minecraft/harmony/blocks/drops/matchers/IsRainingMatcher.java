package org.winterblade.minecraft.harmony.blocks.drops.matchers;

import net.minecraft.item.ItemStack;
import net.minecraftforge.event.world.BlockEvent;
import org.winterblade.minecraft.harmony.api.BaseMatchResult;
import org.winterblade.minecraft.harmony.api.Component;
import org.winterblade.minecraft.harmony.api.PrioritizedObject;
import org.winterblade.minecraft.harmony.api.Priority;
import org.winterblade.minecraft.harmony.api.blocks.IBlockDropMatcher;
import org.winterblade.minecraft.harmony.drops.matchers.BaseIsRainingMatcher;

/**
 * Created by Matt on 5/23/2016.
 */
@Component(properties = {"isRaining"})
@PrioritizedObject(priority = Priority.HIGHER)
public class IsRainingMatcher extends BaseIsRainingMatcher implements IBlockDropMatcher {
    public IsRainingMatcher(RainMatcher matcher) {
        super(matcher);
    }

    /**
     * Should return true if this matcher matches the given event
     *
     * @param harvestDropsEvent The event to match
     * @param drop              The dropped item; this can be modified.
     * @return True if it should match; false otherwise
     */
    @Override
    public BaseMatchResult isMatch(BlockEvent.HarvestDropsEvent harvestDropsEvent, ItemStack drop) {
        return matches(harvestDropsEvent.getWorld(), harvestDropsEvent.getPos());
    }
}
