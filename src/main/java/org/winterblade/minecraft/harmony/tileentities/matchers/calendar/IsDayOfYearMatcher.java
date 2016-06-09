package org.winterblade.minecraft.harmony.tileentities.matchers.calendar;

import net.minecraft.tileentity.TileEntity;
import org.winterblade.minecraft.harmony.api.BaseMatchResult;
import org.winterblade.minecraft.harmony.api.Component;
import org.winterblade.minecraft.harmony.api.PrioritizedObject;
import org.winterblade.minecraft.harmony.api.Priority;
import org.winterblade.minecraft.harmony.api.tileentities.ITileEntityMatcher;
import org.winterblade.minecraft.harmony.api.utility.CallbackMetadata;
import org.winterblade.minecraft.harmony.calendar.matchers.BaseDayOfYearMatcher;

import javax.annotation.Nullable;

/**
 * Created by Matt on 6/6/2016.
 */
@Component(properties = {"calendarIsDayOfYear", "calendarProvider" })
@PrioritizedObject(priority = Priority.MEDIUM)
public class IsDayOfYearMatcher extends BaseDayOfYearMatcher implements ITileEntityMatcher {
    public IsDayOfYearMatcher(int day) {
        this(day, null);
    }

    public IsDayOfYearMatcher(int day, @Nullable String provider) {
        super(day, day, provider);
    }

    @Override
    public BaseMatchResult isMatch(TileEntity tileEntity, CallbackMetadata metadata) {
        return matches(tileEntity.getWorld());
    }
}