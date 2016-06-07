package org.winterblade.minecraft.harmony.api.temperature;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.winterblade.minecraft.harmony.api.utility.IDependentProvider;

/**
 * Interface denoting a calendar/season provider; this must be annotated with {@link TemperatureProvider}.
 */
public interface ITemperatureProvider extends IDependentProvider {
    /**
     * Gets the temperature in the given world at the given position
     * @param world    The world to check
     * @param pos      The position to check
     * @return         The temperature, in degrees Celsius.
     */
    double getTemperatureAt(World world, BlockPos pos);


    /**
     * Gets the temperature for the given entity
     * @param entity   The entity to check
     * @return         The entity's temperature, in degrees Celsius.
     */
    double getInternalTemperature(EntityLivingBase entity);
}
