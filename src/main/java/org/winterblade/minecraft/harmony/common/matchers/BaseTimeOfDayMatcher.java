package org.winterblade.minecraft.harmony.common.matchers;

import net.minecraft.world.World;
import org.winterblade.minecraft.harmony.CraftingHarmonicsMod;
import org.winterblade.minecraft.harmony.api.BaseMatchResult;

/**
 * Created by Matt on 5/14/2016.
 */
public abstract class BaseTimeOfDayMatcher {
    private final long min;
    private final long max;

    public BaseTimeOfDayMatcher(long min, long max) {
        this.min = min;
        this.max = max;
    }

    protected BaseMatchResult matches(World world) {
        long time = world.getWorldTime();
        int len = CraftingHarmonicsMod.getConfigManager().getDayTickLength();

        // Convert into something sensible; time in minutes.
        time = ((time % len) + (len / 4))/20;

        // Translate into standard time (with 0 being midnight)
        return min <= time && time <= max
                ? BaseMatchResult.True
                : BaseMatchResult.False;
    }
}
