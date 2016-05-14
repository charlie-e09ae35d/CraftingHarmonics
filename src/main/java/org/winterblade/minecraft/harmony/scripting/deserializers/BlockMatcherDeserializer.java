package org.winterblade.minecraft.harmony.scripting.deserializers;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import jdk.nashorn.api.scripting.ScriptUtils;
import jdk.nashorn.internal.runtime.ScriptObject;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;
import org.winterblade.minecraft.harmony.blocks.BlockMatcher;
import org.winterblade.minecraft.harmony.blocks.BlockStateMatcher;
import org.winterblade.minecraft.harmony.scripting.NashornConfigProcessor;
import org.winterblade.minecraft.scripting.api.IScriptObjectDeserializer;
import org.winterblade.minecraft.scripting.api.ScriptObjectDeserializer;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by Matt on 5/14/2016.
 */
@ScriptObjectDeserializer(deserializes = BlockMatcher.class)
public class BlockMatcherDeserializer implements IScriptObjectDeserializer {
    @Override
    public Object Deserialize(Object input) {
        // If we just have a string, go ahead and return that.
        if(input instanceof String) {
            Block b = Block.REGISTRY.getObject(new ResourceLocation((String) input));
            return b != null ? new BlockMatcher(b) : null;
        }

        // Otherwise, make sure we can continue:
        if(!ScriptObjectMirror.class.isAssignableFrom(input.getClass()) &&
                !ScriptObject.class.isAssignableFrom(input.getClass())) return null;

        ScriptObjectMirror mirror;

        // The first case will probably not happen, but, just in case...
        if(ScriptObjectMirror.class.isAssignableFrom(input.getClass())) {
            mirror = (ScriptObjectMirror) input;
        } else {
            mirror = ScriptUtils.wrap((ScriptObject) input);
        }

        if(!mirror.containsKey("block")) {
            return null;
        }

        // Get our block:
        Block b = Block.REGISTRY.getObject(new ResourceLocation((String) mirror.get("block")));

        // If we failed to find the block:
        if(b == null) return null;

        // Check if we have any states to check:
        if(!mirror.containsKey("state")) {
            // And bail if we didn't
            return new BlockMatcher(b);
        }

        Map<String, Object> properties = new HashMap<>();
        Object state = mirror.get("state");

        // Otherwise, make sure we can continue:
        if(!ScriptObjectMirror.class.isAssignableFrom(state.getClass()) &&
                !ScriptObject.class.isAssignableFrom(state.getClass())) return null;

        ScriptObjectMirror stateMirror;

        // The first case will probably not happen, but, just in case...
        if(ScriptObjectMirror.class.isAssignableFrom(input.getClass())) {
            stateMirror = (ScriptObjectMirror) state;
        } else {
            stateMirror = ScriptUtils.wrap((ScriptObject) state);
        }

        Set<String> keys = stateMirror.keySet();

        for(String key : keys) {
            properties.put(key, stateMirror.get(key));
        }

        return new BlockMatcher(b, new BlockStateMatcher(properties));
    }
}
