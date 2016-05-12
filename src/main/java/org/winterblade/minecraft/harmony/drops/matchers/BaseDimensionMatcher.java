package org.winterblade.minecraft.harmony.drops.matchers;

import com.google.common.collect.Lists;
import net.minecraft.entity.Entity;

import java.util.List;

/**
 * Created by Matt on 5/11/2016.
 */
public abstract class BaseDimensionMatcher {
    private final List<Integer> dimensionIds;

    public BaseDimensionMatcher(Integer[] dimensionIds) {
        this.dimensionIds = Lists.newArrayList(dimensionIds);
    }

    protected boolean matches(Entity entity) {
        return dimensionIds.contains(entity.dimension);
    }
}
