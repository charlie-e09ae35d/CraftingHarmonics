package org.winterblade.minecraft.harmony.drops.matchers;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Created by Matt on 5/11/2016.
 */
public abstract class BaseLightLevelMatcher {
    private final int min;
    private final int max;

    public BaseLightLevelMatcher(int min, int max) {
        this.min = min;
        this.max = max;
    }

    protected boolean matches(Entity entity) {
        return entity != null && matches(entity.getEntityWorld(), entity.getPosition());
    }

    protected boolean matches(World world, BlockPos pos) {
        if(world == null || pos == null) return false;
        int light = world.getLight(pos);
        return min <= light && light <= max;
    }
}
