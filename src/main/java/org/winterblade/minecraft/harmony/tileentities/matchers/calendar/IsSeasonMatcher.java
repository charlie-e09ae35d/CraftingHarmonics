package org.winterblade.minecraft.harmony.tileentities.matchers.calendar;

import net.minecraft.tileentity.TileEntity;
import org.winterblade.minecraft.harmony.api.BaseMatchResult;
import org.winterblade.minecraft.harmony.api.Component;
import org.winterblade.minecraft.harmony.api.PrioritizedObject;
import org.winterblade.minecraft.harmony.api.Priority;
import org.winterblade.minecraft.harmony.api.tileentities.ITileEntityMatcher;
import org.winterblade.minecraft.harmony.api.tileentities.ITileEntityMatcherData;
import org.winterblade.minecraft.harmony.calendar.matchers.BaseIsSeasonMatcher;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Created by Matt on 6/6/2016.
 */
@Component(properties = {"calendarIsSeason", "calendarProvider" })
@PrioritizedObject(priority = Priority.MEDIUM)
public class IsSeasonMatcher extends BaseIsSeasonMatcher implements ITileEntityMatcher {
    public IsSeasonMatcher(@Nonnull String targetSeason) {
        this(targetSeason, null);
    }

    public IsSeasonMatcher(@Nonnull String targetSeason, @Nullable String provider) {
        super(targetSeason, provider);
    }

    @Override
    public BaseMatchResult isMatch(TileEntity tileEntity, ITileEntityMatcherData iTileEntityMatcherData) {
        return matches(tileEntity.getWorld());
    }
}
