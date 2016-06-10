package org.winterblade.minecraft.harmony.calendar;

import net.minecraft.world.World;
import org.winterblade.minecraft.harmony.api.calendar.ICalendarProvider;
import org.winterblade.minecraft.harmony.common.utility.LogHelper;
import org.winterblade.minecraft.harmony.utility.TypedProviderRegistry;

import javax.annotation.Nullable;

/**
 * Created by Matt on 6/5/2016.
 */
public class CalendarRegistry extends TypedProviderRegistry<ICalendarProvider> {

    public static final CalendarRegistry instance = new CalendarRegistry();

    private CalendarRegistry() {}

    /**
     * Gets the current season
     *
     * @param world         The world to check
     * @param providerName  The provider to use; can be null for 'first'
     * @return The season
     */
    @Nullable
    public String getSeason(World world, @Nullable String providerName) {
        if(providerName != null) {
            if(!providers.containsKey(providerName)) {
                LogHelper.warn("There is no registered calendar provider named '{}'.", providerName);
                return null;
            }

            return providers.get(providerName).getSeason(world);
        }

        return !providers.isEmpty() ? providers.entrySet().iterator().next().getValue().getSeason(world) : null;
    }

    /**
     * Gets the current day of the year
     *
     * @param world         The world to check
     * @param providerName  The provider to use; can be null for 'first'
     * @return The day of the year, with 1 being the first day of the year.
     */
    public int getDayOfYear(World world, @Nullable String providerName) {
        if(providerName != null) {
            if(!providers.containsKey(providerName)) {
                LogHelper.warn("There is no registered calendar provider named '{}'.", providerName);
                return 0;
            }

            return providers.get(providerName).getDayOfYear(world);
        }

        return !providers.isEmpty() ? providers.entrySet().iterator().next().getValue().getDayOfYear(world) : 0;
    }
}
