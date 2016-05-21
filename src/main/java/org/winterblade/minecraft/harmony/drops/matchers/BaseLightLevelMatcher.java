package org.winterblade.minecraft.harmony.drops.matchers;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.winterblade.minecraft.harmony.api.BaseMatchResult;

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

    protected BaseMatchResult matches(Entity entity) {
        return new BaseMatchResult(entity != null && checkLight(entity.getEntityWorld(), entity.getPosition()));
    }

    protected BaseMatchResult matches(World world, BlockPos blockPos) {
        return new BaseMatchResult(world != null && checkLight(world, blockPos));
    }

    private boolean checkLight(World world, BlockPos pos) {
        if(world == null || pos == null) return false;
        int light = world.getLight(pos);
        return min <= light && light <= max;
    }
}
