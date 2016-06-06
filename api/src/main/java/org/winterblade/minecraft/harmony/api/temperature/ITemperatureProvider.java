package org.winterblade.minecraft.harmony.api.temperature;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Interface denoting a calendar/season provider; this must be annotated with {@link TemperatureProvider}.
 */
public interface ITemperatureProvider {
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
     * Gets the temperature in the given world at the given position
     * @param world    The world to check
     * @param pos      The position to check
     * @return         The temperature, in degrees Celsius.
     */
    int getTemperatureAt(World world, BlockPos pos);


    /**
     * Gets the temperature for the given entity
     * @param entity   The entity to check
     * @return         The entity's temperature, in degrees Celsius.
     */
    int getInternalTemperature(EntityLivingBase entity);
}
