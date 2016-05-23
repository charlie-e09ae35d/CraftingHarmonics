package org.winterblade.minecraft.harmony.drops.matchers;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import jdk.nashorn.api.scripting.ScriptUtils;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.winterblade.minecraft.harmony.api.BaseMatchResult;
import org.winterblade.minecraft.harmony.scripting.deserializers.BaseMirroredDeserializer;
import org.winterblade.minecraft.scripting.api.ScriptObjectDeserializer;

/**
 * Created by Matt on 5/23/2016.
 */
public abstract class BaseIsRainingMatcher {
    private final boolean whenRaining;
    private final boolean onlyInBiome;
    private final boolean onlyOnTarget;

    protected BaseIsRainingMatcher(RainMatcher matcher) {
        this.whenRaining = matcher.isWhenRaining();
        this.onlyInBiome = matcher.isOnlyInBiome();
        this.onlyOnTarget = matcher.isOnlyOnTarget();
    }

    protected BaseMatchResult matches(World world, BlockPos pos) {
        // If the rain state doesn't match what we're expecting...
        if(world.isRaining() != whenRaining) return BaseMatchResult.False;

        // If we're not looking for a correct biome or block
        if(!onlyOnTarget && !onlyInBiome) return BaseMatchResult.True;

        // If we're checking the block itself, we automatically check if it's the correct biome...
        if(onlyOnTarget) {
            return world.isRainingAt(pos) == whenRaining ? BaseMatchResult.True : BaseMatchResult.False;
        }

        // Otherwise, we're just checking the biome...
        return world.getBiomeGenForCoords(pos).canRain() == whenRaining ? BaseMatchResult.True : BaseMatchResult.False;
    }

    protected static class RainMatcher {
        private boolean whenRaining;
        private boolean onlyInBiome;
        private boolean onlyOnTarget;

        public boolean isWhenRaining() {
            return whenRaining;
        }

        public boolean isOnlyInBiome() {
            return onlyInBiome;
        }

        public boolean isOnlyOnTarget() {
            return onlyOnTarget;
        }

        @ScriptObjectDeserializer(deserializes = RainMatcher.class)
        public static class Deserializer extends BaseMirroredDeserializer {

            @Override
            protected Object DeserializeMirror(ScriptObjectMirror mirror) {
                RainMatcher output = new RainMatcher();

                output.whenRaining = mirror.containsKey("whenRaining") ? (Boolean)ScriptUtils.convert(mirror.get("whenRaining"), Boolean.class) : true;
                output.onlyOnTarget = mirror.containsKey("onlyOnTarget") ? (Boolean)ScriptUtils.convert(mirror.get("onlyOnTarget"), Boolean.class) : false;

                // This is forced to true when we're checking the block.
                output.onlyInBiome = output.onlyOnTarget
                        || (mirror.containsKey("onlyInBiome") ? (Boolean) ScriptUtils.convert(mirror.get("onlyInBiome"), Boolean.class) : false);

                return output;
            }
        }
    }
}
