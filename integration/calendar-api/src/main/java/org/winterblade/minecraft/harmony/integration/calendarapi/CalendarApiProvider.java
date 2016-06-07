package org.winterblade.minecraft.harmony.integration.calendarapi;

import foxie.calendar.Config;
import foxie.calendar.api.CalendarAPI;
import foxie.calendar.api.ISeasonProvider;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.winterblade.minecraft.harmony.api.calendar.CalendarProvider;
import org.winterblade.minecraft.harmony.api.calendar.ICalendarProvider;
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
    public double getTemperatureAt(World world, BlockPos pos) {
        foxie.calendar.api.ICalendarProvider calendarProvider = CalendarAPI.getCalendarInstance(world);
        return kelvinToCelsius(getProviderFor(world).getTemperature(calendarProvider, pos.getX(), pos.getY(), pos.getZ()));
    }

    /**
     * Gets the temperature for the given entity
     *
     * @param entity    The entity to check
     * @return          The entity's temperature, in degrees Celsius.
     */
    @Override
    public double getInternalTemperature(EntityLivingBase entity) {
        return getTemperatureAt(entity.getEntityWorld(), entity.getPosition());
    }

    /**
     * Gets the current season
     *
     * @param world The world to check
     * @return The season
     */
    @Override
    public String getSeason(World world) {
        foxie.calendar.api.ICalendarProvider calendarProvider = CalendarAPI.getCalendarInstance(world);
        return getProviderFor(world).getSeason(calendarProvider).getName();
    }

    /**
     * Gets the current day of the year
     *
     * @param world The world to check
     * @return The day of the year, with 1 being the first day of the year.
     */
    @Override
    public int getDayOfYear(World world) {
        int daysSinceStartOfYear = 0;
        // Figure out what month we're in...
        int month = CalendarAPI.getCalendarInstance(world).getMonth();
        for(int m = 0; m < month; m++) {
            daysSinceStartOfYear += Config.days[m];
        }

        // Now add in our actual progress into the month...
        daysSinceStartOfYear += CalendarAPI.getCalendarInstance(world).getDay();

        return daysSinceStartOfYear;
    }

    /**
     * Gets the {@link ISeasonProvider} for the given world
     * @param world    The world to check
     * @return         The registered {@link ISeasonProvider}
     */
    private ISeasonProvider getProviderFor(World world) {
        return CalendarAPI.getSeasonProvider(world.provider.getDimension());
    }

    /**
     * Converts kelvin to celsius
     * @param kelvin    The input degrees in kelvin
     * @return          The converted temperature
     */
    private double kelvinToCelsius(float kelvin) {
        return (kelvin-273.15);
    }
}
