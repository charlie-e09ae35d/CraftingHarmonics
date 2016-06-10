package org.winterblade.minecraft.harmony.mobs.sheds.matchers.calendar;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import org.winterblade.minecraft.harmony.api.BaseMatchResult;
import org.winterblade.minecraft.harmony.api.Component;
import org.winterblade.minecraft.harmony.api.PrioritizedObject;
import org.winterblade.minecraft.harmony.api.Priority;
import org.winterblade.minecraft.harmony.api.mobs.sheds.IMobShedMatcher;
import org.winterblade.minecraft.harmony.calendar.matchers.BaseIsSeasonMatcher;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Created by Matt on 6/6/2016.
 */
@Component(properties = {"calendarIsSeason", "calendarProvider" })
@PrioritizedObject(priority = Priority.MEDIUM)
public class IsSeasonMatcher extends BaseIsSeasonMatcher implements IMobShedMatcher {
    public IsSeasonMatcher(@Nonnull String targetSeason) {
        this(targetSeason, null);
    }

    public IsSeasonMatcher(@Nonnull String targetSeason, @Nullable String provider) {
        super(targetSeason, provider);
    }

    /**
     * Should return true if this matcher matches the given event
     *
     * @param entityLivingBase The event to match
     * @param drop             The dropped item; this can be modified.
     * @return True if it should match; false otherwise
     */
    @Override
    public BaseMatchResult isMatch(EntityLivingBase entityLivingBase, ItemStack drop) {
        return matches(entityLivingBase.getEntityWorld());
    }
}
