package org.winterblade.minecraft.harmony.world;

import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.common.DimensionManager;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Matt on 7/3/2016.
 */
public class WeatherRegistry {
    private static final Map<Integer, WeatherType> weatherLockedDims = new HashMap<>();

    public static void lockWeatherFor(int dimension, WeatherType type) {
        weatherLockedDims.put(dimension, type);
    }

    public static void unlockWeatherFor(int dimension) {
        weatherLockedDims.remove(dimension);

        WorldServer world = DimensionManager.getWorld(dimension);
        WorldInfo worldinfo = world.getWorldInfo();

        worldinfo.setCleanWeatherTime(0);
        worldinfo.setRainTime(0);
        worldinfo.setThunderTime(0);
        worldinfo.setRaining(false);
        worldinfo.setThundering(false);
    }

    public static void updateWeather(World world) {
        // Figure out if we need to update:
        WeatherType weatherType = weatherLockedDims.get(world.provider.getDimension());
        if(weatherType == null) return;

        // Get the world info:
        WorldInfo worldinfo = world.getWorldInfo();

        switch (weatherType) {
            case CLEAR:
                worldinfo.setCleanWeatherTime(Integer.MAX_VALUE);
                worldinfo.setRainTime(0);
                worldinfo.setThunderTime(0);
                worldinfo.setRaining(false);
                worldinfo.setThundering(false);
                break;
            case RAIN:
                worldinfo.setCleanWeatherTime(0);
                worldinfo.setRainTime(Integer.MAX_VALUE);
                worldinfo.setThunderTime(0);
                worldinfo.setRaining(true);
                worldinfo.setThundering(false);
                break;
            case STORM:
                worldinfo.setCleanWeatherTime(0);
                worldinfo.setRainTime(Integer.MAX_VALUE);
                worldinfo.setThunderTime(Integer.MAX_VALUE);
                worldinfo.setRaining(true);
                worldinfo.setThundering(true);
                break;
        }
    }

    public enum WeatherType {
        CLEAR,
        RAIN,
        STORM
    }
}
