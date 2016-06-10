package org.winterblade.minecraft.harmony.calendar.matchers;

import net.minecraft.world.World;
import org.winterblade.minecraft.harmony.api.BaseMatchResult;
import org.winterblade.minecraft.harmony.calendar.CalendarRegistry;

import javax.annotation.Nullable;

/**
 * Created by Matt on 6/6/2016.
 */
public abstract class BaseDayOfYearMatcher {
    private final int minDay;
    private final int maxDay;
    @Nullable
    private final String provider;

    public BaseDayOfYearMatcher(int minDay, int maxDay, @Nullable String provider) {
        this.minDay = minDay;
        this.maxDay = maxDay;
        this.provider = provider;
    }

    protected BaseMatchResult matches(World world) {
        int day = CalendarRegistry.instance.getDayOfYear(world, provider);
        return minDay <= day && day <= maxDay ? BaseMatchResult.True : BaseMatchResult.False;
    }
}
