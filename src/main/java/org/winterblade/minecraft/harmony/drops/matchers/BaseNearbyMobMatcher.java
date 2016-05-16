package org.winterblade.minecraft.harmony.drops.matchers;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;

/**
 * Created by Matt on 5/15/2016.
 */
public abstract class BaseNearbyMobMatcher extends BaseAreaMatcher<String> {
    public BaseNearbyMobMatcher(String[] mobs, int dist, int min, int max) {
        super(mobs, dist, min, max);
    }

    /**
     * Check if an entity should be matched under this list
     * @param entity    The entity to match against
     * @return          True if the mob matches, false otherwise.
     */
    private boolean checkItem(Entity entity) {
        return matches.size() <= 0 || matches.contains(entity.getClass().getName()) || matches.contains(entity.getName());
    }

    @Override
    protected boolean matchWithinRange(World world, AxisAlignedBB boundingBox, int min, int max, int returnOn) {
        int foundCount = world.getEntitiesWithinAABB(EntityLivingBase.class, boundingBox, this::checkItem).size();
        return (min <= foundCount && foundCount <= max);
    }
}
