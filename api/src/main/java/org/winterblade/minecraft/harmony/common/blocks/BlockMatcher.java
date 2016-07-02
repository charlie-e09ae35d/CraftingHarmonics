package org.winterblade.minecraft.harmony.common.blocks;

import com.google.common.collect.ImmutableSet;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import jdk.nashorn.api.scripting.ScriptUtils;
import jdk.nashorn.internal.runtime.ScriptObject;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;
import org.winterblade.minecraft.harmony.common.utility.LogHelper;
import org.winterblade.minecraft.scripting.api.IScriptObjectDeserializer;
import org.winterblade.minecraft.scripting.api.ScriptObjectDeserializer;

import javax.annotation.Nullable;
import java.util.*;

/**
 * Created by Matt on 5/14/2016.
 */
public class BlockMatcher {
    private static final Block AIR = Block.REGISTRY.getObject(new ResourceLocation("minecraft:air"));
    private final Block block;
    private final Set<IBlockState> states = new HashSet<>();

    public BlockMatcher(Block block) {
        this(block, null);
    }

    public BlockMatcher(Block block, @Nullable BlockStateMatcher matcher) {
        this.block = block;

        // Add all valid block states:
        for(IBlockState state : block.getBlockState().getValidStates()) {
            if(state == null || (matcher != null && !matcher.matches(state))) continue;

            states.add(state);
        }
    }

    public Block getBlock() {
        return block;
    }

    public Set<IBlockState> getStates() {
        return ImmutableSet.copyOf(states);
    }

    public Optional<IBlockState> getFirstState() {
        return states.stream().findFirst();
    }

    public boolean matches(IBlockState state) {
        return (state.getBlock().equals(block) && states.contains(state));
    }

    @ScriptObjectDeserializer(deserializes = BlockMatcher.class)
    public static class BlockMatcherDeserializer implements IScriptObjectDeserializer {
        @Override
        public Object Deserialize(Object input) {
            // If we just have a string, go ahead and return that.
            if(input instanceof String) {
                Block b = stringToBlock((String) input);
                if (b == null) return null;

                return new BlockMatcher(b);
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
            Block b = stringToBlock((String)mirror.get("block"));

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
            if(ScriptObjectMirror.class.isAssignableFrom(state.getClass())) {
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

        /**
         * Translates the given string to a block, returning null if it's not found.
         * @param input    The input string
         * @return         The block, or null if none was found.
         */
        @Nullable
        private Block stringToBlock(String input) {
            if(input.indexOf(':') != input.lastIndexOf(':')) {
                LogHelper.warn("The block definition '{}' is invalid; specify this using block states, not metadata.", input);
                return null;
            }

            Block b = Block.REGISTRY.getObject(new ResourceLocation(input));

            // Make sure we don't have air, unless we wanted it:
            if(b == AIR && !input.equals("minecraft:air")) {
                LogHelper.warn("Cannot find a valid block for '{}'.", input);
                return null;
            }

            return b;
        }
    }
}
