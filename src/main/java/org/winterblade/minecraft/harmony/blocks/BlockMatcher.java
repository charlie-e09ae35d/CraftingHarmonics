package org.winterblade.minecraft.harmony.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Matt on 5/14/2016.
 */
public class BlockMatcher {
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

    public boolean matches(IBlockState state) {
        return (state.getBlock().equals(block) && states.contains(state));
    }
}
