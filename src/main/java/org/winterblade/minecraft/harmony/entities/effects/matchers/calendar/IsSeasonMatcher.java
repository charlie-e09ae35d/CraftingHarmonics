package org.winterblade.minecraft.harmony.entities.effects.matchers.calendar;

import net.minecraft.entity.Entity;
import org.winterblade.minecraft.harmony.api.BaseMatchResult;
import org.winterblade.minecraft.harmony.api.Component;
import org.winterblade.minecraft.harmony.api.PrioritizedObject;
import org.winterblade.minecraft.harmony.api.Priority;
import org.winterblade.minecraft.harmony.api.mobs.effects.IEntityMatcher;
import org.winterblade.minecraft.harmony.api.utility.CallbackMetadata;
import org.winterblade.minecraft.harmony.calendar.matchers.BaseIsSeasonMatcher;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Created by Matt on 6/6/2016.
 */
@Component(properties = {"calendarIsSeason", "calendarProvider" })
@PrioritizedObject(priority = Priority.MEDIUM)
public class IsSeasonMatcher extends BaseIsSeasonMatcher implements IEntityMatcher {
    public IsSeasonMatcher(@Nonnull String targetSeason) {
        this(targetSeason, null);
    }

    public IsSeasonMatcher(@Nonnull String targetSeason, @Nullable String provider) {
        super(targetSeason, provider);
    }

    @Override
    public BaseMatchResult isMatch(Entity entity, CallbackMetadata iEntityMatcherData) {
        return matches(entity.getEntityWorld());
    }
}
