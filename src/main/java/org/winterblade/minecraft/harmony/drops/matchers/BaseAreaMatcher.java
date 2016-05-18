package org.winterblade.minecraft.harmony.drops.matchers;

import com.google.common.collect.ImmutableList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.winterblade.minecraft.harmony.api.drops.BaseDropMatchResult;

/**
 * Created by Matt on 5/16/2016.
 */
public abstract class BaseAreaMatcher <TSource> {
    protected final ImmutableList<TSource> matches;
    private final int dist;
    private final int min;
    private final int max;

    public BaseAreaMatcher(TSource[] types, int dist, int min, int max) {
        this.matches = types != null ? ImmutableList.copyOf(types) : ImmutableList.of();
        this.dist = dist;
        this.min = min;
        this.max = max;
    }

    protected final BaseDropMatchResult matches(World world, BlockPos pos) {
        // Get one from our block pos and expand it
        AxisAlignedBB bb = new AxisAlignedBB(pos).expandXyz(dist);

        // If we're just searching for a minimum number of mobs, and don't care about the max:
        int returnOn = max != Integer.MAX_VALUE ? Integer.MAX_VALUE : min;

        // Now find all the matching items within that bounding box:
        return matchWithinRange(world, bb, min, max, returnOn) ? BaseDropMatchResult.True : BaseDropMatchResult.False;
    }

    /**
     * Checks to see if the given area contains between the min and max range
     * @param world               The world to check
     * @param boundingBox         The bounding box to check in
     * @param min                 The minimum number to get
     * @param max                 The maximum number to get
     * @param returnEarlyCount    If the count exceeds this; return true.
     * @return                    True if the matched range is good, false otherwise
     */
    protected abstract boolean matchWithinRange(World world, AxisAlignedBB boundingBox, int min, int max, int returnEarlyCount);
}
