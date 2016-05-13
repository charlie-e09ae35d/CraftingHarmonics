package org.winterblade.minecraft.harmony.blocks;

import com.google.common.collect.ImmutableMap;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;

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
}
