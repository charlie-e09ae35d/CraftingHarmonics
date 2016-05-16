package org.winterblade.minecraft.harmony.drops.matchers;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.winterblade.minecraft.harmony.api.drops.BaseDropMatchResult;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matt on 5/15/2016.
 */
public abstract class BaseNearbyMobMatcher {
    private final List<String> mobs;
    private final int dist;
    private final int min;
    private final int max;

    public BaseNearbyMobMatcher(String[] mobs, int dist, int min, int max) {
        this.mobs = mobs != null ? Lists.newArrayList(mobs) : new ArrayList<>();
        this.dist = dist;
        this.min = min;
        this.max = max;

    }

    protected BaseDropMatchResult matches(World world, BlockPos pos) {
        // Initially set this to our block pos
        AxisAlignedBB bb = new AxisAlignedBB(pos);

        // Then expand it:
        bb.expandXyz(dist);

        // If we're just searching for a minimum number of mobs, and don't care about the max:
        int returnOn = max != Integer.MAX_VALUE ? Integer.MAX_VALUE : min;

        // Now find all the matching entities within that bounding box:
        int foundCount = world.getEntitiesWithinAABB(EntityLiving.class, bb, this::matchesEntity).size();

        return (min <= foundCount && foundCount <= max) ? BaseDropMatchResult.True : BaseDropMatchResult.False;
    }

    /**
     * Check if an entity should be matched under this list
     * @param entity    The entity to match against
     * @return          True if the mob matches, false otherwise.
     */
    private boolean matchesEntity(Entity entity) {
        return mobs.size() <= 0 || mobs.contains(entity.getClass().getName()) || mobs.contains(entity.getName());
    }
}
