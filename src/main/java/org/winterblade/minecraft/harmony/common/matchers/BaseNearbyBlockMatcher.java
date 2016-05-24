package org.winterblade.minecraft.harmony.common.matchers;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.winterblade.minecraft.harmony.blocks.BlockMatcher;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Matt on 5/16/2016.
 */
public abstract class BaseNearbyBlockMatcher extends BaseAreaMatcher<BlockMatcher> {
    private final Map<IBlockState, Boolean> cache = new HashMap<>();

    @Nullable
    public BaseNearbyBlockMatcher(@Nullable BlockMatcher[] matcher, int dist, int min, int max) {
        super(matcher, dist, min, max);
    }

    @Override
    protected boolean matchWithinRange(World world, AxisAlignedBB boundingBox, int min, int max, int returnOn) {
        int found = 0;

        for(double x = boundingBox.minX; x < boundingBox.maxX; x++) {
            for(double z = boundingBox.minZ; z < boundingBox.maxZ; z++) {
                for(double y = boundingBox.minY; y < boundingBox.maxY; y++) {
                    if(!matchesBlock(world.getBlockState(new BlockPos(x, y, z)))) continue;

                    found++;
                    if(returnOn <= found) return true;
                    if(max < found) return false;
                }
            }
        }

        return (min <= found && found <= max);
    }

    /**
     * Go through our list of matchers and find any matching states
     * @param state    The state to match
     * @return         True if it does, false otherwise.
     */
    private boolean matchesBlock(IBlockState state) {
        if(cache.containsKey(state)) return cache.get(state);

        for(BlockMatcher matcher : matches) {
            if(!matcher.matches(state)) continue;

            cache.put(state, true);
            return true;
        }

        cache.put(state, false);
        return false;
    }
}
