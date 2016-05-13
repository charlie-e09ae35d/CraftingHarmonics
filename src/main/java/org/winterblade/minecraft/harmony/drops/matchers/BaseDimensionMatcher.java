package org.winterblade.minecraft.harmony.drops.matchers;

import com.google.common.collect.Lists;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import org.winterblade.minecraft.harmony.api.drops.BaseDropMatchResult;

import java.util.List;

/**
 * Created by Matt on 5/11/2016.
 */
public abstract class BaseDimensionMatcher {
    private final List<Integer> dimensionIds;

    public BaseDimensionMatcher(Integer[] dimensionIds) {
        this.dimensionIds = Lists.newArrayList(dimensionIds);
    }

    protected BaseDropMatchResult matches(Entity entity) {
        return new BaseDropMatchResult(dimensionIds.contains(entity.dimension));
    }

    protected BaseDropMatchResult matches(World world) {
        return new BaseDropMatchResult(dimensionIds.contains(world.provider.getDimension()));
    }
}
