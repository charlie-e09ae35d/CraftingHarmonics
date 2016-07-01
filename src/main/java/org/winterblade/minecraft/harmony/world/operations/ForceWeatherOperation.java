package org.winterblade.minecraft.harmony.world.operations;

import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.common.DimensionManager;
import org.winterblade.minecraft.harmony.api.BasicOperation;
import org.winterblade.minecraft.harmony.api.Operation;
import org.winterblade.minecraft.harmony.api.OperationException;
import org.winterblade.minecraft.harmony.common.utility.LogHelper;
import org.winterblade.minecraft.harmony.world.ProxiedWorldProvider;

/**
 * Created by Matt on 6/30/2016.
 */
@Operation(name = "forceWeather")
public class ForceWeatherOperation extends BasicOperation {
    /*
     * Serialized properties
     */
    private int dimension;
    private String weather;

    /*
     * Computed properties
     */
    private transient WeatherType weatherType;


    /**
     * Called to initialize the set
     *
     * @throws OperationException If something went wrong
     */
    @Override
    public void init() throws OperationException {
        if(weather == null) throw new OperationException("Unable to determine 'weather' for forceWeather operation.");

        switch (weather.toLowerCase()) {
            case "clear":
                weatherType = WeatherType.CLEAR;
                break;
            case "rain":
            case "snow":
                weatherType = WeatherType.RAIN;
                break;
            case "thunder":
            case "storm":
            case "storming":
                weatherType = WeatherType.STORM;
                break;
            default:
                throw new OperationException("Weather type for forceWeather must be one of 'clear', 'rain', or 'storm'.");
        }
    }

    /**
     * Called to apply the set (if not player-specific)
     */
    @Override
    public void apply() {
        WorldServer world = DimensionManager.getWorld(dimension);
        if(world == null) {
            LogHelper.warn("Unable to get world {} for forceWeather operation.", dimension);
            return;
        }

        WorldInfo worldinfo = world.getWorldInfo();

        LogHelper.info("Updating weather for dimension {} to be constantly {}", dimension, weatherType);
        ProxiedWorldProvider.lockWeatherFor(dimension);
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

    /**
     * Called to remove the set (if not player-specific)
     */
    @Override
    public void undo() {
        WorldServer world = DimensionManager.getWorld(dimension);
        if(world == null) {
            LogHelper.warn("Unable to get world {} for forceWeather operation.", dimension);
            return;
        }

        ProxiedWorldProvider.unlockWeatherFor(dimension);
        WorldInfo worldinfo = world.getWorldInfo();

        worldinfo.setCleanWeatherTime(0);
        worldinfo.setRainTime(0);
        worldinfo.setThunderTime(0);
        worldinfo.setRaining(false);
        worldinfo.setThundering(false);
    }

    private enum WeatherType {
        CLEAR,
        RAIN,
        STORM
    }
}
