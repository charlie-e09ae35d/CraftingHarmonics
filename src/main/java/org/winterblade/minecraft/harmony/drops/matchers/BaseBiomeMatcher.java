package org.winterblade.minecraft.harmony.drops.matchers;

import com.google.common.collect.Lists;
import net.minecraft.entity.Entity;

import java.util.List;

/**
 * Created by Matt on 5/11/2016.
 */
public abstract class BaseBiomeMatcher {
    private final List<String> biomeIds;

    public BaseBiomeMatcher(String[] biomeIds) {
        this.biomeIds = Lists.newArrayList(biomeIds);
    }

    protected boolean matches(Entity entity) {
        return biomeIds.contains(entity.getEntityWorld().getBiomeGenForCoords(entity.getPosition()).getBiomeName());
    }
}
