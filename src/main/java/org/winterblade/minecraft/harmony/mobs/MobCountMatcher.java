package org.winterblade.minecraft.harmony.mobs;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import jdk.nashorn.api.scripting.ScriptUtils;
import org.winterblade.minecraft.harmony.scripting.deserializers.BaseMirroredDeserializer;
import org.winterblade.minecraft.scripting.api.ScriptObjectDeserializer;

/**
 * Created by Matt on 5/15/2016.
 */
public class MobCountMatcher {
    private String[] what;
    private int dist;
    private int min;
    private int max;

    public String[] getWhat() {
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

    @ScriptObjectDeserializer(deserializes = MobCountMatcher.class)
    public static class Deserializer extends BaseMirroredDeserializer {

        @Override
        protected Object DeserializeMirror(ScriptObjectMirror mirror) {
            MobCountMatcher matcher = new MobCountMatcher();

            if(!mirror.containsKey("what") || !mirror.containsKey("dist")) {
                return null;
            }

            matcher.what = (String[]) ScriptUtils.convert(mirror.get("what"), String[].class);
            matcher.dist = (int) ScriptUtils.convert(mirror.get("dist"), Integer.class);

            matcher.min = mirror.containsKey("min") ? (int) ScriptUtils.convert(mirror.get("min"), Integer.class) : 0;
            matcher.max = mirror.containsKey("max") ? (int) ScriptUtils.convert(mirror.get("max"), Integer.class) : Integer.MAX_VALUE;

            return matcher;
        }
    }
}
