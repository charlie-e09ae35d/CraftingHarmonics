package org.winterblade.minecraft.harmony.common.matchers;

import com.google.common.collect.Lists;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import org.winterblade.minecraft.harmony.api.BaseMatchResult;

import java.util.List;

/**
 * Created by Matt on 5/11/2016.
 */
public abstract class BaseDimensionMatcher {
    private final List<Integer> dimensionIds;

    public BaseDimensionMatcher(Integer[] dimensionIds) {
        this.dimensionIds = Lists.newArrayList(dimensionIds);
    }

    protected BaseMatchResult matches(Entity entity) {
        return new BaseMatchResult(dimensionIds.contains(entity.dimension));
    }

    protected BaseMatchResult matches(World world) {
        return new BaseMatchResult(dimensionIds.contains(world.provider.getDimension()));
    }
}
