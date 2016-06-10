package org.winterblade.minecraft.harmony.calendar.matchers;

import net.minecraft.world.World;
import org.winterblade.minecraft.harmony.api.BaseMatchResult;
import org.winterblade.minecraft.harmony.calendar.CalendarRegistry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Created by Matt on 6/6/2016.
 */
public abstract class BaseIsSeasonMatcher {
    private final String targetSeason;
    @Nullable
    private final String provider;

    public BaseIsSeasonMatcher(@Nonnull String targetSeason, @Nullable String provider) {
        this.targetSeason = targetSeason;
        this.provider = provider;
    }

    protected BaseMatchResult matches(World world) {
        return targetSeason.equals(CalendarRegistry.instance.getSeason(world, provider))
                ? BaseMatchResult.True
                : BaseMatchResult.False;
    }
}
