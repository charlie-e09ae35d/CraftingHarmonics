package org.winterblade.minecraft.harmony.mobs.drops.matchers.calendar;

import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import org.winterblade.minecraft.harmony.api.BaseMatchResult;
import org.winterblade.minecraft.harmony.api.Component;
import org.winterblade.minecraft.harmony.api.PrioritizedObject;
import org.winterblade.minecraft.harmony.api.Priority;
import org.winterblade.minecraft.harmony.api.mobs.drops.IMobDropMatcher;
import org.winterblade.minecraft.harmony.calendar.matchers.BaseDayOfYearMatcher;

import javax.annotation.Nullable;

/**
 * Created by Matt on 6/6/2016.
 */
@Component(properties = {"calendarIsDayOfYear", "calendarProvider" })
@PrioritizedObject(priority = Priority.MEDIUM)
public class IsDayOfYearMatcher extends BaseDayOfYearMatcher implements IMobDropMatcher {
    public IsDayOfYearMatcher(int day) {
        this(day, null);
    }

    public IsDayOfYearMatcher(int day, @Nullable String provider) {
        super(day, day, provider);
    }

    /**
     * Should return true if this matcher matches the given event
     *
     * @param event The event to match
     * @param drop  The dropped item; this can be modified.
     * @return True if it should match; false otherwise
     */
    @Override
    public BaseMatchResult isMatch(LivingDropsEvent event, ItemStack drop) {
        return matches(event.getEntity().getEntityWorld());
    }
}