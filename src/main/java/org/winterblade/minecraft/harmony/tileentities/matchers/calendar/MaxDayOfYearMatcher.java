package org.winterblade.minecraft.harmony.tileentities.matchers.calendar;

import net.minecraft.tileentity.TileEntity;
import org.winterblade.minecraft.harmony.api.BaseMatchResult;
import org.winterblade.minecraft.harmony.api.Component;
import org.winterblade.minecraft.harmony.api.PrioritizedObject;
import org.winterblade.minecraft.harmony.api.Priority;
import org.winterblade.minecraft.harmony.api.tileentities.ITileEntityMatcher;
import org.winterblade.minecraft.harmony.api.tileentities.ITileEntityMatcherData;
import org.winterblade.minecraft.harmony.calendar.matchers.BaseDayOfYearMatcher;

import javax.annotation.Nullable;

/**
 * Created by Matt on 6/6/2016.
 */
@Component(properties = {"calendarMaxDayOfYear", "calendarProvider" })
@PrioritizedObject(priority = Priority.MEDIUM)
public class MaxDayOfYearMatcher extends BaseDayOfYearMatcher implements ITileEntityMatcher {
    public MaxDayOfYearMatcher(int day) {
        this(day, null);
    }

    public MaxDayOfYearMatcher(int day, @Nullable String provider) {
        super(Integer.MIN_VALUE, day, provider);
    }

    @Override
    public BaseMatchResult isMatch(TileEntity tileEntity, ITileEntityMatcherData iTileEntityMatcherData) {
        return matches(tileEntity.getWorld());
    }
}