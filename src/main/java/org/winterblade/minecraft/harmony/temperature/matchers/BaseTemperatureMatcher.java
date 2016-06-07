package org.winterblade.minecraft.harmony.temperature.matchers;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.winterblade.minecraft.harmony.api.BaseMatchResult;
import org.winterblade.minecraft.harmony.temperature.TemperatureRegistry;

import javax.annotation.Nullable;

/**
 * Created by Matt on 6/6/2016.
 */
public abstract class BaseTemperatureMatcher {
    private final double minTemp;
    private final double maxTemp;
    @Nullable
    private final String provider;

    public BaseTemperatureMatcher(double minTemp, double maxTemp, @Nullable String provider) {
        this.minTemp = minTemp;
        this.maxTemp = maxTemp;
        this.provider = provider;
    }

    protected BaseMatchResult matches(World world, BlockPos pos) {
        double temp = TemperatureRegistry.instance.getTemperatureAt(world, pos, provider);
        return minTemp <= temp && temp <= maxTemp ? BaseMatchResult.True : BaseMatchResult.False;
    }
}
