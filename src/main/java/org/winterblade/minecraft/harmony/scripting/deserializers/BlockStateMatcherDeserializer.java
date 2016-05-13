package org.winterblade.minecraft.harmony.scripting.deserializers;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import jdk.nashorn.api.scripting.ScriptUtils;
import jdk.nashorn.internal.runtime.ScriptObject;
import org.winterblade.minecraft.harmony.blocks.BlockStateMatcher;
import org.winterblade.minecraft.scripting.api.IScriptObjectDeserializer;
import org.winterblade.minecraft.scripting.api.ScriptObjectDeserializer;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Matt on 5/12/2016.
 */
@ScriptObjectDeserializer(deserializes = BlockStateMatcher.class)
public class BlockStateMatcherDeserializer implements IScriptObjectDeserializer {
    @Override
    public Object Deserialize(Object input) {
        // Make sure we can continue:
        if(!ScriptObjectMirror.class.isAssignableFrom(input.getClass()) &&
                !ScriptObject.class.isAssignableFrom(input.getClass())) return null;

        ScriptObjectMirror mirror;

        // The first case will probably not happen, but, just in case...
        if(ScriptObjectMirror.class.isAssignableFrom(input.getClass())) {
            mirror = (ScriptObjectMirror) input;
        } else {
            mirror = ScriptUtils.wrap((ScriptObject) input);
        }

        Map<String, Object> properties = new HashMap<>();

        for(String key : mirror.keySet()) {
            properties.put(key, mirror.get(key));
        }

        return new BlockStateMatcher(properties);
    }
}
