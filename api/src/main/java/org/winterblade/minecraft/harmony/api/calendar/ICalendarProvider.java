package org.winterblade.minecraft.harmony.api.calendar;

import net.minecraft.world.World;

/**
 * Interface denoting a calendar/season provider; this must be annotated with {@link CalendarProvider}.
 */
public interface ICalendarProvider {
    /**
     * Gets the name of this provider.
     * @return  The name of the provider, used to reference it in matchers/callbacks.
     */
    String getName();

    /**
     * Get a list of mod dependencies for this provider
     * @return  A string array of mod dependencies.
     */
    String[] getDependencyList();

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
