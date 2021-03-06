package org.winterblade.minecraft.harmony.common.blocks;

import com.google.common.collect.ImmutableMap;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import jdk.nashorn.api.scripting.ScriptUtils;
import jdk.nashorn.internal.runtime.ScriptObject;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import org.winterblade.minecraft.scripting.api.IScriptObjectDeserializer;
import org.winterblade.minecraft.scripting.api.ScriptObjectDeserializer;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Matt on 5/12/2016.
 */
public class BlockStateMatcher {
    private final ImmutableMap<String, Object> properties;

    public BlockStateMatcher(Map<String, Object> properties) {
        this.properties = ImmutableMap.copyOf(properties);
    }

    public boolean matches(IBlockState state) {
        for(IProperty<?> property : state.getPropertyNames()) {
            // If we're not checking this, just bail:
            if(!properties.containsKey(property.getName())) continue;

            // Get our values:
            Object value = state.getValue(property);
            Object expected = properties.get(property.getName());

            // Try to compare by value, then by making them strings:
            if(!value.equals(expected) && !value.toString().equals(expected.toString())) {
                return false;
            }
        }

        return true;
    }

    @ScriptObjectDeserializer(deserializes = BlockStateMatcher.class)
    public static class BlockStateMatcherDeserializer implements IScriptObjectDeserializer {
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
}
