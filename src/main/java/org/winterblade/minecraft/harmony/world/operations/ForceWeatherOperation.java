package org.winterblade.minecraft.harmony.world.operations;

import org.winterblade.minecraft.harmony.api.BasicOperation;
import org.winterblade.minecraft.harmony.api.Operation;
import org.winterblade.minecraft.harmony.api.OperationException;
import org.winterblade.minecraft.harmony.common.utility.LogHelper;
import org.winterblade.minecraft.harmony.world.WeatherRegistry;

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
    private transient WeatherRegistry.WeatherType weatherType;


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
                weatherType = WeatherRegistry.WeatherType.CLEAR;
                break;
            case "rain":
            case "snow":
                weatherType = WeatherRegistry.WeatherType.RAIN;
                break;
            case "thunder":
            case "storm":
            case "storming":
                weatherType = WeatherRegistry.WeatherType.STORM;
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
        LogHelper.info("Updating weather for dimension {} to be constantly {}", dimension, weatherType);
        WeatherRegistry.lockWeatherFor(dimension, weatherType);
    }

    /**
     * Called to remove the set (if not player-specific)
     */
    @Override
    public void undo() {
        WeatherRegistry.unlockWeatherFor(dimension);
    }

}
