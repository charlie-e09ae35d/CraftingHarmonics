package org.winterblade.minecraft.harmony.blocks;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import jdk.nashorn.api.scripting.ScriptUtils;
import org.winterblade.minecraft.harmony.scripting.deserializers.BaseMirroredDeserializer;
import org.winterblade.minecraft.harmony.scripting.deserializers.BlockMatcherDeserializer;
import org.winterblade.minecraft.scripting.api.ScriptObjectDeserializer;

/**
 * Created by Matt on 5/16/2016.
 */
public class BlockCountMatcher {
    private static final BlockMatcherDeserializer BLOCK_MATCHER_DESERIALIZER = new BlockMatcherDeserializer();
    private BlockMatcher[] what;
    private int dist;
    private int min;
    private int max;

    public BlockMatcher[] getWhat() {
        return what;
    }

    public int getDist() {
        return dist;
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }

    @ScriptObjectDeserializer(deserializes = BlockCountMatcher.class)
    public static class Deserializer extends BaseMirroredDeserializer {

        @Override
        protected Object DeserializeMirror(ScriptObjectMirror mirror) {
            BlockCountMatcher matcher = new BlockCountMatcher();

            if(!mirror.containsKey("what") || !mirror.containsKey("dist")) {
                return null;
            }

            // Copy over all our matchers...
            Object[] items = (Object[]) ScriptUtils.convert(mirror.get("what"), Object[].class);
            matcher.what = new BlockMatcher[items.length];

            for (int i = 0; i < items.length; i++) {
                matcher.what[i] = (BlockMatcher) BLOCK_MATCHER_DESERIALIZER.Deserialize(items[i]);
            }

            // Then the rest of our properties...
            matcher.dist = (int) ScriptUtils.convert(mirror.get("dist"), Integer.class);

            matcher.min = mirror.containsKey("min") ? (int) ScriptUtils.convert(mirror.get("min"), Integer.class) : 0;
            matcher.max = mirror.containsKey("max") ? (int) ScriptUtils.convert(mirror.get("max"), Integer.class) : Integer.MAX_VALUE;

            return matcher;
        }
    }
}
