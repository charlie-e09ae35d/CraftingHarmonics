package org.winterblade.minecraft.harmony.temperature;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.winterblade.minecraft.harmony.api.temperature.ITemperatureProvider;
import org.winterblade.minecraft.harmony.common.utility.LogHelper;
import org.winterblade.minecraft.harmony.utility.TypedProviderRegistry;

import javax.annotation.Nullable;

/**
 * Created by Matt on 6/5/2016.
 */
public class TemperatureRegistry extends TypedProviderRegistry<ITemperatureProvider> {
    private TemperatureRegistry() {}

    public static final TemperatureRegistry instance = new TemperatureRegistry();


    /**
     * Gets the temperature in the given world at the given position
     *
     * @param world     The world to check
     * @param provider  The provider to use; can be null for 'first'
     * @param pos       The position to check
     * @return          The temperature, in degrees Celsius.
     */
    public double getTemperatureAt(World world, BlockPos pos, @Nullable String provider) {
        if(provider != null) {
            if(!providers.containsKey(provider)) {
                LogHelper.warn("There is no registered temperature provider named '{}'.", provider);
                return 0;
            }

            return providers.get(provider).getTemperatureAt(world, pos);
        }

        return !providers.isEmpty() ? providers.entrySet().iterator().next().getValue().getTemperatureAt(world, pos) : 0;
    }

    /**
     * Gets the temperature for the given entity
     *
     * @param entity    The entity to check
     * @param provider  The provider to use; can be null for 'first'
     * @return          The entity's temperature, in degrees Celsius.
     */
    public double getInternalTemperature(EntityLivingBase entity, @Nullable String provider) {
        if(provider != null) {
            if(!providers.containsKey(provider)) {
                LogHelper.warn("There is no registered temperature provider named '{}'.", provider);
                return 0;
            }

            return providers.get(provider).getInternalTemperature(entity);
        }

        return !providers.isEmpty() ? providers.entrySet().iterator().next().getValue().getInternalTemperature(entity) : 0;
    }
}
