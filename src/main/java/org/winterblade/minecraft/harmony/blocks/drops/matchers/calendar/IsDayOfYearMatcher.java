package org.winterblade.minecraft.harmony.blocks.drops.matchers.calendar;

import net.minecraft.item.ItemStack;
import net.minecraftforge.event.world.BlockEvent;
import org.winterblade.minecraft.harmony.api.BaseMatchResult;
import org.winterblade.minecraft.harmony.api.Component;
import org.winterblade.minecraft.harmony.api.PrioritizedObject;
import org.winterblade.minecraft.harmony.api.Priority;
import org.winterblade.minecraft.harmony.api.blocks.IBlockDropMatcher;
import org.winterblade.minecraft.harmony.calendar.matchers.BaseDayOfYearMatcher;

import javax.annotation.Nullable;

/**
 * Created by Matt on 6/6/2016.
 */
@Component(properties = {"calendarIsDayOfYear", "calendarProvider" })
@PrioritizedObject(priority = Priority.MEDIUM)
public class IsDayOfYearMatcher extends BaseDayOfYearMatcher implements IBlockDropMatcher {
    public IsDayOfYearMatcher(int day) {
        this(day, null);
    }

    public IsDayOfYearMatcher(int day, @Nullable String provider) {
        super(day, day, provider);
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
        return matches(harvestDropsEvent.getWorld());
    }
}