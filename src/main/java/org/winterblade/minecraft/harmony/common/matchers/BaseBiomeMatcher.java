package org.winterblade.minecraft.harmony.common.matchers;

import com.google.common.collect.Lists;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.winterblade.minecraft.harmony.api.BaseMatchResult;

import java.util.List;

/**
 * Created by Matt on 5/11/2016.
 */
public abstract class BaseBiomeMatcher {
    private final List<String> biomeIds;

    public BaseBiomeMatcher(String[] biomeIds) {
        this.biomeIds = Lists.newArrayList(biomeIds);
    }

    protected BaseMatchResult matches(Entity entity) {
        return matches(entity.getEntityWorld(), entity.getPosition());
    }

    protected BaseMatchResult matches(World world, BlockPos pos) {
        return new BaseMatchResult(biomeIds.contains(world.getBiomeGenForCoords(pos).getBiomeName()));
    }
}
