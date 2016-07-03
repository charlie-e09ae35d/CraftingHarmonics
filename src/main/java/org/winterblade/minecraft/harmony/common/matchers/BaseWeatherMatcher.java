package org.winterblade.minecraft.harmony.common.matchers;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import jdk.nashorn.api.scripting.ScriptUtils;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import org.winterblade.minecraft.harmony.api.BaseMatchResult;
import org.winterblade.minecraft.harmony.scripting.deserializers.BaseMirroredDeserializer;
import org.winterblade.minecraft.scripting.api.ScriptObjectDeserializer;

/**
 * Created by Matt on 5/23/2016.
 */
public abstract class BaseWeatherMatcher {
    private final boolean presence;
    private final boolean onlyInBiome;
    private final boolean onlyOnTarget;

    BaseWeatherMatcher(WeatherMatcher matcher) {
        this.onlyInBiome = matcher.isOnlyInBiome();
        this.presence = matcher.isPresence();
        this.onlyOnTarget = matcher.isOnlyOnTarget();
    }

    protected BaseMatchResult matches(World world, BlockPos pos) {
        // If the rain state doesn't match what we're expecting...
        if(isOccurring(world) != presence) return BaseMatchResult.False;

        // If we're not looking for a correct biome or block
        if(!onlyOnTarget && !onlyInBiome) return BaseMatchResult.True;

        // If we're checking the block itself, we automatically check if it's the correct biome...
        if(onlyOnTarget) {
            return isOccuringAtPosition(world, pos) == presence ? BaseMatchResult.True : BaseMatchResult.False;
        }

        // Otherwise, we're just checking the biome...
        return canOccurInBiome(world.getBiome(pos)) == presence ? BaseMatchResult.True : BaseMatchResult.False;
    }

    protected abstract boolean isOccurring(World inWorld);

    protected abstract boolean canOccurInBiome(Biome biome);

    protected abstract boolean isOccuringAtPosition(World inWorld, BlockPos atPos);

    protected static class WeatherMatcher {
        private boolean presence;
        private boolean onlyInBiome;
        private boolean onlyOnTarget;

        public boolean isPresence() {
            return presence;
        }

        public boolean isOnlyInBiome() {
            return onlyInBiome;
        }

        public boolean isOnlyOnTarget() {
            return onlyOnTarget;
        }

        @ScriptObjectDeserializer(deserializes = WeatherMatcher.class)
        public static class Deserializer extends BaseMirroredDeserializer {

            @Override
            protected Object DeserializeMirror(ScriptObjectMirror mirror) {
                WeatherMatcher output = new WeatherMatcher();

                output.presence = mirror.containsKey("presence") ? (Boolean) ScriptUtils.convert(mirror.get("presence"), Boolean.class) : true;
                output.onlyOnTarget = mirror.containsKey("onlyOnTarget") ? (Boolean)ScriptUtils.convert(mirror.get("onlyOnTarget"), Boolean.class) : false;

                // This is forced to true when we're checking the block.
                output.onlyInBiome = output.onlyOnTarget
                        || (mirror.containsKey("onlyInBiome") ? (Boolean) ScriptUtils.convert(mirror.get("onlyInBiome"), Boolean.class) : false);

                return output;
            }
        }
    }
}
