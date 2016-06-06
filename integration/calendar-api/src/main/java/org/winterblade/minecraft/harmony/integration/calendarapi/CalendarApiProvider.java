package org.winterblade.minecraft.harmony.integration.calendarapi;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.winterblade.minecraft.harmony.api.calendar.CalendarProvider;
import org.winterblade.minecraft.harmony.api.calendar.ICalendarProvider;
import org.winterblade.minecraft.harmony.api.calendar.Season;
import org.winterblade.minecraft.harmony.api.temperature.ITemperatureProvider;
import org.winterblade.minecraft.harmony.api.temperature.TemperatureProvider;

/**
 * Created by Matt on 6/5/2016.
 */
@CalendarProvider
@TemperatureProvider
public class CalendarApiProvider implements ICalendarProvider, ITemperatureProvider {
    /**
     * Gets the name of this provider.
     *
     * @return The name of the provider, used to reference it in matchers/callbacks.
     */
    @Override
    public String getName() {
        return "CalendarAPI";
    }

    /**
     * Get a list of mod dependencies for this provider
     *
     * @return A string array of mod dependencies.
     */
    @Override
    public String[] getDependencyList() {
        return new String[] {"CalendarAPI"};
    }

    /**
     * Gets the temperature in the given world at the given position
     *
     * @param world The world to check
     * @param pos   The position to check
     * @return The temperature, in degrees Celsius.
     */
    @Override
    public int getTemperatureAt(World world, BlockPos pos) {
        return 0;
    }

    /**
     * Gets the temperature for the given entity
     *
     * @param entity
     * @return The entity's temperature, in degrees Celsius.
     */
    @Override
    public int getInternalTemperature(EntityLivingBase entity) {
        return 0;
    }

    /**
     * Gets the current season
     *
     * @param world The world to check
     * @return The season
     */
    @Override
    public Season getSeason(World world) {
        return null;
    }

    /**
     * Gets the current day of the year
     *
     * @param world The world to check
     * @return The day of the year, with 1 being the first day of the year.
     */
    @Override
    public short getDayOfYear(World world) {
        return 0;
    }
}
