package org.winterblade.minecraft.harmony.entities.effects.matchers.calendar;

import net.minecraft.entity.Entity;
import org.winterblade.minecraft.harmony.api.BaseMatchResult;
import org.winterblade.minecraft.harmony.api.Component;
import org.winterblade.minecraft.harmony.api.PrioritizedObject;
import org.winterblade.minecraft.harmony.api.Priority;
import org.winterblade.minecraft.harmony.api.mobs.effects.IEntityMatcher;
import org.winterblade.minecraft.harmony.api.utility.CallbackMetadata;
import org.winterblade.minecraft.harmony.calendar.matchers.BaseDayOfYearMatcher;

import javax.annotation.Nullable;

/**
 * Created by Matt on 6/6/2016.
 */
@Component(properties = {"calendarIsDayOfYear", "calendarProvider" })
@PrioritizedObject(priority = Priority.MEDIUM)
public class IsDayOfYearMatcher extends BaseDayOfYearMatcher implements IEntityMatcher {
    public IsDayOfYearMatcher(int day) {
        this(day, null);
    }

    public IsDayOfYearMatcher(int day, @Nullable String provider) {
        super(day, day, provider);
    }

    @Override
    public BaseMatchResult isMatch(Entity entity, CallbackMetadata iEntityMatcherData) {
        return matches(entity.getEntityWorld());
    }
}