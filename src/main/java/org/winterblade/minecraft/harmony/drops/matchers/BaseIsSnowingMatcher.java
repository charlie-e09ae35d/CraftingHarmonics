package org.winterblade.minecraft.harmony.drops.matchers;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

/**
 * Created by Matt on 5/23/2016.
 */
public abstract class BaseIsSnowingMatcher extends BaseWeatherMatcher {
    protected BaseIsSnowingMatcher(WeatherMatcher matcher) {
        super(matcher);
    }

    @Override
    protected boolean isOccurring(World inWorld) {
        return inWorld.isRaining();
    }

    @Override
    protected boolean canOccurInBiome(Biome biome) {
        return biome.isSnowyBiome();
    }

    @Override
    protected boolean isOccuringAtPosition(World inWorld, BlockPos atPos) {
        return inWorld.canSnowAt(atPos, true);
    }
}
