package org.winterblade.minecraft.harmony.api.calendar;

import net.minecraft.world.World;
import org.winterblade.minecraft.harmony.api.utility.IDependentProvider;

/**
 * Interface denoting a calendar/season provider; this must be annotated with {@link CalendarProvider}.
 */
public interface ICalendarProvider extends IDependentProvider {

    /**
     * Gets the current season
     * @param world     The world to check
     * @return          The season
     */
    String getSeason(World world);

    /**
     * Gets the current day of the year
     * @param world     The world to check
     * @return          The day of the year, with 1 being the first day of the year.
     */
    int getDayOfYear(World world);


}
